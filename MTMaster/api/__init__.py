import sys

from socketio import Client, exceptions


class API:

    def __init__(self, main):

        self.main = main

        self.namespace = "/api"

        self.client = Client(logger=self.main.debug,
                             engineio_logger=self.main.debug)

        self.client.on("connect", self.on_connect, namespace=self.namespace)
        self.client.on("disconnect", self.on_disconnect, namespace=self.namespace)
        self.client.on("api_error", self.on_api_error, namespace=self.namespace)
        self.client.on("servers", self.servers, namespace=self.namespace)
        self.client.on("agents", self.agents, namespace=self.namespace)
        self.client.on("logs", self.logs, namespace=self.namespace)
        self.client.on("change_mode", self.change_mode, namespace=self.namespace)
        self.client.on("command", self.command, namespace=self.namespace)

    @property
    def config(self):
        return self.main.config["api"]

    def run(self):

        try:

            self.connect()

            self.client.wait()

        except KeyboardInterrupt:

            self.main.quit()

    def connect(self):

        while self.main.running:

            try:
                self.client.connect(self.config["url"],
                                    auth={
                                        "key": self.main.sql.get_general_entry("api_key"),
                                        "id": "master"
                                    },
                                    namespaces=[self.namespace])
            except exceptions.ConnectionError:
                continue

            break

    def quit(self):

        self.client.disconnect()

    def on_api_error(self, data):

        self.main.logger.warning(f"API error: {data['error']}")

        if data["error"] in ["missing_auth", "invalid_key", "missing_id"]:
            self.main.logger.error("API authentication failed! Quit")
            self.main.quit()

    def on_connect(self):

        self.main.logger.info("API connected")

        self.agents({})
        self.servers({})
        self.logs({})

    def on_disconnect(self):

        self.main.logger.info("API disconnected")

    def servers(self, data):

        servers = []

        for server in self.main.sm.servers:
            servers.append({
                "name": server.instance.name,
                "host": server.instance.host,
                "address": server.instance.address,
                "agent": server.agent.name,
                "cluster": server.instance.cluster,
                "type": server.instance.type,
                "mode": server.instance.mode,
                "enabled": server.instance.enabled,
                "created": int(server.instance.created.timestamp()),
            })

        self.client.emit("servers", {"servers": servers}, namespace=self.namespace)

    def agents(self, data):

        agents = []

        for agent in self.main.sm.agents:
            agents.append({
                "name": agent.name,
                "created": int(agent.created.timestamp()),
            })

        self.client.emit("agents", {"agents": agents}, namespace=self.namespace)

    def logs(self, data):

        logs = {}

        for server in self.main.sm.servers:
            logs[server.instance.name] = server.logs

        size = sys.getsizeof(logs)
        if size > 10_000:
            self.main.logger.warning(f"Sending big package for logs with size {size}")

        self.client.emit("logs", logs, namespace=self.namespace)

    def change_mode(self, data):

        self.main.logger.debug(data)

        self.main.sm.change_mode(data["server"], data["mode"])

    def log_update(self, server, data):

        self.client.emit("log_update", {
            "server": server,
            "data": data
        }, namespace=self.namespace)

    def command(self, data):

        if data["command"] in ("stop", "end"):

            server = self.main.sm.get_server(data["server"])
            if server and server.instance.mode == "failure":
                session = self.main.sql.Session()
                session.add(server.instance)
                server.instance.mode = "manual"
                session.commit()
                self.main.sql.Session.remove()

                self.servers({})

        self.client.emit("command", data, namespace=self.namespace)

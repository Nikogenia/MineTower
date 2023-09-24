from sql import Instance, Agent
from server.waterfall import WaterfallServer
from server.paper import PaperServer

class ServerManager:

    def __init__(self, main):

        self.main = main

        self.servers = []
        self.agents = []

        session = self.sql.Session()

        for agent in session.query(Agent):
            self.agents.append(agent)

        for instance, agent in session.query(Instance, Agent).filter(Instance.agent == Agent.id):

            if agent.name != "master":
                self.main.logger.info(f"Remote server: {instance.name} ({agent.name})")

            if instance.type == "paper":
                self.main.logger.info(f"Paper server: {instance.name}")
                self.servers.append(PaperServer(self, agent, instance))

            if instance.type == "waterfall":
                self.main.logger.info(f"Waterfall server: {instance.name}")
                self.servers.append(WaterfallServer(self, agent, instance))

        self.sql.Session.remove()

    @property
    def sql(self):
        return self.main.sql

    def run(self):

        self.main.logger.info("Create servers")
        for server in self.servers:
            server.create()

        self.main.logger.info("Start servers")
        for server in self.servers:
            if server.instance.mode != "off":
                server.request_start = True
            server.start()

    def change_mode(self, server, mode):

        session = self.sql.Session()

        for s in self.servers:

            session.add(s.instance)

            if s.instance.name == server:

                s.instance.mode = mode

                session.commit()

                if mode == "off" and s.running():
                    s.stop()

                elif mode != "off" and not s.running():
                    s.request_start = True

                self.main.api.servers({})

        self.sql.Session.remove()

    def quit(self):

        self.main.logger.info("Stop servers")
        for server in self.servers:
            server.stop()

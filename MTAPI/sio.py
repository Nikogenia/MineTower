from flask_socketio import SocketIO, disconnect, emit, Namespace
from flask import request, session

from app import app, General
from session import get_user


sio = SocketIO(app, cors_allowed_origins=app.config["CORS_ORIGINS"],
               manage_session=False, engineio_logger=app.config["DEBUG"])


master = None


class API(Namespace):

    name = "/api"

    def on_connect(self, auth):

        global master

        if not auth:
            emit("api_error", {
                "error": "missing_auth",
                "message": "Missing API authentication!"
            }, namespace=self.name)
            disconnect()
            return

        if "key" not in auth:
            emit("api_error", {
                "error": "missing_auth",
                "message": "Missing API authentication!"
            }, namespace=self.name)
            disconnect()
            return

        if auth["key"] != General.query.filter_by(name="api_key").first().value:
            emit("api_error", {
                "error": "invalid_key",
                "message": "Invalid API key!"
            }, namespace=self.name)
            disconnect()
            return

        if "id" not in auth:
            emit("api_error", {
                "error": "missing_id",
                "message": "Missing API id!"
            }, namespace=self.name)
            disconnect()
            return

        if auth["id"] == "master":
            master = request.sid

        session["id"] = auth["id"]

        print(f"New API socket connection from {auth['id']}")

    def on_disconnect(self):

        global master

        if request.sid == master:
            print("API socket connection to master lost")
            master = None
        else:
            print("A API socket connection lost")

    def on_servers(self, data):

        emit("servers", data, broadcast=True, namespace=Control.name)

    def on_agents(self, data):

        emit("agents", data, broadcast=True, namespace=Control.name)

    def on_logs(self, data):

        emit("logs", data, broadcast=True, namespace=Control.name)

    def on_log_update(self, data):

        emit("log_update", data, broadcast=True, namespace=Control.name)

    def on_command(self, data):

        emit("command", data, broadcast=True, namespace=self.name, include_self=False)

    def on_tab_complete(self, data):

        emit("tab_complete", data, namespace=Control.name, to=data["session"])


class Control(Namespace):

    name = "/control"

    def setup_packet(self):

        user = get_user()
        if isinstance(user, tuple):
            emit("control_error", user[0].json, namespace=self.name)
            disconnect()
            return None

        if not master:
            emit("control_error", {
                "error": "master_offline",
                "message": "The master is offline!"
            }, namespace=self.name)
            return None

        return user

    @staticmethod
    def forward_packet(data, event):

        emit(event, data, namespace=API.name, to=master)

    def setup_forward_packet(self, data, event):

        user = self.setup_packet()
        if not user:
            return

        self.forward_packet(data, event)

    def on_connect(self):

        user = get_user()
        if isinstance(user, tuple):
            emit("control_error", user[0].json, namespace=self.name)
            disconnect()
            return

        print(f"New control socket connection from {user.name}")

    def on_disconnect(self):

        print("A control socket connection lost")

    def on_servers(self, data):

        self.setup_forward_packet(data, "servers")

    def on_agents(self, data):

        self.setup_forward_packet(data, "agents")

    def on_logs(self, data):

        self.setup_forward_packet(data, "logs")

    def on_change_mode(self, data):

        self.setup_forward_packet(data, "change_mode")

    def on_command(self, data):

        user = self.setup_packet()
        if not user:
            return

        data["user"] = user.name

        self.forward_packet(data, "command")

    def on_tab_complete(self, data):

        user = self.setup_packet()
        if not user:
            return

        data["session"] = request.sid

        emit("tab_complete", data, namespace=API.name, broadcast=True)


sio.on_namespace(API(API.name))
sio.on_namespace(Control(Control.name))

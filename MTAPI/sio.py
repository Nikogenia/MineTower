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

    def on_servers(self, data):

        emit("servers", data, broadcast=True, namespace=Control.name)

    def on_agents(self, data):

        emit("agents", data, broadcast=True, namespace=Control.name)

    def on_logs(self, data):

        emit("logs", data, broadcast=True, namespace=Control.name)

    def on_log_update(self, data):

        emit("log_update", data, broadcast=True, namespace=Control.name)


class Control(Namespace):

    name = "/control"

    def on_connect(self):

        user = get_user()
        if isinstance(user, tuple):
            emit("control_error", user[0].json, namespace=self.name)
            disconnect()
            return

        print(f"New control socket connection from {user.name}")

    def forward_to_master(self, data, event):

        user = get_user()
        if isinstance(user, tuple):
            emit("control_error", user[0].json, namespace=self.name)
            disconnect()
            return

        if not master:
            emit("control_error", {
                "error": "master_offline",
                "message": "The master is offline!"
            }, namespace=self.name)
            return

        emit(event, data, namespace=API.name, to=master)

    def on_servers(self, data):

        self.forward_to_master(data, "servers")

    def on_agents(self, data):

        self.forward_to_master(data, "agents")

    def on_logs(self, data):

        self.forward_to_master(data, "logs")

    def on_change_mode(self, data):

        self.forward_to_master(data, "change_mode")


sio.on_namespace(API(API.name))
sio.on_namespace(Control(Control.name))

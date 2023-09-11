from flask_socketio import SocketIO, disconnect, emit, Namespace
from flask import request, session

from app import app, General
from session import get_user


sio = SocketIO(app, cors_allowed_origins=app.config["CORS_ORIGINS"],
               manage_session=False, engineio_logger=app.config["DEBUG"])


master = None


class API(Namespace):

    def on_connect(self, auth):

        global master

        if not auth:
            emit("api_error", {
                "error": "missing_auth",
                "message": "Missing API authentication!"
            })
            disconnect()
            return

        if "key" not in auth:
            emit("api_error", {
                "error": "missing_auth",
                "message": "Missing API authentication!"
            })
            disconnect()
            return

        if auth["key"] != General.query.filter_by(name="api_key").first().value:
            emit("api_error", {
                "error": "invalid_key",
                "message": "Invalid API key!"
            })
            disconnect()
            return

        if "id" not in auth:
            emit("api_error", {
                "error": "missing_id",
                "message": "Missing API id!"
            })
            disconnect()
            return

        if auth["id"] == "master":
            master = request.sid

        session["id"] = auth["id"]

        print(f"New API socket connection from {auth['id']}")


class Control(Namespace):

    def on_connect(self):

        user = get_user()
        if isinstance(user, tuple):
            emit("control_error", user[0].json)
            disconnect()
            return

        print(f"New control socket connection from {user.name}")


sio.on_namespace(API("/api"))
sio.on_namespace(Control("/control"))

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
            emit("error", {
                "error": "missing_auth",
                "message": "Missing API authentication!"
            })
            disconnect()

        if "key" not in auth:
            emit("error", {
                "error": "missing_auth",
                "message": "Missing API authentication!"
            })
            disconnect()

        if auth["key"] != General.query.filter_by(name="api_key").first().value:
            emit("error", {
                "error": "invalid_key",
                "message": "Invalid API key!"
            })
            disconnect()

        if "id" not in auth:
            emit("error", {
                "error": "missing_id",
                "message": "Missing API id!"
            })
            disconnect()

        if auth["id"] == "master":
            master = request.sid

        session["id"] = auth["id"]

        print(f"New API socket connection from {auth['id']}")


class Backend(Namespace):

    def on_connect(self):

        user = get_user()
        if isinstance(user, tuple):
            emit("error", user[0].json)
            disconnect()

        print(f"New backend socket connection from {user.name}")


sio.on_namespace(API("/api"))
sio.on_namespace(Backend("/backend"))

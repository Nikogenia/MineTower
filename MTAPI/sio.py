from flask_socketio import SocketIO

from app import app


sio = SocketIO(app, cors_allowed_origins="*", manage_session=False)

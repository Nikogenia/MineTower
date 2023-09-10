from app import app
from sio import sio
import route


if __name__ == '__main__':

    sio.run(app, host="0.0.0.0", port=8080, use_reloader=False)

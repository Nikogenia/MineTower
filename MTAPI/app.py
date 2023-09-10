from flask import Flask
from flask_bcrypt import Bcrypt
from flask_cors import CORS
from flask_session import Session

from config import Config
from sql import db, User


app = Flask(__name__)

app.config.from_object(Config)

bcrypt = Bcrypt(app)

CORS(app, supports_credentials=True)
Session(app)

db.init_app(app)

with app.app_context():

    db.create_all()

    if User.query.filter_by(name="admin").first() is None:
        db.session.add(User(name="admin", password=bcrypt.generate_password_hash("admin"), admin=True))
        db.session.commit()

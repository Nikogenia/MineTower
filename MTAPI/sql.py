from flask_sqlalchemy import SQLAlchemy
from uuid import uuid4


db = SQLAlchemy()


class User(db.Model):

    __tablename__ = "user"

    id = db.Column(db.VARCHAR(32), primary_key=True, default=lambda: uuid4().hex)
    name = db.Column(db.VARCHAR(32), unique=True)
    password = db.Column(db.TEXT)
    admin = db.Column(db.BOOLEAN, default=False)
    created = db.Column(db.TIMESTAMP, server_default=db.func.current_timestamp())

    def __repr__(self):
        return (f"User({self.id} | name={self.name} | password={self.password}" +
                f" | admin={self.admin} | created={self.created})")

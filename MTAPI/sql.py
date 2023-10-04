from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import Column as Col
from sqlalchemy.dialects.mysql import VARCHAR, TEXT, BOOLEAN, TIMESTAMP, INTEGER
from uuid import uuid4


class Column(Col):

    inherit_cache = True

    def __init__(self, *psargs, **kwargs):
        kwargs.setdefault('nullable', False)
        super().__init__(*psargs, **kwargs)


db = SQLAlchemy()


class User(db.Model):

    __tablename__ = "user"

    id = Column(VARCHAR(32), primary_key=True, default=lambda: uuid4().hex)
    name = Column(VARCHAR(32), unique=True)
    password = Column(TEXT)
    admin = Column(BOOLEAN, default=False)
    created = Column(TIMESTAMP, server_default=db.func.current_timestamp())
    last_login = Column(TIMESTAMP, default=None)


class General(db.Model):

    __tablename__ = "general"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    name = Column(VARCHAR(32), unique=True)
    value = Column(TEXT)

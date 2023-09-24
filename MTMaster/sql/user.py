from uuid import uuid4

from sqlalchemy.dialects.mysql import VARCHAR, TEXT, BOOLEAN, TIMESTAMP
from sqlalchemy.sql import func, false, null
from sql.base import Base

from sql.column import Column


class User(Base):

    __tablename__ = "user"

    id = Column(VARCHAR(32), primary_key=True, default=lambda: uuid4().hex)
    name = Column(VARCHAR(32), unique=True)
    password = Column(TEXT)
    admin = Column(BOOLEAN, server_default=false())
    created = Column(TIMESTAMP, server_default=func.current_timestamp())
    last_login = Column(TIMESTAMP, nullable=True, server_default=null())

    def __repr__(self):
        return (f"User({self.id} | name={self.name} | password={self.password}" +
                f" | admin={self.admin} | created={self.created} | last_login={self.last_login})")

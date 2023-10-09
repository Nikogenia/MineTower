from sqlalchemy.dialects.mysql import INTEGER, VARCHAR, BOOLEAN, TIMESTAMP
from sqlalchemy.sql import false, null, text
from sqlalchemy import ForeignKey
from sql.base import Base

from sql.column import Column


class Player(Base):

    __tablename__ = "player"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    uuid = Column(VARCHAR(36), unique=True)
    name = Column(VARCHAR(16), unique=True)
    server = Column(INTEGER(unsigned=True), ForeignKey("instance.id"))
    online = Column(BOOLEAN, server_default=false())
    time_played = Column(INTEGER(unsigned=True), server_default=text("0"))
    first_joined = Column(TIMESTAMP, nullable=True, server_default=null())
    number_joined = Column(INTEGER(unsigned=True), server_default=text("0"))
    last_joined = Column(TIMESTAMP, nullable=True, server_default=null())
    last_disconnect = Column(TIMESTAMP, nullable=True, server_default=null())
    ban_until = Column(TIMESTAMP, nullable=True, server_default=null())
    ban_reason = Column(VARCHAR(255), server_default="")
    status = Column(INTEGER(unsigned=True), server_default=text("0"))

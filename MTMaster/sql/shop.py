from sqlalchemy.dialects.mysql import INTEGER, VARCHAR, TIMESTAMP
from sqlalchemy.sql import func
from sqlalchemy import ForeignKey
from sql.base import Base

from sql.column import Column


class Shop(Base):

    __tablename__ = "shop"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    name = Column(VARCHAR(32), unique=True)
    created = Column(TIMESTAMP, server_default=func.current_timestamp())
    owner = Column(INTEGER(unsigned=True), ForeignKey("player.id"), nullable=True)
    area = Column(VARCHAR(255))

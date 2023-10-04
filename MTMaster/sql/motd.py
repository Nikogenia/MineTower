from sqlalchemy.dialects.mysql import INTEGER, VARCHAR, TIMESTAMP
from sqlalchemy.sql import func
from sql.base import Base

from sql.column import Column


class Motd(Base):

    __tablename__ = "motd"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    name = Column(VARCHAR(32), unique=True)
    line1 = Column(VARCHAR(255))
    line2 = Column(VARCHAR(255))
    created = Column(TIMESTAMP, server_default=func.current_timestamp())

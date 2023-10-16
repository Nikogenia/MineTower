from sqlalchemy.dialects.mysql import INTEGER, VARCHAR, DOUBLE
from sqlalchemy import ForeignKey
from sql.base import Base

from sql.column import Column


class Home(Base):

    __tablename__ = "home"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    player = Column(INTEGER(unsigned=True), ForeignKey("player.id"))
    world = Column(VARCHAR(255))
    x = Column(DOUBLE)
    y = Column(DOUBLE)
    z = Column(DOUBLE)

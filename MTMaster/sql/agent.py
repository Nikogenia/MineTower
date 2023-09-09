from sqlalchemy.dialects.mysql import INTEGER, VARCHAR, TIMESTAMP
from sqlalchemy.sql import func
from sql.base import Base

from sql.column import Column


class Agent(Base):

    __tablename__ = "agent"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    name = Column(VARCHAR(32), unique=True)
    created = Column(TIMESTAMP, server_default=func.current_timestamp())

    def __repr__(self):
        return f"Agent({self.id} | name={self.name} | created={self.created})"

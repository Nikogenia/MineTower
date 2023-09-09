from sqlalchemy.dialects.mysql import INTEGER, VARCHAR, TIMESTAMP
from sqlalchemy.sql import func
from sql.base import Base

from sql.column import Column


class Cluster(Base):

    __tablename__ = "cluster"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    name = Column(VARCHAR(32), unique=True)
    created = Column(TIMESTAMP, server_default=func.current_timestamp())

    def __repr__(self):
        return f"Cluster({self.id} | name={self.name} | created={self.created})"

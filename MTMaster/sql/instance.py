from sqlalchemy.dialects.mysql import INTEGER, VARCHAR, BOOLEAN, TIMESTAMP
from sqlalchemy import ForeignKey
from sqlalchemy.sql import func, false, null, text
from sql.base import Base

from sql.column import Column


class Instance(Base):

    __tablename__ = "instance"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    name = Column(VARCHAR(32), unique=True)
    host = Column(VARCHAR(15), unique=True)
    address = Column(VARCHAR(21), unique=True)
    agent = Column(INTEGER(unsigned=True), ForeignKey("agent.id"))
    cluster = Column(INTEGER(unsigned=True), ForeignKey("cluster.id"), nullable=True, server_default=null())
    type = Column(VARCHAR(32))
    enabled = Column(BOOLEAN, server_default=false())
    created = Column(TIMESTAMP, server_default=func.current_timestamp())
    mode = Column(VARCHAR(32), server_default="off")
    memory = Column(VARCHAR(16), server_default="2048M")
    version = Column(VARCHAR(16), server_default="latest")

    def __repr__(self):
        return (f"Instance({self.id} | name={self.name} | host={self.host}" +
                f" | address={self.address} | agent={self.agent} | type={self.type}" +
                f" | cluster={self.cluster} | enabled={self.enabled} | created={self.created}" +
                f" | mode={self.mode} | memory={self.memory} | version={self.version})")

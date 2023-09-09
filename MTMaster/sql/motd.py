from sqlalchemy.dialects.mysql import INTEGER, VARCHAR
from sql.base import Base

from sql.column import Column


class Motd(Base):

    __tablename__ = "motd"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    name = Column(VARCHAR(32), unique=True)
    line1 = Column(VARCHAR(255))
    line2 = Column(VARCHAR(255))

    def __repr__(self):
        return (f"Motd({self.id} | name={self.name}" +
                f" | line1={self.line1} | line2={self.line2})")

from sqlalchemy.dialects.mysql import VARCHAR, INTEGER, TEXT
from sql.base import Base

from sql.column import Column


class General(Base):

    __tablename__ = "general"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    name = Column(VARCHAR(32), unique=True)
    value = Column(TEXT)

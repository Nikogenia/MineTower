from sqlalchemy.dialects.mysql import INTEGER, VARCHAR, BOOLEAN, TIMESTAMP
from sqlalchemy import ForeignKey
from sql.base import Base

from sql.column import Column


class Player(Base):

    __tablename__ = "player"

    id = Column(INTEGER(unsigned=True), primary_key=True)
    uuid = Column(VARCHAR(36), unique=True)
    name = Column(VARCHAR(16), unique=True)
    server = Column(INTEGER(unsigned=True), ForeignKey("instance.id"))
    online = Column(BOOLEAN, default=False)
    time_played = Column(INTEGER(unsigned=True), default=0)
    first_joined = Column(TIMESTAMP, nullable=True, default=None)
    number_joined = Column(INTEGER(unsigned=True), default=0)
    last_disconnect = Column(TIMESTAMP, nullable=True, default=None)

    def __repr__(self):
        return (f"Player({self.id} | uuid={self.uuid} | name={self.name}" +
                f" | server={self.server} | online={self.online} | time_played={self.time_played}" +
                f" | first_joined={self.first_joined} | number_joined={self.number_joined}" +
                f" | last_disconnect={self.last_disconnect})")

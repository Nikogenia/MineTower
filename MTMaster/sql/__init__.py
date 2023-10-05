import random
import string

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, scoped_session

from sql.base import Base
from sql.general import General
from sql.instance import Instance
from sql.agent import Agent
from sql.motd import Motd
from sql.player import Player
from sql.user import User
from sql.cluster import Cluster
from sql.shop import Shop


class SQL:

    def __init__(self, main):

        self.main = main

        self.engine = create_engine(
            f"mysql+mysqldb://{self.config['user']}:{self.config['password']}" +
            f"@{self.config['host']}:{self.config['port']}" +
            f"/{self.config['database']}?charset=utf8mb4",
            pool_recycle=3600)

        if self.main.debug:
            self.engine.echo = True

        Base.metadata.create_all(bind=self.engine)

        self.Session = scoped_session(sessionmaker(bind=self.engine, expire_on_commit=False))

    @property
    def config(self):
        return self.main.config["sql"]

    @staticmethod
    def define_general_entry(session, name, value):

        if not session.query(General).filter_by(name=name).first():
            session.add(General(name=name, value=value))

    def set_general_entry(self, name, value):

        session = self.Session()

        entry = session.query(General).filter_by(name=name).first()

        if not entry:
            session.add(General(name=name, value=value))
        else:
            entry.value = value

        session.commit()
        self.Session.remove()

    def get_general_entry(self, name):

        session = self.Session()

        entry = session.query(General).filter_by(name=name).first()

        if not entry:
            self.Session.remove()
            return ""

        self.Session.remove()
        return entry.value

    def init(self):

        self.init_general()
        self.init_agent()

    def init_agent(self):

        session = self.Session()

        if not session.query(Agent).filter_by(name="master").first():
            session.add(Agent(name="master"))

        session.commit()
        self.Session.remove()

    def init_general(self):

        session = self.Session()

        self.define_general_entry(session, "name", "minetower")
        self.define_general_entry(session, "full_name", "MineTower")

        self.define_general_entry(session, "docker_network", "minetower")
        self.define_general_entry(session, "docker_prefix", "minetower-")
        self.define_general_entry(session, "docker_root_path", "/root/minetower")

        key = "".join(random.choice(string.ascii_letters + string.digits) for i in range(32))
        self.define_general_entry(session, "api_key", key)

        self.define_general_entry(session, "time_zone", "Europe/Berlin")

        self.define_general_entry(session, "motd", "")

        session.commit()
        self.Session.remove()

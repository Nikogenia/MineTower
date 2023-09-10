import random
import string

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from sql.base import Base
from sql.general import General
from sql.instance import Instance
from sql.agent import Agent
from sql.motd import Motd
from sql.player import Player
from sql.user import User
from sql.cluster import Cluster


class SQL:

    def __init__(self, main):

        self.main = main

        self.engine = create_engine(
            f"mysql+mysqldb://{self.config['user']}:{self.config['password']}" +
            f"@{self.config['host']}:{self.config['port']}" +
            f"/{self.config['database']}?charset=utf8mb4")

        if self.main.debug:
            self.engine.echo = True

        Base.metadata.create_all(bind=self.engine)

        self.session = sessionmaker(bind=self.engine)()

    @property
    def config(self):
        return self.main.config["sql"]

    def define_general_entry(self, name, value):

        if not self.session.query(General).filter_by(name=name).first():
            self.session.add(General(name=name, value=value))

    def set_general_entry(self, name, value):

        entry = self.session.query(General).filter_by(name=name).first()
        if not entry:
            self.session.add(General(name=name, value=value))
        else:
            entry.value = value
        self.session.commit()

    def get_general_entry(self, name):

        entry = self.session.query(General).filter_by(name=name).first()
        if not entry:
            return ""
        return entry.value

    def init(self):

        self.init_general()
        self.init_agent()

    def init_agent(self):

        if not self.session.query(Agent).filter_by(name="master").first():
            self.session.add(Agent(name="master"))
        self.session.commit()

    def init_general(self):

        self.define_general_entry("name", "minetower")
        self.define_general_entry("full_name", "MineTower")

        self.define_general_entry("docker_network", "minetower")
        self.define_general_entry("docker_prefix", "minetower-")
        self.define_general_entry("docker_root_path", "/root/minetower")

        key = "".join(random.choice(string.ascii_letters + string.digits) for i in range(32))
        self.define_general_entry("api_key", key)

        self.define_general_entry("time_zone", "Europe/Berlin")

        self.define_general_entry("motd", "")

        self.session.commit()

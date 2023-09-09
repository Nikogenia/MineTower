from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from sql.base import Base
from sql.general import General
from sql.instance import Instance
from sql.agent import Agent
from sql.motd import Motd


class SQL:

    def __init__(self, main):

        self.main = main

        self.engine = create_engine(
            f"mysql+mysqldb://{self.config['user']}:{self.config['password']}" +
            f"@{self.config['host']}:{self.config['port']}" +
            f"/{self.config['database']}?charset=utf8mb4")

        self.engine.echo = True

        Base.metadata.create_all(bind=self.engine)

        self.session = sessionmaker(bind=self.engine)()

    @property
    def config(self):
        return self.main.config["sql"]

    def add_general_entry(self, name, value):

        if not self.session.query(General).filter_by(name=name).first():
            self.session.add(General(name=name, value=value))

    def init(self):

        self.init_general()
        self.init_agent()

    def init_agent(self):

        if not self.session.query(Agent).filter_by(name="master").first():
            self.session.add(Agent(name="master"))
        self.session.commit()

    def init_general(self):

        self.add_general_entry("name", "minetower")
        self.add_general_entry("full_name", "MineTower")

        self.add_general_entry("docker_network", "minetower")
        self.add_general_entry("docker_prefix", "minetower-")

        self.session.commit()

from sql import Instance, Agent
from server.waterfall import WaterfallServer


class ServerManager:

    def __init__(self, main):

        self.main = main

        self.servers = []
        self.agents = []

        for agent in self.sql.session.query(Agent):
            self.agents.append(agent)

        for instance, agent in self.sql.session.query(Instance, Agent).filter(Instance.agent == Agent.id):

            if agent.name != "master":
                self.main.logger.info(f"Remote server: {instance.name} ({agent.name})")

            if instance.type == "paper":
                self.main.logger.info(f"Paper server: {instance.name}")
                self.servers.append(WaterfallServer(self, agent, instance))

            if instance.type == "waterfall":
                self.main.logger.info(f"Waterfall server: {instance.name}")
                self.servers.append(WaterfallServer(self, agent, instance))

    @property
    def sql(self):
        return self.main.sql

    def start(self):

        print(self.sql.session.query(Instance).all())


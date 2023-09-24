from abc import ABC, abstractmethod
import threading as th


class Server(ABC, th.Thread):

    def __init__(self, sm, agent, instance):

        super().__init__(name=f"Server - {instance.name}", daemon=True)

        self.sm = sm
        self.agent = agent
        self.instance = instance

        self.logs = ""

        self.request_start = False

    @property
    def dc(self):
        return self.sm.main.dc

    @property
    def api(self):
        return self.sm.main.api

    @property
    def sql(self):
        return self.sm.main.sql

    @property
    def logger(self):
        return self.sm.main.logger

    @abstractmethod
    def running(self):
        pass

    @abstractmethod
    def create(self):
        pass

    @abstractmethod
    def stop(self):
        pass

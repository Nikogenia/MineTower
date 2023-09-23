from abc import ABC, abstractmethod
import threading as th


class Server(ABC, th.Thread):

    def __init__(self, sm, agent, instance):

        super().__init__(name=f"Server - {instance.name}")

        self.sm = sm
        self.agent = agent
        self.instance = instance

        self.logs = "Hello World!\n42"

    @abstractmethod
    def stop(self):
        pass

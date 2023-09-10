from abc import ABC, abstractmethod
import threading as th


class Server(ABC, th.Thread):

    def __init__(self, instance):

        super().__init__(name=f"Server - {instance.name}")

        self.instance = instance

        self.logs = ""

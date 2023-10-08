import os
from logging import Logger, Formatter, StreamHandler, INFO, DEBUG
import sys
import signal

from constant import *
import sql
import config
from dc import Docker
from api import API
from server import ServerManager


class Main:

    def __init__(self):

        self.running = True

        self.debug = "-d" in sys.argv or "--debug" in sys.argv or "DEBUG" in os.environ

        formatter = Formatter("[%(asctime)s] [%(name)s] [%(levelname)s] %(message)s",
                              datefmt='%b %d %H:%M:%S')

        handler = StreamHandler(sys.stdout)
        handler.setFormatter(formatter)

        self.logger = Logger("MineTower", DEBUG if self.debug else INFO)
        self.logger.addHandler(handler)

        self.logger.info(f"Version: {VERSION}")
        self.logger.info(f"Author: {AUTHOR}")
        self.logger.info(f"Debug: {self.debug}")

        self.config = config.load(self)

        self.sql = sql.SQL(self)
        self.sql.init()

        if not self.running:
            return

        self.dc = Docker(self)

        self.api = API(self)

        self.sm = ServerManager(self)

    def run(self):

        self.sm.run()

        self.api.run()

    def quit(self, *args):

        if args:
            self.logger.info("Keyboard interrupted! Quit")

        self.running = False

        self.api.quit()

        self.sm.quit()

        self.logger.info("Exit")

    @property
    def timezone(self):
        return self.sql.get_general_entry("time_zone")


if __name__ == '__main__':

    main = Main()

    main.run()

    signal.signal(signal.SIGINT, main.quit)

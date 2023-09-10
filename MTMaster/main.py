import os
from logging import Logger, Formatter, StreamHandler, INFO, DEBUG
import sys

from constant import *
import sql
import config
from dc import Docker
from api import API


class Main:

    def __init__(self):

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

        self.dc = Docker(self)

        self.api = API(self)

    def run(self):

        pass


if __name__ == '__main__':

    main = Main()
    main.run()

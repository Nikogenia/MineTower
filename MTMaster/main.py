import os
from logging import Logger, Formatter, StreamHandler, INFO, DEBUG
import sys
import signal

from constant import VERSION, AUTHOR, HOST_MYSQL, HOST_INFLUXDB, HOST_API, HOST_CONSOLE
from dc import Docker
from sql import SQL
import config
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

        self.config = config.load(self.logger)

        self.logger.info(f"Name: {self.full_name} ({self.name})")
        self.logger.info(f"Path: {self.path}")

        self.dc = Docker(self)

        self.sql = SQL(self)

        if not self.running:
            return


        self.api = API(self)

        self.sm = ServerManager(self)

    @property
    def name(self):
        return self.config["name"]

    @property
    def full_name(self):
        return self.config["full_name"]

    @property
    def path(self):
        return self.config["path"]

    def format_path(self, path):
        return f"{self.path}/{path}"

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

    def init_docker(self):

        self.dc.create(self.dc.format_name("mysql"),
                       "mysql",
                       "latest",
                       self.dc.format_host(*HOST_MYSQL),
                       {"3306/tcp": self.config["mysql_port"]},
                       [
                           f"{self.format_path("mysql/config")}:/etc/mysql/conf.d",
                           f"{self.format_path("mysql/data")}:/var/lib/mysql"
                       ],
                       {"MYSQL_ROOT_PASSWORD": self.config["mysql_password"]})

        self.dc.create(self.dc.format_name("influxdb"),
                       "influxdb",
                       "latest",
                       self.dc.format_host(*HOST_INFLUXDB),
                       {"8086/tcp": self.config["influxdb_port"]},
                       [
                           f"{self.format_path("influxdb/config")}:/etc/influxdb2",
                           f"{self.format_path("influxdb/data")}:/var/lib/influxdb2"
                       ],
                       {
                           "DOCKER_INFLUXDB_INIT_MODE": "setup",
                           "DOCKER_INFLUXDB_INIT_USERNAME": "root",
                           "DOCKER_INFLUXDB_INIT_PASSWORD": self.config["influxdb_password"],
                           "DOCKER_INFLUXDB_INIT_ORG": self.config["influxdb_org"],
                           "DOCKER_INFLUXDB_INIT_BUCKET": self.config["influxdb_logs"],
                           "DOCKER_INFLUXDB_INIT_RETENTION": self.config["influxdb_retention"],
                           "DOCKER_INFLUXDB_INIT_ADMIN_TOKEN": self.config["influxdb_password"]
                       })

        self.dc.create(self.dc.format_name("api"),
                       "nikogenia/mt-api",
                       "latest",
                       self.dc.format_host(*HOST_API),
                       {"8080/tcp": self.config["api_port"]},
                       [f"{self.format_path("api")}:/api"],
                       {
                           "DOCKER_INFLUXDB_INIT_MODE": "setup",
                           "DOCKER_INFLUXDB_INIT_USERNAME": "root",
                           "DOCKER_INFLUXDB_INIT_PASSWORD": self.config["influxdb_password"],
                           "DOCKER_INFLUXDB_INIT_ORG": self.config["influxdb_org"],
                           "DOCKER_INFLUXDB_INIT_BUCKET": self.config["influxdb_logs"],
                           "DOCKER_INFLUXDB_INIT_RETENTION": self.config["influxdb_retention"],
                           "DOCKER_INFLUXDB_INIT_ADMIN_TOKEN": self.config["influxdb_password"]
                       })

    @property
    def timezone(self):
        return self.sql.get_general_entry("time_zone")


if __name__ == '__main__':

    main = Main()

    signal.signal(signal.SIGINT, main.quit)

    main.run()

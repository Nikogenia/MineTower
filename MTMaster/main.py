import os
from logging import Logger, Formatter, StreamHandler, INFO, DEBUG
import sys
import signal

from master.constant import *
from master import config
from master.docker import Docker
from master.sql import SQL, connector


class Main:

    def __init__(self):

        self.debug = "-d" in sys.argv or "--debug" in sys.argv or "DEBUG" in os.environ
        self.exit = False

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

        self.dc = Docker(self, self.config["docker_prefix"],
                         self.config["docker_network_name"],
                         self.config["docker_network_subnet"])
        self.init_docker()

        self.sql = SQL(self, self.dc.format_host(*HOST_MYSQL),
                       3306, self.config["mysql_password"])
        self.init_sql()

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

        pass

    def quit(self, *args):

        if args:
            self.logger.info("Received termination signal! Quit")
        self.exit = True
        self.sql.quit()
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
                           "DOCKER_INFLUXDB_INIT_BUCKET": "logs",
                           "DOCKER_INFLUXDB_INIT_RETENTION": self.config["influxdb_retention"],
                           "DOCKER_INFLUXDB_INIT_ADMIN_TOKEN": self.config["influxdb_password"]
                       })

        self.dc.create(self.dc.format_name("api"),
                       "nikogenia/mt-api",
                       "latest",
                       self.dc.format_host(*HOST_API),
                       {"5000/tcp": self.config["api_port"]},
                       [f"{self.format_path("api")}:/api"],
                       {
                           "API_PASSWORD": self.config["api_password"],
                           "API_URL": self.config["api_url"],
                           "CONSOLE_URL": self.config["console_url"],
                           "SUBNET": self.dc.network_subnet,
                       })

        self.dc.create(self.dc.format_name("console"),
                       "nikogenia/mt-console",
                       "latest",
                       self.dc.format_host(*HOST_CONSOLE),
                       {"3000/tcp": self.config["console_port"]},
                       [],
                       {
                           "API_URL": self.config["api_url"],
                       })

        for name in ("mysql", "influxdb", "api", "console"):
            if not self.dc.running(self.dc.format_name(name)):
                self.dc.start(self.dc.format_name(name))

    def init_sql(self):

        try:
            cursor = self.sql.cursor()
            cursor.execute(f"CREATE DATABASE IF NOT EXISTS {self.config["mysql_master"]};")
            cursor.execute(f"CREATE DATABASE IF NOT EXISTS {self.config["mysql_luckperms"]};")
            cursor.execute(f"USE {self.config["mysql_master"]};")
            self.sql.commit()
            with open(SCHEMA_PATH, "r") as file:
                for statement in file.read().split(";"):
                    try:
                        cursor.execute(statement)
                        self.sql.commit()
                    except connector.Error as e:
                        self.logger.info(f"TODO: Check error type: {e}")
            cursor.execute("INSERT IGNORE INTO agent (name) VALUES ('master');")
            self.sql.commit()
            cursor.close()
        except connector.Error as e:
            self.logger.error("ATTENTION")
            self.logger.error(f"Failed to initialize MySQL database! MySQL error: {e}")
            self.logger.error("Abort")
            self.quit()
        except OSError as e:
            self.logger.error("ATTENTION")
            self.logger.error(f"Failed to initialize MySQL database! OS error: {e}")
            self.logger.error("Abort")
            self.quit()


if __name__ == '__main__':

    main = Main()

    signal.signal(signal.SIGINT, main.quit)
    signal.signal(signal.SIGTERM, main.quit)

    main.run()

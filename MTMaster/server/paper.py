import time

from constant import DOCKER_IMAGE_PREFIX
from .server import Server


class PaperServer(Server):

    def running(self):

        return self.dc.running(self.docker_name)

    def create(self):

        self.logger.info(f"Create server {self.instance.name}")
        self.dc.create(self.docker_name,
                       f"{DOCKER_IMAGE_PREFIX}paper:{self.instance.version}",
                       self.instance.host, {},
                       [f"{self.dc.root_path}/{self.instance.name}:/server"],
                       {"TZ": self.sm.main.timezone, "MEMORY": self.instance.memory})

        self.logs = self.dc.get_logs(self.docker_name).decode("utf-8")

    def run(self) -> None:

        while self.sm.main.running:

            if not self.request_start:
                time.sleep(0.5)
                continue

            self.request_start = False

            self.logger.info(f"Start server {self.instance.name}")
            self.dc.start(self.docker_name)

            for line in self.dc.get_logs(self.docker_name, True, True):

                self.logs += line.decode("utf-8")
                if self.api.client.connected:
                    self.api.log_update(self.instance.name, line.decode("utf-8"))

            if self.instance.mode == "manual":

                session = self.sql.Session()
                session.add(self.instance)
                self.instance.mode = "off"
                session.commit()
                self.sql.Session.remove()

                if self.api.client.connected:
                    self.api.servers({})

            if self.instance.mode in ("failure", "always"):

                self.request_start = True

            time.sleep(5)

    def stop(self):

        self.logger.info(f"Stop server {self.instance.name}")
        self.dc.stop(self.docker_name)

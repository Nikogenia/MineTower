import time

from .server import Server


class WaterfallServer(Server):

    def running(self):

        return self.dc.running("minetower-proxy")

    def create(self):

        self.logger.info(f"Create server {self.instance.name}")
        self.dc.create("minetower-proxy", "nikogenia/mt-waterfall:latest",
                    "172.19.0.21", {"25565/tcp": 25565},
                    [r"C:\Users\Nikocraft\Dev\MineTower\Test\proxy:/proxy"],
                    {"TZ": self.sm.main.timezone})


    def run(self) -> None:

        self.logs = self.dc.get_logs("minetower-proxy", False).decode("utf-8")

        while self.sm.main.running:

            if not self.request_start:
                time.sleep(0.5)
                continue

            self.request_start = False

            self.logger.info(f"Start server {self.instance.name}")
            self.dc.start("minetower-proxy")

            self.logs = ""
            self.api.logs({})

            for line in self.dc.get_logs("minetower-proxy", True):
                self.logs += line.decode("utf-8")
                if self.api.client.connected:
                    self.api.log_update(self.instance.name, line.decode("utf-8"))

            if self.instance.mode == "manual":
                session = self.sql.Session()
                session.add(self.instance)
                self.instance.mode = "off"
                session.commit()
                self.sql.Session.remove()

    def stop(self):

        self.logger.info(f"Stop server {self.instance.name}")
        self.dc.stop("minetower-proxy")

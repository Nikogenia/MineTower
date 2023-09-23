from .server import Server


class WaterfallServer(Server):

    def run(self) -> None:

        print(f"RUN {self.instance.name}")

    def stop(self):
        pass

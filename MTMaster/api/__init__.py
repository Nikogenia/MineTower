import socketio


class API:

    def __init__(self, main):

        self.main = main

        self.client = socketio.Client(logger=self.main.debug, engineio_logger=self.main.debug)

        self.client.connect(self.config["url"],
                            auth={
                                "key": self.main.sql.get_general_entry("api_key"),
                                "id": "master"
                            },
                            namespaces=["/api"])

    @property
    def config(self):
        return self.main.config["api"]

    def get_servers(self, data):
        print("MOIN")

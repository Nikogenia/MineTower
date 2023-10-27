import docker


class Docker:

    def __init__(self, main):

        self.main = main

        self.logger.info(f"Docker prefix: {self.prefix}")
        self.logger.info(f"Docker network: {self.network_name} ({self.network_subnet})")

        self.logger.info("Connect to docker socket")
        self.client = docker.from_env()

        self.logger.info("Log all existing containers")
        for container in self.get_containers_filtered():
            self.logger.info(f"Container: {container.name} | {container.image.tags} | {container.status}")

    @property
    def logger(self):
        return self.main.logger

    @property
    def config(self):
        return self.main.config

    @property
    def prefix(self):
        return self.config["docker_prefix"]

    @property
    def network_name(self):
        return self.config["docker_network_name"]

    @property
    def network_subnet(self):
        return self.config["docker_network_subnet"]

    def format_host(self, x, y):
        return self.network_subnet.replace("x", x).replace("y", y)

    def format_name(self, name):
        return f"{self.prefix}{name}"

    def format_image(self, repo, tag="latest"):
        return f"{repo}:{tag}"

    def get_containers(self):
        return self.client.containers.list(True)

    def get_images(self):
        return self.client.images.list()

    def get_containers_filtered(self):

        containers = []

        for container in self.get_containers():
            if container.name.startswith(self.prefix):
                containers.append(container)

        return containers

    def get_network(self):

        for network in self.client.networks.list():
            if network.name == self.network_name:
                return network

    def get_logs(self, name, since):

        for container in self.get_containers():
            if container.name == name:
                return container.logs(stream=True, since=since)

    def pull_image(self, repo, tag="latest"):

        for image in self.get_images():
            for t in image.tags:
                if t == self.format_image(repo, tag):
                    return

        self.main.logger.info(f"Pull image {self.format_image(repo, tag)}")

        self.client.images.pull(repo, tag)

    def create(self, name, repo, tag, host, ports, volumes, env, restart=""):

        for container in self.get_containers():
            if container.name == name:
                return

        self.pull_image(repo, tag)

        self.main.logger.info(f"Create container {name} ({host}) with {self.format_image(repo, tag)}")

        container = self.client.containers.create(
            image=self.format_image(repo, tag),
            name=name,
            detach=True,
            stdin_open=True,
            volumes=volumes,
            ports=ports,
            environment=env,
            restart_policy={"Name": restart, "MaximumRetryCount": 5} if restart else None
        )

        self.get_network().connect(container, ipv4_address=host)

    def start(self, name):

        for container in self.get_containers():
            if container.name == name:
                self.main.logger.info(f"Start container {name}")
                container.start()

    def stop(self, name):

        for container in self.get_containers():
            if container.name == name:
                self.main.logger.info(f"Stop container {name}")
                container.stop()

    def delete(self, name):

        for container in self.get_containers():
            if container.name == name:
                self.main.logger.info(f"Delete container {name}")
                container.stop()

    def running(self, name):

        for container in self.get_containers():
            if container.name == name:
                return container.status == "running"

    def quit(self):

        self.logger.info("Disconnect from docker socket")
        self.client.close()

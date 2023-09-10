import docker


class Docker:

    def __init__(self, main):

        self.main = main

        self.client = docker.from_env()

        for container in self.get_containers():
            self.main.logger.debug(f"Container: {container.name} | {container.image.tags} | {container.status} | {container.id}")

        for image in self.get_images():
            self.main.logger.debug(f"Image: {image.tags} | {image.id}")

        self.create("minetower-test", "nikogenia/mt-paper:latest",
                    "172.19.0.20", {"25565/tcp": 25565},
                    [r"C:\Users\Nikocraft\Dev\MineTower\Test\test:/server"])

        self.start("minetower-test")

    @property
    def prefix(self):
        return self.main.sql.get_general_entry("docker_prefix")

    @property
    def network(self):
        return self.main.sql.get_general_entry("docker_network")

    def get_containers(self):

        containers = []

        for container in self.client.containers.list(True):
            if container.name.startswith(self.prefix):
                containers.append(container)

        return containers

    def get_images(self):

        images = []

        for image in self.client.images.list():
            for tag in image.tags:
                if tag.startswith("nikogenia/mt-"):
                    images.append(image)
                    continue

        return images

    def get_network(self):

        for network in self.client.networks.list():
            if network.name == self.network:
                return network

    def get_logs(self, name):

        for container in self.get_containers():
            if container.name == name:
                return container.logs(stream=True)

    def create(self, name, image, address, ports, volumes):

        for container in self.get_containers():
            if container.name == name:
                return

        self.main.logger.info(f"Create container {name} ({address}) with {image}")

        container = self.client.containers.create(
            image=image,
            name=name,
            detach=True,
            stdin_open=True,
            volumes=volumes,
            ports=ports
        )

        self.get_network().connect(container, ipv4_address=address)

    def start(self, name):

        for container in self.get_containers():
            if container.name == name:

                self.main.logger.info(f"Start container {name}")

                container.start()

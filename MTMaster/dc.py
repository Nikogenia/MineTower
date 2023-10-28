import docker
import time

from constant import DOCKER_IMAGE_PREFIX


class Docker:

    def __init__(self, main):

        self.main = main

        self.client = docker.from_env()

        for container in self.get_containers():
            self.main.logger.info(f"Container: {container.name} | {container.image.tags} | {container.status} | {container.id}")

        for image in self.get_images():
            self.main.logger.info(f"Image: {image.tags} | {image.id}")

    @property
    def prefix(self):
        return self.main.sql.get_general_entry("docker_prefix")

    @property
    def network(self):
        return self.main.sql.get_general_entry("docker_network")

    @property
    def root_path(self):
        return self.main.sql.get_general_entry("docker_root_path")

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
                if tag.startswith(DOCKER_IMAGE_PREFIX):
                    images.append(image)
                    break

        return images

    def get_network(self):

        for network in self.client.networks.list():
            if network.name == self.network:
                return network

    def get_logs(self, name, stream=False, since_now=False):

        for container in self.get_containers():
            if container.name == name:
                if since_now:
                    return container.logs(stream=stream, since=time.time() - 5, tail=1000)
                return container.logs(stream=stream, tail=1000)

    def pull_image(self, tag):

        for image in self.get_images():
            for t in image.tags:
                if t == tag:
                    return

        self.main.logger.info(f"Pull image {tag}")

        self.client.images.pull(*tag.split(":"))

    def create(self, name, image, address, ports, volumes, env):

        for container in self.get_containers():
            if container.name == name:
                return

        self.pull_image(image)

        self.main.logger.info(f"Create container {name} ({address}) with {image}")

        container = self.client.containers.create(
            image=image,
            name=name,
            detach=True,
            stdin_open=True,
            volumes=volumes,
            ports=ports,
            environment=env
        )

        self.get_network().connect(container, ipv4_address=address)

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

    def running(self, name):

        for container in self.get_containers():
            if container.name == name:
                return container.status == "running"

import random
import string
import sys

import yaml

from master.constant import CONFIG_PATH


TEMPLATE = """# ---------
# MineTower
# ---------

# Name
name: "minetower"
full_name: "MineTower"

# Path
path: "/root/minetower"  # without '/' at the end

# Public URLs
console_url: "https://console.example.xyz"
api_url: "https://api.example.xyz"

# Exposed ports
mysql_port: 3342
influxdb_port: 8042
console_port: 3042
api_port: 5042

# Docker
docker_prefix: "minetower-"
docker_network_name: "minetower"
docker_network_subnet: "172.22.x.y"

# MySQL databases
mysql_master: "minetower_master"
mysql_luckperms: "minetower_luckperms"

# InfluxDB organisation and retention
influxdb_org: "minetower"
influxdb_retention: "4w"

# Passwords (read only)
mysql_password: "$PASSWORD1"  # DO NOT CHANGE!
influxdb_password: "$PASSWORD2"  # DO NOT CHANGE!
api_password: "$PASSWORD3"  # DO NOT CHANGE!

"""


def load(logger):

    try:
        logger.info("Load configuration file")
        with open(CONFIG_PATH, "r") as file:
            return yaml.safe_load(file)
    except IOError:
        logger.error("ATTENTION")
        logger.error("Failed to load configuration!")
        logger.error("Please adjust the values!")
        logger.error("Abort")
        with open(CONFIG_PATH, "w") as file:
            password1 = "".join(random.choice(string.ascii_letters + string.digits) for _ in range(32))
            password2 = "".join(random.choice(string.ascii_letters + string.digits) for _ in range(32))
            password3 = "".join(random.choice(string.ascii_letters + string.digits) for _ in range(32))
            file.write(TEMPLATE
                       .replace("$PASSWORD1", password1)
                       .replace("$PASSWORD2", password2)
                       .replace("$PASSWORD3", password3))
        sys.exit(0)

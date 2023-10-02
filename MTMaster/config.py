import sys

import yaml

from constant import *


TEMPLATE = """# ----------------
# MineTower Master
# ----------------

# MySQL server
sql:
  host: 172.22.0.2
  port: 3306
  user: root
  password:
  database:
    
# API
api:
  url: "http://172.22.1.2:8080"
"""


def load(main):

    try:

        with open(CONFIG, "r") as file:
            return yaml.safe_load(file)

    except IOError:

        main.logger.error("ATTENTION")
        main.logger.error("Failed to load configuration!")
        main.logger.error("Please adjust the MySQL server and API!")
        main.logger.error("Abort")

        with open(CONFIG, "w") as file:
            file.write(TEMPLATE)

        sys.exit(0)

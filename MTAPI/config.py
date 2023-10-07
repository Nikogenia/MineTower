import os
import random
import string
import sys

from dotenv import load_dotenv
import redis


TEMPLATE = """# -------------
# MineTower API
# -------------

# Debug mode
#DEBUG=

# Secret key
SECRET_KEY=#KEY#

# CORS origins
CORS_ORIGINS="http://localhost:3000,http://172.22.1.2:8080"

# MySQL server
MYSQL_HOST=172.22.0.2
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=
MYSQL_DATABASE=
    
# Redis server
REDIS_HOST=172.22.0.3
REDIS_PORT=6379
"""


def setup_template():

    if os.path.exists("./.env"):
        return

    print("ATTENTION")
    print("Failed to load configuration!")
    print("Please adjust the CORS origin, MySQL and Redis server!")
    print("Abort")

    key = "".join(random.choice(string.ascii_letters + string.digits) for i in range(32))
    with open("./.env", "w") as file:
        file.write(TEMPLATE.replace("#KEY#", key))

    sys.exit(0)


setup_template()
load_dotenv()


class Config:

    DEBUG = "DEBUG" in os.environ

    SECRET_KEY = os.environ["SECRET_KEY"].encode("utf-8")

    CORS_ORIGINS = os.environ["CORS_ORIGINS"].split(",")

    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_ECHO = "DEBUG" in os.environ
    SQLALCHEMY_DATABASE_URI = (f"mysql+mysqldb://{os.environ['MYSQL_USER']}" +
                               f":{os.environ['MYSQL_PASSWORD']}" +
                               f"@{os.environ['MYSQL_HOST']}" +
                               f":{os.environ['MYSQL_PORT']}"
                               f"/{os.environ['MYSQL_DATABASE']}")

    SESSION_TYPE = "redis"
    SESSION_PERMANENT = False
    SESSION_REDIS = redis.from_url(f"redis://{os.environ['REDIS_HOST']}" +
                                   f":{os.environ['REDIS_PORT']}")

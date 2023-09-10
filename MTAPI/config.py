import os
import random
import string

from dotenv import load_dotenv
import redis


TEMPLATE = """# -------------
# MineTower API
# -------------

# Debug mode
#DEBUG=

# Secret key
SECRET_KEY=#KEY#

# MySQL Server
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=
MYSQL_DATABASE=
    
# Redis Server
REDIS_HOST=localhost
REDIS_PORT=6379
"""


def setup_template():
    if os.path.exists("./.env"):
        return
    key = "".join(random.choice(string.ascii_letters + string.digits) for i in range(32))
    with open("./.env", "w") as file:
        file.write(TEMPLATE.replace("#KEY#", key))


setup_template()
load_dotenv()


class Config:

    DEBUG = "DEBUG" in os.environ

    SECRET_KEY = os.environ["SECRET_KEY"]

    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_ECHO = "DEBUG" in os.environ
    SQLALCHEMY_DATABASE_URI = (f"mysql+mysqldb://{os.environ['MYSQL_USER']}" +
                               f":{os.environ['MYSQL_PASSWORD']}" +
                               f"@{os.environ['MYSQL_HOST']}" +
                               f":{os.environ['MYSQL_PORT']}"
                               f"/{os.environ['MYSQL_DATABASE']}")

    SESSION_TYPE = "redis"
    SESSION_PERMANENT = False
    SESSION_USE_SIGNER = True
    SESSION_REDIS = redis.from_url(f"redis://{os.environ['REDIS_HOST']}" +
                                   f":{os.environ['REDIS_PORT']}")

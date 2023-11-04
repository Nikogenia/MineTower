import time

from mysql import connector
from mysql.connector import errorcode


class SQL:

    def __init__(self, main, host, port, password, database=None):

        self.main = main

        self.host = host
        self.port = port
        self.password = password
        self.database = database

        self.connection = self.connect()

    @property
    def logger(self):

        return self.main.logger

    def connect(self):

        self.logger.info("Connect to MySQL database")
        while not self.main.exit:
            try:
                return connector.connect(
                    host=self.host,
                    port=self.port,
                    user="root",
                    password=self.password
                )
            except connector.Error as e:
                if e.errno == errorcode.ER_ACCESS_DENIED_ERROR:
                    self.logger.error(f"MySQL connection failed! Access denied: {e}")
                elif e.errno == errorcode.ER_BAD_DB_ERROR:
                    self.logger.error(f"MySQL connection failed! Missing database: {e}")
                else:
                    self.logger.error(f"MySQL connection failed! Error unknown: {e}")
            time.sleep(10)

    def quit(self):

        if not self.connection.is_closed():
            self.logger.info("Close MySQL connection")
            self.connection.close()

    def cursor(self):

        self.connection.ping(reconnect=True)
        return self.connection.cursor()

    def commit(self):

        self.connection.commit()

    def set_general(self, name, data):

        try:
            cursor = self.cursor()
            statement = f"""INSERT INTO general (name, data)
                            VALUES (%(name)s, %(data)s) ON DUPLICATE KEY
                            UPDATE data = %(data)s;"""
            cursor.execute(statement, {
                "name": name,
                "data": data
            })
            self.connection.commit()
            cursor.close()
            return True
        except connector.Error:
            return False

    def get_general(self, name):

        try:
            cursor = self.cursor()
            statement = f"""SELECT data FROM general
                            WHERE name = %(name)s;"""
            cursor.execute(statement, {
                "name": name,
            })
            data = cursor.fetchone()
            cursor.close()
            return data
        except connector.Error:
            pass

from utils import print, format_size_1024

print("-----------------------")
print("MineTower Backup Script")
print("-----------------------")

import os
import time
import schedule

import paramiko
from dotenv import load_dotenv


load_dotenv()

HOST = os.environ["HOST"]
PORT = int(os.environ["PORT"])
USER = os.environ["USER"]
KEY = os.environ["KEY"]
PASSPHRASE = os.environ["PASSPHRASE"]
REMOTE_PATH = os.environ["REMOTE_PATH"]
LOCAL_PATH = "./backups/"

last_percentage = 0


def progress(downloaded, total):

    global last_percentage

    percentage = int(downloaded / total * 100)

    if percentage != last_percentage:
        last_percentage = percentage
        print(f"Progress: {format_size_1024(downloaded)}/{format_size_1024(total)} {percentage}%")


def main():

    global last_percentage

    try:

        print("Setup environment")

        last_percentage = 0

        os.makedirs(LOCAL_PATH, exist_ok=True)

        local = os.listdir(LOCAL_PATH)

        private_key = paramiko.ECDSAKey.from_private_key_file(KEY, PASSPHRASE)

        client = paramiko.SSHClient()
        client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

        client.connect(hostname=HOST, port=PORT, username=USER,
                       pkey=private_key, passphrase=PASSPHRASE)

        print(f"Connected to '{HOST}' as '{USER}'")

        print("FILESYSTEM STATISTICS")
        print(client.exec_command("df -h /dev/sda1")[1].read().decode("utf-8").strip())

        sftp = client.open_sftp()

        print("SCAN FOR NEW FILES")
        for file in sftp.listdir(REMOTE_PATH):

            if file in local:
                print(f"File {file} already available")
                continue

            print(f"New file '{file}' found")

            print("Start download")
            sftp.get(REMOTE_PATH + file, LOCAL_PATH + file, progress)

            print("Download finished")

        print("SCAN COMPLETED")

        sftp.close()

        client.close()

        print("Connection closed")

    except Exception as e:
        print(f"Error occurred: {e}")

    print("SLEEP 1 HOUR")


if __name__ == '__main__':

    print("Schedule tasks")
    schedule.every().hour.at(":10").do(main)

    print("Start scheduler")
    while True:
        schedule.run_pending()
        time.sleep(1)

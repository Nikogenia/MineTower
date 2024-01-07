from utils import print, format_size_1024

print("-----------------------")
print("MineTower Backup Script")
print("-----------------------")

import os
import sys
import time
import stat
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


def download_dir(sftp, path):
    os.makedirs(LOCAL_PATH + path)
    for file in sftp.listdir_attr(REMOTE_PATH + path):
        if stat.S_ISDIR(file.st_mode):
            download_dir(sftp, path + "/" + file.filename)
        else:
            print(f"Download file {path + '/' + file.filename}")
            sftp.get(REMOTE_PATH + path + "/" + file.filename, LOCAL_PATH + path + "/" + file.filename, progress)


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

        print("SCAN FOR NEW FILES AND DIRECTORIES")
        for file in sftp.listdir_attr(REMOTE_PATH):

            if stat.S_ISDIR(file.st_mode):

                if file.filename in local:
                    print(f"Directory {file.filename} already available")
                    continue

                print(f"New directory '{file.filename}' found")

                print("Start recursive download")
                download_dir(sftp, file.filename)

            else:

                if file.filename in local:
                    print(f"File {file.filename} already available")
                    continue

                print(f"New file '{file.filename}' found")

                print("Start file download")
                sftp.get(REMOTE_PATH + file.filename, LOCAL_PATH + file.filename, progress)

            print("Download finished")

        print("SCAN COMPLETED")

        sftp.close()

        client.close()

        print("Connection closed")

    except Exception as e:
        print(f"Error occurred: {e}")

    print("BACKUP COMPLETED")


if __name__ == '__main__':

    if "-s" not in sys.argv:
        main()
        exit(0)

    print("Schedule tasks")
    schedule.every().hour.at(":20").do(main)

    print("Start scheduler")
    while True:
        schedule.run_pending()
        time.sleep(1)

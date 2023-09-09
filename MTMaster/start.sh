#!/bin/bash

# Make directory
mkdir -p /minetower/master

# Copy files
shopt -s extglob
cp -r !(start.sh|requirements.txt) /minetower/master/

# Change workdir
# shellcheck disable=SC2164
cd /minetower/master

# Log version
echo "----------------------------------------"
echo "Python version"
echo "----------------------------------------"
python --version

# Start main
echo "----------------------------------------"
echo "Start main.py"
echo "----------------------------------------"
exec python main.py

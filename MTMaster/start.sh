#!/bin/bash

# Make directory
mkdir -p /mt/master

# Copy files
shopt -s extglob
cp -r !(start.sh|requirements.txt) /mt/master/

# Change workdir
# shellcheck disable=SC2164
cd /mt/master

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

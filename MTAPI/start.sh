#!/bin/bash

# Copy files
shopt -s extglob
cp -r !(start.sh|requirements.txt) /api/

# Change workdir
# shellcheck disable=SC2164
cd /api

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

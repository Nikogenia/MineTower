#!/bin/sh

# Log version
echo "----------------------------------------"
echo "Python version"
echo "----------------------------------------"
python --version

# Start main
echo "----------------------------------------"
echo "Start main.py"
echo "----------------------------------------"
exec python -OO main.py

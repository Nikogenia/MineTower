#!/bin/bash

# Setup environment
MEMORY=${MEMORY:="512M"}

# Copy files
shopt -s extglob
cp -r !(start.sh) /proxy/

# Change workdir
# shellcheck disable=SC2164
cd /proxy

# Log version
echo "----------------------------------------"
echo "Java version"
echo "----------------------------------------"
java --version

# Start waterfall.jar
echo "----------------------------------------"
echo "Start waterfall.jar (Memory: $MEMORY)"
echo "----------------------------------------"
exec java -Xms$MEMORY -Xmx$MEMORY -XX:+UseG1GC -XX:G1HeapRegionSize=4M -XX:+UnlockExperimentalVMOptions \
    -XX:+ParallelRefProcEnabled -XX:+AlwaysPreTouch -XX:MaxInlineLevel=15 -DIReallyKnowWhatIAmDoingISwear=true \
    -jar waterfall.jar --nogui

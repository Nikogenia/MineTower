#!/bin/bash

# Setup environment
MEMORY=${MEMORY:="2048M"}

# Copy files
shopt -s extglob
cp -r !(start.sh) /server/

# Change workdir
# shellcheck disable=SC2164
cd /server

# Log version
echo "----------------------------------------"
echo "Java version"
echo "----------------------------------------"
java --version

# Start paper.jar
echo "----------------------------------------"
echo "Start paper.jar (Memory: $MEMORY)"
echo "----------------------------------------"
exec java -Xms$MEMORY -Xmx$MEMORY --add-modules=jdk.incubator.vector -XX:+UseG1GC \
    -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions \
    -XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=4 \
    -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 \
    -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32 -XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1 \
    -Dusing.aikars.flags=https://mcflags.emc.gs -Daikars.new.flags=true -XX:G1NewSizePercent=30 \
    -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -DIReallyKnowWhatIAmDoingISwear=true \
    -jar paper.jar --nogui

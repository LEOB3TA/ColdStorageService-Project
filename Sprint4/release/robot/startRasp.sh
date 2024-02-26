#!/bin/bash

echo "HIT CTRL+C TO CLOSE THE PROGRAM"

function cleanup() {
    kill $pid1
    kill $pid2
    exit 0
}

# Set up a trap to catch the Ctrl+C signal and run the cleanup function
trap cleanup SIGINT

# shellcheck disable=SC2164
cd /unibo.basicrobot23-2.0/bin/

chmod +x ./unibo.basicrobot23

./unibo.basicrobot23 &
pid1=$!
sleep 5

# shellcheck disable=SC2164
cd ../../ctxRasp

java -jar unibo.ctxrasp-1.0-all.jar &
pid2=$!


# Keep the script running in the background
while true; do
    sleep 1
done





#!/bin/bash
echo "HIT CTRL+C TO CLOSE THE PROGRAM"

function cleanup() {
    # Stop the Docker container
    docker-compose -f webbasicrobot23.yaml down
    kill $pid1
    kill $pid2
    pkill frontend
    pkill servicestatusgui
    exit 0
}

# Set up a trap to catch the Ctrl+C signal and run the cleanup function
trap cleanup SIGINT

# Start the Docker containers using Docker Compose
docker-compose -f webbasicrobot23.yaml up -d

# Wait for the containers to initialize
echo "wait for the containers to inizialize"
sleep 3

# Open the default web browser to localhost:8090
open http://localhost:8090
sleep 15

# Start the unibo.prototipo3-1.0-all.jar file (assuming it's in the current directory)
java -jar unibo.ctxprotipo4-1.0-all.jar &
pid1=$!
sleep 5

#start the server
java -jar servicestatusbe.jar &
pid2=$!
sleep 5

# start the guis
open -a $(pwd)/ServiceAccessGUI/Mac/frontend.app
open -a $(pwd)/ServiceAccessGUI/Mac/frontend.app
open -a $(pwd)/ServiceStatusGUI/Mac/servicestatusgui.app

# Keep the script running in the background
while true; do
    sleep 1
done
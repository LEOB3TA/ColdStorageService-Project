#!/bin/bash
echo "HIT CTRL+C TO CLOSE THE PROGRAM"

function cleanup() {
    # Stop the Docker container
    docker-compose -f webbasicrobot23.yaml down
    kill $pid1
    kill $pid2
    kill $pid3
    kill $pid4
    kill $pid5
    exit 0
}

# Set up a trap to catch the Ctrl+C signal and run the cleanup function
trap cleanup SIGINT

# Start the Docker containers using Docker Compose
docker-compose -f webbasicrobot23.yaml up -d

# Wait for the containers to initialize
echo "wait for the containers to inizialize"
sleep 1

# Open the default web browser to localhost:8090
xdg-open http://localhost:8090
sleep 5

# Start the unibo.prototipo3-1.0-all.jar file (assuming it's in the current directory)
java -jar unibo.ctxprototipo4-1.0-all.jar &
pid1=$!
sleep 5

#start the server
java -jar servicestatusbe.jar &
pid2=$!
sleep 5

# start the guis
./ServiceAccessGUI/Linux/frontend &
pid3=$!
./ServiceAccessGUI/Linux/frontend &
pid4=$!
./ServiceStatusGUI/Linux/servicestatusgui &
pid5=$!


# Keep the script running in the background
while true; do
    sleep 1
done
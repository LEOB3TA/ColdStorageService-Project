#!/bin/bash
echo "HIT CTRL+C TO CLOSE THE PROGRAM"

function cleanup() {
    # Stop the Docker container
    docker-compose -f webbasicrobot23.yaml down
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

# Start the unibo.prototipo2-1.0-all.jar file (assuming it's in the current directory)
java -jar unibo.prototipo2-1.0-all.jar


# Keep the script running in the background
while true; do
    sleep 1
done
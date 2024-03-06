#!/bin/bash
echo "HIT CTRL+C TO CLOSE THE PROGRAM"

function cleanup() {
    kill $pid1
    kill $pid2
    kill $pid3
    exit 0
}

# Set up a trap to catch the Ctrl+C signal and run the cleanup function
trap cleanup SIGINT

# Start the unibo.prototipo3-1.0-all.jar file (assuming it's in the current directory)
java -jar unibo.ctxstorageservice-1.0-all.jar &
pid1=$!
sleep 5

#start the server
java -jar servicestatusbe.jar &
pid2=$!
sleep 5

# start the guis
./serviceStatusGUI/Linux/servicestatusgui &
pid3=$!


# Keep the script running in the background
while true; do
    sleep 1
done
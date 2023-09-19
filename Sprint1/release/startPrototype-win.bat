@echo off
echo HIT CTRL+C TO CLOSE THE PROGRAM

REM Define a function to gracefully shut down the Docker container
:cleanup
docker-compose -f webbasicrobot23.yaml down
exit /b 0

REM Set up a trap to catch the Ctrl+C signal and run the cleanup function
echo Waiting for containers to initialize...
docker-compose -f webbasicrobot23.yaml up -d
ping 127.0.0.1 -n 5 > nul

REM Open the default web browser to localhost:8090
start http://localhost:8090
ping 127.0.0.1 -n 5 > nul

REM Start the unibo.prototipo0-1.0-all.jar file (assuming it's in the current directory)
java -jar unibo.prototipo0-1.0-all.jar

REM Keep the script running in the background
:loop
ping 127.0.0.1 -n 3600 > nul
goto :loop
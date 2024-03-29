@echo off

REM Start the Docker container using Docker Compose
docker-compose -f webbasicrobot23.yaml up -d

REM Wait for a few seconds to ensure the container is up and running
timeout /t 5

REM Open the default browser to localhost:8090
start "" "http://localhost:8090"

REM Start the unibo.prototipo2-1.0-all.jar file in the background
start "prototipo3" java -jar unibo.ctxprotipo4-1.0-all.jar
timeout /t 5

start "servicestatusbe" java -jar servicestatusbe.jar
timeout /t 5

start "servicestatusgui" .\ServiceStatusGUI\Windows\servicestatusgui.exe
start "servicaccessgui" .\ServiceAccessGUI\Windows\frontend.exe
start "servicaccessgui1" .\ServiceAccessGUI\Windows\frontend.exe

REM Wait for the user to press Ctrl+C to stop the container
pause

REM Stop the Docker container gracefully
docker-compose -f webbasicrobot23.yaml down
taskkill /FI "WindowTitle eq prototipo3*" /T /F
taskkill /FI "WindowTitle eq servicestatusbe*" /T /F
taskkill /FI "WindowTitle eq servicestatusgui*" /T /F
taskkill /FI "WindowTitle eq servicaccessgui*" /T /F
taskkill /FI "WindowTitle eq servicaccessgui1*" /T /F
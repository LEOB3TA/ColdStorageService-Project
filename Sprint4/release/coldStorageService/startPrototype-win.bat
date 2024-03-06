@echo off

REM Start the Docker container using Docker Compose
docker-compose -f webbasicrobot23.yaml up -d

REM Wait for a few seconds to ensure the container is up and running
timeout /t 5

REM Open the default browser to localhost:8090
start "" "http://localhost:8090"

REM Start the unibo.prototipo2-1.0-all.jar file in the background
start "prototipo3" java -jar unibo.ctxstorageservice-1.0-all.jar
timeout /t 5

start "spring" docker-compose -f spring.yaml up -d
REM start "servicestatusbe" java -jar servicestatusbe.jar
timeout /t 5

start "servicestatusgui" ..\Windows\serviceAccWIN\serviceaccessgui.exe
start "servicaccessgui" ..\Windows\serviceStatWIN\servicestatusgui.exe

REM Wait for the user to press Ctrl+C to stop the container
pause

REM Stop the Docker container gracefully
docker-compose -f spring.yaml down
docker-compose -f webbasicrobot23.yaml down
taskkill /FI "WindowTitle eq CSS*" /T /F
taskkill /FI "WindowTitle eq servicestatusbe*" /T /F
taskkill /FI "WindowTitle eq servicestatusgui*" /T /F
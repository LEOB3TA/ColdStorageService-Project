@echo off

REM Start the unibo.prototipo2-1.0-all.jar file in the background
start "CSS" java -jar unibo.ctxstorageservice-1.0-all.jar
timeout /t 5

start "servicestatusbe" java -jar servicestatusbe.jar
timeout /t 5

start "servicestatusgui" .\GUIs\Windows\serviceStatWIN\servicestatusgui.exe
start "servicaccessgui" .\GUIs\Windows\serviceAccWIN\serviceaccessgui.exe

REM Wait for the user to press Ctrl+C to stop the container
pause

REM Stop the Docker container gracefully
docker-compose -f webbasicrobot23.yaml down
taskkill /FI "WindowTitle eq CSS*" /T /F
taskkill /FI "WindowTitle eq servicestatusbe*" /T /F
taskkill /FI "WindowTitle eq servicestatusgui*" /T /F
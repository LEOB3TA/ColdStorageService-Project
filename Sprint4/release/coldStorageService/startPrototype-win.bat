@echo off
REM TODO : connect the realrobot

REM Open the default browser to localhost:8090
start "" "http://localhost:8090"

REM Start the unibo.prototipo2-1.0-all.jar file in the background
start "prototipo3" java -jar unibo.ctxstorageservice-1.0-all.jar
timeout /t 5
REM se sei leo e ti stai chiedendo come ho fatto ad usare i nomi, ho buildato prima creando le immagini coi tag
start "CSS" docker run css:latest
REM start "servicestatusbe" java -jar servicestatusbe.jar
timeout /t 5

start "CSS" docker run spring:latest
REM start "servicestatusbe" java -jar servicestatusbe.jar
timeout /t 5

start "servicestatusgui" ..\Windows\serviceAccWIN\serviceaccessgui.exe
start "servicaccessgui" ..\Windows\serviceStatWIN\servicestatusgui.exe

REM Wait for the user to press Ctrl+C to stop the container
pause

REM Stop the Docker container gracefully
REM docker-compose -f spring.yaml down
REM docker-compose -f webbasicrobot23.yaml down
taskkill /FI "WindowTitle eq CSS*" /T /F
taskkill /FI "WindowTitle eq servicestatusbe*" /T /F
taskkill /FI "WindowTitle eq servicestatusgui*" /T /F
%====================================================================================
% prototipo1 description   
%====================================================================================
request( deposit, deposit(_) ).
request( sendTicket, sendTicket(TICKETID) ).
request( storeFood, storeFood(FW) ).
request( pickup, pickup(_) ).
request( engage, engage(ARG) ).
dispatch( disengage, disengage(ARG) ).
request( doplan, doplan(PATH,OWNER,STEPTIME) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
dispatch( setrobotstate, setpos(X,Y,D) ).
dispatch( setdirection, dir(D) ).
dispatch( cmd, cmd(MOVE) ).
event( local_movef, movef(_) ).
event( sonardata, distance(D) ).
event( obstacle, obstacle(D) ).
event( alarm, alarm(X) ).
event( robotmoving, robotmoving(_) ).
event( resume, resume(_) ).
dispatch( sonaractivate, info(D) ).
dispatch( ledCmd, ledCmd(ON,OFF,BLINK) ).
dispatch( coapUpdate, coapUpdate(RES,VAL) ).
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxprototipo1, "localhost",  "TCP", "8099").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( sonar, ctxprototipo1, "rx.sonarSimulator").
  qactor( datacleaner, ctxprototipo1, "rx.dataCleaner").
  qactor( distancefilter, ctxprototipo1, "rx.distanceFilter").
  qactor( mocktruck, ctxprototipo1, "it.unibo.mocktruck.Mocktruck").
  qactor( coldstorageservice, ctxprototipo1, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxprototipo1, "it.unibo.transporttrolley.Transporttrolley").
  qactor( sonar23, ctxprototipo1, "it.unibo.sonar23.Sonar23").
  qactor( ledqakactor, ctxprototipo1, "it.unibo.ledqakactor.Ledqakactor").
  qactor( controller23, ctxprototipo1, "it.unibo.controller23.Controller23").

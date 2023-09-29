%====================================================================================
% prototipo0 description   
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
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxprototipo0, "localhost",  "TCP", "8099").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mocktruck, ctxprototipo0, "it.unibo.mocktruck.Mocktruck").
  qactor( coldstorageservice, ctxprototipo0, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxprototipo0, "it.unibo.transporttrolley.Transporttrolley").

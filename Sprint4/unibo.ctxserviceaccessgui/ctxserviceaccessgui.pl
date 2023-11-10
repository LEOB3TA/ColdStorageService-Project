%====================================================================================
% ctxserviceaccessgui description   
%====================================================================================
request( deposit, deposit(_) ).
request( sendTicket, sendTicket(TICKETID) ).
request( storeFood, storeFood(FW) ).
reply( storeAccepted, storeAccepted(TICKETID) ).  %%for storeFood
reply( storeRejected, storeRejected(_) ).  %%for storeFood
reply( chargeTaken, chargeTaken(_) ).  %%for deposit
reply( ticketValid, ticketValid(_) ).  %%for sendTicket
reply( ticketNotValid, ticketNotValid(_) ).  %%for sendTicket
reply( ticketExpired, ticketExpired(_) ).  %%for sendTicket
request( pickup, pickup(_) ).
reply( pickupdone, pickupdone(_) ).  %%for pickup
request( engage, engage(ARG) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
dispatch( disengage, disengage(ARG) ).
request( doplan, doplan(PATH,OWNER,STEPTIME) ).
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(ARG) ).  %%for doplan
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
dispatch( setrobotstate, setpos(X,Y,D) ).
dispatch( setdirection, dir(D) ).
dispatch( cmd, cmd(MOVE) ).
event( local_movef, movef(_) ).
event( sonardata, distance(D) ).
event( alarm, alarm(X) ).
event( stop, stop(_) ).
event( resume, resume(_) ).
dispatch( sonaractivate, info(D) ).
dispatch( ledCmd, ledCmd(CMD) ).
dispatch( coapUpdate, coapUpdate(RES,VAL) ).
dispatch( gotomovetoport, gotomovetoport(_) ).
dispatch( gotodepositactionended, gotodepositactionended(_) ).
dispatch( gotorobottohome, gotorobottohome(_) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8099").
context(ctxserviceaccessgui, "localhost",  "TCP", "8099").
 qactor( coldstorageservice, ctxcoldstorageservice, "external").
  qactor( mocktruck, ctxserviceaccessgui, "it.unibo.mocktruck.Mocktruck").

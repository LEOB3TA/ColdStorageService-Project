%====================================================================================
% prototipo3 description   
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
dispatch( updateS, updateS(W) ).
dispatch( getData, getData(_) ).
dispatch( updateR, updateR(_) ).
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxprototipo3, "localhost",  "TCP", "8099").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( guicontroller, ctxprototipo3, "it.unibo.guicontroller.Guicontroller").
  qactor( coldstorageservice, ctxprototipo3, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxprototipo3, "it.unibo.transporttrolley.Transporttrolley").
  qactor( sonar23, ctxprototipo3, "it.unibo.sonar23.Sonar23").
  qactor( ledqakactor, ctxprototipo3, "it.unibo.ledqakactor.Ledqakactor").
  qactor( stateobservercontroller, ctxprototipo3, "it.unibo.stateobservercontroller.Stateobservercontroller").

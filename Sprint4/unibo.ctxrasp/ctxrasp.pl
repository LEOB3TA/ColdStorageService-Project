%====================================================================================
% ctxrasp description   
%====================================================================================
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
event( sonardata, distance(D) ).
event( alarm, alarm(X) ).
event( stop, stop(_) ).
event( resume, resume(_) ).
dispatch( sonaractivate, info(D) ).
dispatch( ledCmd, ledCmd(CMD) ).
dispatch( gotoblink, gotoblink(_) ).
dispatch( coapUpdate, coapUpdate(RES,VAL) ).
%====================================================================================
context(ctxrasp, "localhost",  "TCP", "8099").
 qactor( sonar, ctxrasp, "rx.sonarHCSR04Support23").
  qactor( datacleaner, ctxrasp, "rx.dataCleaner").
  qactor( sonar23, ctxrasp, "it.unibo.sonar23.Sonar23").
  qactor( ledqakactor, ctxrasp, "it.unibo.ledqakactor.Ledqakactor").

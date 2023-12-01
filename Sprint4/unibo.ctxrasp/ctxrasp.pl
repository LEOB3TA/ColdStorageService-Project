%====================================================================================
% ctxrasp description   
%====================================================================================
event( sonardata, distance(D) ).
event( alarm, alarm(X) ).
event( stop, stop(_) ).
event( resume, resume(_) ).
dispatch( sonaractivate, info(D) ).
dispatch( ledCmd, ledCmd(CMD) ).
%====================================================================================
context(ctxrasp, "localhost",  "TCP", "8099").
context(ctxstorageservice, "192.168.178.21",  "TCP", "8099").
 qactor( sonar, ctxrasp, "rx.sonarHCSR04Support23").
  qactor( datacleaner, ctxrasp, "rx.dataCleaner").
  qactor( sonar23, ctxrasp, "it.unibo.sonar23.Sonar23").
  qactor( ledqakactor, ctxrasp, "it.unibo.ledqakactor.Ledqakactor").

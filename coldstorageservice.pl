%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxsonarqak23, "localhost",  "TCP", "8128").
context(ctxledqak, "192.168.1.xxx",  "TCP", "8086").
context(ctxstorageservice, "localhost",  "TCP", "8090").
 qactor( sonar23, ctxsonarqak23, "sonarSimulator").
  qactor( ledqakactor, ctxledqak, "it.unibo.ledqakactor.Ledqakactor").
  qactor( controller23, ctxledqak, "it.unibo.controller23.Controller23").
  qactor( coldstorageserviceactor, ctxstorageservice, "it.unibo.coldstorageserviceactor.Coldstorageserviceactor").
  qactor( transporttrolley, ctxstorageservice, "it.unibo.transporttrolley.Transporttrolley").
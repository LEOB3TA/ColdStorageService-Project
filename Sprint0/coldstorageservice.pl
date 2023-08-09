%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxrasp, "192.168.1.xxx",  "TCP", "8086").
context(ctxstorageservice, "localhost",  "TCP", "8090").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( sonar23, ctxrasp, "sonarSimulator").
  qactor( ledqakactor, ctxrasp, "it.unibo.ledqakactor.Ledqakactor").
  qactor( controller23, ctxrasp, "it.unibo.controller23.Controller23").
  qactor( coldstorageserviceactor, ctxstorageservice, "it.unibo.coldstorageserviceactor.Coldstorageserviceactor").
  qactor( transporttrolley, ctxstorageservice, "it.unibo.transporttrolley.Transporttrolley").

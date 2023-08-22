%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxbasicrobot, "localhost",  "TCP", "8020").
context(ctxrasp, "192.168.1.xxx",  "TCP", "8086").
context(ctxstorageservice, "localhost",  "TCP", "8092").
context(ctxtruck, "localhost",  "TCP", "8087").
context(ctxmanager, "localhost",  "TCP", "8085").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( sonar23, ctxrasp, "rx.sonarSimulator").
  qactor( ledqakactor, ctxrasp, "it.unibo.ledqakactor.Ledqakactor").
  qactor( controller23, ctxrasp, "it.unibo.controller23.Controller23").
  qactor( coldstorageservice, ctxstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( serviceaccesgui, ctxtruck, "it.unibo.serviceaccesgui.Serviceaccesgui").
  qactor( servicestatusgui, ctxmanager, "it.unibo.servicestatusgui.Servicestatusgui").

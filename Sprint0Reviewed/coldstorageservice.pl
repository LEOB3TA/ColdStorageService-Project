%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxstorageservice, "localhost",  "TCP", "8092").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
context(ctxtruck, "localhost",  "TCP", "8087").
context(ctxrasp, "192.168.1.xxx",  "TCP", "8086").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( sonar23, ctxrasp, "rx.sonarSimulator").
  qactor( led, ctxrasp, "it.unibo.led.Led").
  qactor( serviceaccesgui, ctxtruck, "it.unibo.serviceaccesgui.Serviceaccesgui").
  qactor( servicestatusgui, ctxstorageservice, "it.unibo.servicestatusgui.Servicestatusgui").
  qactor( transporttrolley, ctxstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( coldstorageservice, ctxstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").

%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxledqak, "192.168.1.xxx",  "TCP", "8086").
context(ctxstorageservice, "localhost",  "TCP", "8090").
 qactor( ledqakactor, ctxledqak, "it.unibo.ledqakactor.Ledqakactor").
  qactor( coldstorageserviceactor, ctxstorageservice, "it.unibo.coldstorageserviceactor.Coldstorageserviceactor").
  qactor( transporttrolley, ctxstorageservice, "it.unibo.transporttrolley.Transporttrolley").

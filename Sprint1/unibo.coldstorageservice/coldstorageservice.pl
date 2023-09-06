%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8080").
context(ctxtruck, "localhost",  "TCP", "8092").
 qactor( mocktruck, ctxtruck, "external").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").

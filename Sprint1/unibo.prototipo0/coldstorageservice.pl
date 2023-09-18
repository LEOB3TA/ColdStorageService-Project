%====================================================================================
% coldstorageservice description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxprototipo0, "localhost",  "TCP", "8099").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mocktruck, ctxprototipo0, "it.unibo.mocktruck.Mocktruck").
  qactor( coldstorageservice, ctxprototipo0, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxprototipo0, "it.unibo.transporttrolley.Transporttrolley").

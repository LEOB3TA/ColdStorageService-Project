%====================================================================================
% mocktruck description   
%====================================================================================
context(ctxstorageservice, "localhost",  "TCP", "8099").
context(ctxtruck, "localhost",  "TCP", "8092").
 qactor( coldstorageservice, ctxstorageservice, "external").
  qactor( mocktruck, ctxtruck, "it.unibo.mocktruck.Mocktruck").

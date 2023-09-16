%====================================================================================
% mocktruck description   
%====================================================================================
context(ctxtruck, "localhost",  "TCP", "8092").
 qactor( coldstorageservice, ctxtruck, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( mocktruck, ctxtruck, "it.unibo.mocktruck.Mocktruck").

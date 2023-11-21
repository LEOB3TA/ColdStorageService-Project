%====================================================================================
% ctxserviceaccessgui description   
%====================================================================================
request( deposit, deposit(_) ).
request( sendTicket, sendTicket(TICKETID) ).
request( storeFood, storeFood(FW) ).
reply( storeAccepted, storeAccepted(TICKETID) ).  %%for storeFood
reply( storeRejected, storeRejected(_) ).  %%for storeFood
reply( chargeTaken, chargeTaken(_) ).  %%for deposit
reply( ticketValid, ticketValid(_) ).  %%for sendTicket
reply( ticketNotValid, ticketNotValid(_) ).  %%for sendTicket
reply( ticketExpired, ticketExpired(_) ).  %%for sendTicket
%====================================================================================
context(ctxcoldstorageservice, "192.168.178.21",  "TCP", "8099").
context(ctxserviceaccessgui, "192.168.178.81",  "TCP", "8099").
 qactor( coldstorageservice, ctxcoldstorageservice, "external").
  qactor( mocktruck, ctxserviceaccessgui, "it.unibo.mocktruck.Mocktruck").

%====================================================================================
% transporttrolley description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxcoldstorageservice, "localhost",  "TCP", "8092").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( pathobs, ctxbasicrobot, "observers.planexecCoapObserver").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").

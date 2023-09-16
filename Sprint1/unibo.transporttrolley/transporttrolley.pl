%====================================================================================
% transporttrolley description   
%====================================================================================
context(ctxstorageservice, "localhost",  "TCP", "8092").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
context(ctxtransporttrolley, "localhost",  "TCP", "8056").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( pathexec, ctxbasicrobot, "external").
  qactor( coldstorageservicecore, ctxstorageservice, "external").
  qactor( transporttrolleycore, ctxtransporttrolley, "it.unibo.transporttrolleycore.Transporttrolleycore").
  qactor( transporttrolleymover, ctxtransporttrolley, "it.unibo.transporttrolleymover.Transporttrolleymover").
  qactor( transporttrolleyexecutor, ctxtransporttrolley, "it.unibo.transporttrolleyexecutor.Transporttrolleyexecutor").

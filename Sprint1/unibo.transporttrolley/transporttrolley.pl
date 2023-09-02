%====================================================================================
% transporttrolley description   
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8092").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( transporttrolleycore, ctxcoldstorageservice, "it.unibo.transporttrolleycore.Transporttrolleycore").
  qactor( transporttrolleymover, ctxcoldstorageservice, "it.unibo.transporttrolleymover.Transporttrolleymover").
  qactor( transporttrolleyexecutor, ctxcoldstorageservice, "it.unibo.transporttrolleyexecutor.Transporttrolleyexecutor").

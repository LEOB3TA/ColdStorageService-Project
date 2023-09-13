/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var CRX = 4;
				var CRY = 3;
				var INDOORX=0;
				var INDOORY=4;
				var HOMEX=0;
				var HOMEY=0;
				var TICKETID = 0;
				val tTState = state.TransportTrolleyState()
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						discardMessages = false
						CommUtils.outgreen("$name | request engage")
						request("engage", "engage(transporttrolley,330)" ,"basicrobot" )  
						updateResourceRep(tTState.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_init", 
				 	 					  scope, context!!, "local_tout_transporttrolley_init", 10000.toLong() )
					}	 	 
					 transition(edgeName="t04",targetState="error",cond=whenTimeout("local_tout_transporttrolley_init"))   
					transition(edgeName="t05",targetState="idle",cond=whenReply("engagedone"))
					transition(edgeName="t06",targetState="waitforfree",cond=whenReply("engagerefused"))
				}	 
				state("error") { //this:State
					action { //it:State
						CommUtils.outred("$name | basic robot error")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("waitforfree") { //this:State
					action { //it:State
						CommUtils.outgreen("$name | already engaged")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_waitforfree", 
				 	 					  scope, context!!, "local_tout_transporttrolley_waitforfree", 10000.toLong() )
					}	 	 
					 transition(edgeName="t07",targetState="idle",cond=whenTimeout("local_tout_transporttrolley_waitforfree"))   
				}	 
				state("idle") { //this:State
					action { //it:State
						
									tTState.setCurrState(state.CurrStateTrolley.IDLE)
									tTState.setCurrPosition(state.TTPosition.HOME)
						updateResourceRep(tTState.toJsonString() 
						)
						CommUtils.outgreen("$name | waiting for commands.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t08",targetState="moverobottoindoor",cond=whenRequest("pickup"))
				}	 
				state("moverobottoindoor") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("pickup(_)"), Term.createTerm("pickup(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											tTState.setCurrState(state.CurrStateTrolley.PICKINGUP)
											tTState.setCurrPosition(state.TTPosition.INDOOR)
						}
						updateResourceRep(tTState.toJsonString() 
						)
						CommUtils.outgreen("$name | moving robot to indoor.")
						request("moverobot", "moverobot($INDOORX,$INDOORY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t29",targetState="movetoport",cond=whenReply("moverobotdone"))
					transition(edgeName="t210",targetState="robotmovefailed",cond=whenReply("moverobotfailed"))
				}	 
				state("movetoport") { //this:State
					action { //it:State
						
									tTState.setCurrState(state.CurrStateTrolley.MOVING)
									tTState.setCurrPosition(state.TTPosition.ONTHEROAD)
						updateResourceRep(tTState.toJsonString() 
						)
						CommUtils.outgreen("$name | robot is in indoor")
						CommUtils.outgreen("$name | moving robot to coldroom")
						request("moverobot", "moverobot($CRX,$CRY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t311",targetState="depositactionended",cond=whenReply("moverobotdone"))
					transition(edgeName="t312",targetState="robotmovefailed",cond=whenReply("moverobotfailed"))
				}	 
				state("depositactionended") { //this:State
					action { //it:State
						
									tTState.setCurrState(state.CurrStateTrolley.DROPPINGOUT)
									tTState.setCurrPosition(state.TTPosition.PORT)
						updateResourceRep(tTState.toJsonString() 
						)
						answer("pickup", "pickupdone", "pickupdone(_)"   )  
						CommUtils.outgreen("$name | robot is in coldroom")
						CommUtils.outgreen("$name | depositaction ended")
						CommUtils.outgreen("$name | waiting for next move")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_depositactionended", 
				 	 					  scope, context!!, "local_tout_transporttrolley_depositactionended", 3000.toLong() )
					}	 	 
					 transition(edgeName="t413",targetState="robottohome",cond=whenTimeout("local_tout_transporttrolley_depositactionended"))   
					transition(edgeName="t414",targetState="moverobottoindoor",cond=whenRequest("pickup"))
				}	 
				state("robottohome") { //this:State
					action { //it:State
						
									tTState.setCurrState(state.CurrStateTrolley.MOVING)
									tTState.setCurrPosition(state.TTPosition.ONTHEROAD)
						updateResourceRep(tTState.toJsonString() 
						)
						request("moverobot", "moverobot($HOMEX,$HOMEY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t515",targetState="idle",cond=whenReply("moverobotdone"))
					transition(edgeName="t516",targetState="robotmovefailed",cond=whenReply("moverobotfailed"))
				}	 
				state("robotmovefailed") { //this:State
					action { //it:State
						CommUtils.outred("$name | robot failed to move")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}

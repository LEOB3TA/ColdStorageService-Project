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
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class Transporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@OptIn(ExperimentalTime::class)
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var CRX = 4
				var CRY = 3
				var INDOORX= 0
				var INDOORY= 4
				var HOMEX= 0
				var HOMEY= 0
				//println("$HOMEX,$HOMEY")
				var TICKETID = 0
				val tTState = state.TransportTrolleyState()
				val MyName = name
				val ts = kotlin.time.TimeSource.Monotonic
				var m1 = ts.markNow()
				val MINT : kotlin.time.Duration= 1.seconds
				var savedState = tTState.getCurrState()
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						discardMessages = false
						CommUtils.outgreen("$name | request engage")
						request("engage", "engage($MyName,305)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t014",targetState="idle",cond=whenReply("engagedone"))
					transition(edgeName="t015",targetState="waitforfree",cond=whenReply("engagerefused"))
				}	 
				state("error") { //this:State
					action { //it:State
						CommUtils.outred("$name | basic robot error")
						CommUtils.outred("$name | disengaging....")
						forward("disengage", "disengage($MyName)" ,"basicrobot" ) 
						CommUtils.outred("$name | disengaged....")
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
					 transition(edgeName="t016",targetState="idle",cond=whenTimeout("local_tout_transporttrolley_waitforfree"))   
				}	 
				state("idle") { //this:State
					action { //it:State
						CommUtils.outgreen("$name | engaged")
						
									tTState.setCurrState(state.CurrStateTrolley.IDLE)
									tTState.setCurrPosition(state.TTPosition.HOME)
						updateResourceRep(tTState.toJsonString() 
						)
						forward("setrobotstate", "setpos(0,0,d)" ,"basicrobot" ) 
						CommUtils.outgreen("$name | waiting for commands.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t017",targetState="moverobottoindoor",cond=whenRequest("pickup"))
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
					 transition(edgeName="t018",targetState="handlerobotstopped",cond=whenEvent("stop"))
					transition(edgeName="t019",targetState="movetoport",cond=whenReply("moverobotdone"))
				}	 
				state("handlerobotstopped") { //this:State
					action { //it:State
						CommUtils.outgreen("$name |handle robot stopped")
						
						  			if ((m1+MINT).hasPassedNow()){
						  				m1 = ts.markNow()
						  				savedState = tTState.getCurrState()
						  				tTState.setCurrState(state.CurrStateTrolley.STOPPED)
						emit("alarm", "alarm(_)" ) 
						updateResourceRep(tTState.toJsonString() 
						)
						
						  			 }/*else{
						CommUtils.outred("ignored stop signal")
						tTState.setCurrState(savedState) 
						updateResourceRep(tTState.toJsonString() 
						)
						 	when {
													tTState.getCurrState() == state.CurrStateTrolley.PICKINGUP ->  
						request("moverobot", "moverobot($INDOORX,$INDOORY)" ,"basicrobot" )  
						
													tTState.getCurrState() == state.CurrStateTrolley.MOVINGTOPORT ->  
						request("moverobot", "moverobot($CRX,$CRY)" ,"basicrobot" )  
						
													tTState.getCurrState() == state.CurrStateTrolley.MOVINGTOHOME ->{  
						request("moverobot", "moverobot($HOMEX,$HOMEY)" ,"basicrobot" )  
						}} 
							}*/ 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t020",targetState="goahead",cond=whenReply("moverobotdone"))
					transition(edgeName="t021",targetState="handlerobotstopped",cond=whenEvent("stop"))
					transition(edgeName="t022",targetState="resumerobot",cond=whenEvent("resume"))
				}	 
				state("resumerobot") { //this:State
					action { //it:State
						CommUtils.outgreen("$name | resume robot")
						if( checkMsgContent( Term.createTerm("resume(_)"), Term.createTerm("resume(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								tTState.setCurrState(savedState) 
								updateResourceRep(tTState.toJsonString() 
								)
								 	when {
															tTState.getCurrState() == state.CurrStateTrolley.PICKINGUP ->  
								request("moverobot", "moverobot($INDOORX,$INDOORY)" ,"basicrobot" )  
								
															tTState.getCurrState() == state.CurrStateTrolley.MOVINGTOPORT ->  
								request("moverobot", "moverobot($CRX,$CRY)" ,"basicrobot" )  
								
															tTState.getCurrState() == state.CurrStateTrolley.MOVINGTOHOME ->{  
								request("moverobot", "moverobot($HOMEX,$HOMEY)" ,"basicrobot" )  
								}} 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t023",targetState="handlerobotstopped",cond=whenEvent("stop"))
					transition(edgeName="t024",targetState="goahead",cond=whenReply("moverobotdone"))
				}	 
				state("goahead") { //this:State
					action { //it:State
						CommUtils.outgreen("$name | go ahead with next state")
						 	when {
													tTState.getCurrState() == state.CurrStateTrolley.PICKINGUP ->  
						forward("gotomovetoport", "gotomovetoport(_)" ,"transporttrolley" ) 
						
													tTState.getCurrState() == state.CurrStateTrolley.MOVINGTOPORT ->  
						forward("gotodepositactionended", "gotodepositactionended(_)" ,"transporttrolley" ) 
						
													tTState.getCurrState() == state.CurrStateTrolley.MOVINGTOHOME ->{  
						forward("gotorobottohome", "gotorobottohome(_)" ,"transporttrolley" ) 
						
									}} 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t025",targetState="movetoport",cond=whenDispatch("gotomovetoport"))
					transition(edgeName="t026",targetState="depositactionended",cond=whenDispatch("gotodepositactionended"))
					transition(edgeName="t027",targetState="corrDir",cond=whenDispatch("gotorobottohome"))
				}	 
				state("movetoport") { //this:State
					action { //it:State
						
									tTState.setCurrState(state.CurrStateTrolley.MOVINGTOPORT)
									tTState.setCurrPosition(state.TTPosition.ONTHEROAD)
						updateResourceRep(tTState.toJsonString() 
						)
						CommUtils.outgreen("$name | robot is in indoor")
						CommUtils.outgreen("$name | moving robot to coldroom")
						answer("pickup", "pickupdone", "pickupdone(_)"   )  
						request("moverobot", "moverobot($CRX,$CRY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t028",targetState="handlerobotstopped",cond=whenEvent("stop"))
					transition(edgeName="t029",targetState="depositactionended",cond=whenReply("moverobotdone"))
				}	 
				state("depositactionended") { //this:State
					action { //it:State
						
									tTState.setCurrState(state.CurrStateTrolley.DROPPINGOUT)
									tTState.setCurrPosition(state.TTPosition.PORT)
						updateResourceRep(tTState.toJsonString() 
						)
						CommUtils.outgreen("$name | robot is in coldroom")
						CommUtils.outgreen("$name | depositaction ended")
						CommUtils.outgreen("$name | waiting for next move")
						
									tTState.setCurrState(state.CurrStateTrolley.IDLE)
									tTState.setCurrPosition(state.TTPosition.PORT)
						updateResourceRep(tTState.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_depositactionended", 
				 	 					  scope, context!!, "local_tout_transporttrolley_depositactionended", 3000.toLong() )
					}	 	 
					 transition(edgeName="t030",targetState="robottohome",cond=whenTimeout("local_tout_transporttrolley_depositactionended"))   
					transition(edgeName="t031",targetState="moverobottoindoor",cond=whenRequest("pickup"))
				}	 
				state("robottohome") { //this:State
					action { //it:State
						
									tTState.setCurrState(state.CurrStateTrolley.MOVINGTOHOME)
									tTState.setCurrPosition(state.TTPosition.ONTHEROAD)
						updateResourceRep(tTState.toJsonString() 
						)
						CommUtils.outgreen("$name | back to home")
						request("moverobot", "moverobot($HOMEX,$HOMEY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t032",targetState="corrDir",cond=whenReply("moverobotdone"))
					transition(edgeName="t033",targetState="handlerobotstopped",cond=whenEvent("stop"))
				}	 
				state("corrDir") { //this:State
					action { //it:State
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("robotmovefailed") { //this:State
					action { //it:State
						CommUtils.outred("$name | robot failed to move")
						emit("local_movef", "local_movef(_)" ) 
						CommUtils.outred("$name | close")
						 System.exit(0)  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}

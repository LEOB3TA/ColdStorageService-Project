/* Generated by AN DISI Unibo */ 
package it.unibo.mocktruck

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023
	
class Mocktruck ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
		       	var FW = 0
		        var DT = 1000L  //DT= driver time tempo che ci mette il driver dopo aver ricevuto la richiesta per arrivare alla INDOOR
		        var TICKETID = 0
		        val truckstate = state.TruckState()
		
		        fun initDriver(){
		        	FW =  kotlin.random.Random.nextInt(1, 101)
		        	DT = kotlin.random.Random.nextLong(1, 5001)
		        }
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("$name |	started")
						CommUtils.outred("this is an infinite loop, you need to kill the program to stop this")
						discardMessages = false
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						CommUtils.outgreen("$name |	in idle")
						
						        	initDriver()
						        	delay(kotlin.random.Random.nextLong(1, 15001))
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="sendStore", cond=doswitch() )
				}	 
				state("sendStore") { //this:State
					action { //it:State
						CommUtils.outgreen("$name |	sendStore")
						request("storeFood", "storeFood($FW)" ,"coldstorageservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_sendStore", 
				 	 					  scope, context!!, "local_tout_mocktruck_sendStore", 60000.toLong() )
					}	 	 
					 transition(edgeName="t00",targetState="handleError",cond=whenTimeout("local_tout_mocktruck_sendStore"))   
					transition(edgeName="t01",targetState="accepted",cond=whenReply("storeAccepted"))
					transition(edgeName="t02",targetState="rejected",cond=whenReply("storeRejected"))
				}	 
				state("rejected") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						CommUtils.outgreen("$name |	request rejected")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("accepted") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("storeAccepted(TICKETID)"), Term.createTerm("storeAccepted(TICKETID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								   			TICKETID = payloadArg(0).toInt()
						}
						CommUtils.outgreen("$name |	request accepted")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_accepted", 
				 	 					  scope, context!!, "local_tout_mocktruck_accepted", DT )
					}	 	 
					 transition(edgeName="t03",targetState="sendTicket",cond=whenTimeout("local_tout_mocktruck_accepted"))   
				}	 
				state("sendTicket") { //this:State
					action { //it:State
						request("sendTicket", "sendTicket($TICKETID)" ,"coldstorageservice" )  
						CommUtils.outgreen("$name |	send ticket")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_sendTicket", 
				 	 					  scope, context!!, "local_tout_mocktruck_sendTicket", 60000.toLong() )
					}	 	 
					 transition(edgeName="t04",targetState="handleError",cond=whenTimeout("local_tout_mocktruck_sendTicket"))   
					transition(edgeName="t05",targetState="sendDeposit",cond=whenReply("ticketValid"))
					transition(edgeName="t06",targetState="handleTicketExpired",cond=whenReply("ticketExpired"))
				}	 
				state("sendDeposit") { //this:State
					action { //it:State
						request("deposit", "deposit(_)" ,"coldstorageservice" )  
						CommUtils.outgreen("$name |	send deposit")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_sendDeposit", 
				 	 					  scope, context!!, "local_tout_mocktruck_sendDeposit", 60000.toLong() )
					}	 	 
					 transition(edgeName="t07",targetState="handleError",cond=whenTimeout("local_tout_mocktruck_sendDeposit"))   
					transition(edgeName="t08",targetState="idle",cond=whenReply("chargeTaken"))
				}	 
				state("handleError") { //this:State
					action { //it:State
						CommUtils.outred("$name |	COLD STORAGE SERVICE ERROR")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("handleTicketExpired") { //this:State
					action { //it:State
						CommUtils.outgreen("$name |	ticket expired")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("handleTicketNotValid") { //this:State
					action { //it:State
						CommUtils.outgreen("$name |	ticket Not valid retry")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="sendTicket", cond=doswitch() )
				}	 
			}
		}
}

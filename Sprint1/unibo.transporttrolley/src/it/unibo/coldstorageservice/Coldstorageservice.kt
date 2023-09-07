/* Generated by AN DISI Unibo */ 
package it.unibo.coldstorageservice

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Coldstorageservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "setup"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
					val MAXW = ColdStorageService.getMAXW()
					val TICKETTIME = ColdStorageService.getTICKETTIME()
				   	var currentWeightStorage = ColdStorageService.getCurrentWeightStorage()
				   	var requestWeightToStore = 0.0
				   	var TICKETNUMBER = ColdStorageService.getTicketNumber()
		    	  	
		return { //this:ActionBasciFsm
				state("setup") { //this:State
					action { //it:State
						CommUtils.outblue("$name |	setup")
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
						CommUtils.outblack("$name |	in idle")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="requestEvaluation",cond=whenRequest("storeFood"))
					transition(edgeName="t01",targetState="ticketEvaluation",cond=whenRequest("sendTicket"))
					transition(edgeName="t02",targetState="charged",cond=whenRequest("deposit"))
				}	 
				state("requestEvaluation") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storeFood(FW)"), Term.createTerm("storeFood(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("Request evaluation to store ${payloadArg(0)} kg")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="acceptRequest", cond=doswitchGuarded({ ColdStorageService.canStore(requestWeightToStore)  
					}) )
					transition( edgeName="goto",targetState="rejectRequest", cond=doswitchGuarded({! ( ColdStorageService.canStore(requestWeightToStore)  
					) }) )
				}	 
				state("acceptRequest") { //this:State
					action { //it:State
						
						    		var TICKET : Ticket = Ticket(TICKETNUMBER, TICKETTIME) 	
						    		ColdStorageService.incrementTicketNumber()	
						    		ColdStorageService.getTicketList().add(TICKET)
						answer("storeFood", "storeAccepted", "storeAccepted(TICKETNUMBER)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("rejectRequest") { //this:State
					action { //it:State
						
									 ColdStorageService.incrementRejectedRequestCounter()
						answer("storeFood", "storeRejected", "storeRejected(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("charged") { //this:State
					action { //it:State
						answer("deposit", "chargeTaken", "chargeTaken(_)"   )  
						request("pickup", "pickup(_)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("removeExpiredTicket") { //this:State
					action { //it:State
						CommUtils.outblue("Ticket of id ${payloadArg(0)} is expired - Reject Request")
						
						   	  		 val TICKETID = payloadArg(0).toInt()
						   	  		 val TICKET = ColdStorageService.getTicketById(TICKETID)
									 ColdStorageService.incrementRejectedRequestCounter()
									 ColdStorageService.getTicketList().remove(TICKET)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("sendInvalidTicket") { //this:State
					action { //it:State
						CommUtils.outred("Inserted ticket id is not valid")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("ticketEvaluation") { //this:State
					action { //it:State
						CommUtils.outblue("Ticket evaluation of ticket id ${payloadArg(0)}")
						
						    	        		 val TICKETID = payloadArg(0).toInt()
						if( checkMsgContent( Term.createTerm("sendTicket(TICKETID)"), Term.createTerm("sendTicket($TICKETID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val TICKETEVALUATION = ColdStorageService.evaluateTicket(TICKETID)  
								if( TICKETEVALUATION == TicketEvaluationResponse.VALID  
								 ){
								    	        			val TICKET = ColdStorageService.getTicketById(TICKETID)
															ColdStorageService.getTicketList().remove(TICKET)
								answer("sendTicket", "ticketValid", "ticketValid(_)"   )  
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="removeExpiredTicket", cond=doswitchGuarded({ ColdStorageService.evaluateTicket(payloadArg(0).toInt()) == TicketEvaluationResponse.EXPIRED  
					}) )
					transition( edgeName="goto",targetState="sendInvalidTicket", cond=doswitchGuarded({! ( ColdStorageService.evaluateTicket(payloadArg(0).toInt()) == TicketEvaluationResponse.EXPIRED  
					) }) )
				}	 
			}
		}
}

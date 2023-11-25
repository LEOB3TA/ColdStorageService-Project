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
		
		        	val MAXW = resources.ColdStorageService.getMAXW()
					val TICKETTIME = resources.ColdStorageService.getTICKETTIME()
				   	var currentWeightStorage = resources.ColdStorageService.getCurrentWeightStorage()
				   	var requestWeightToStore = 0.0
				   	var TICKETNUMBER = resources.ColdStorageService.getTicketNumber()
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
						CommUtils.outgreen("$name |	in idle")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="requestEvaluation",cond=whenRequest("storeFood"))
					transition(edgeName="t05",targetState="ticketEvaluation",cond=whenRequest("sendTicket"))
					transition(edgeName="t06",targetState="charged",cond=whenRequest("deposit"))
					transition(edgeName="t07",targetState="taken",cond=whenReply("pickupdone"))
					transition(edgeName="t08",targetState="error",cond=whenEvent("local_movef"))
				}	 
				state("requestEvaluation") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storeFood(FW)"), Term.createTerm("storeFood(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("Request evaluation to store ${payloadArg(0)} kg")
								
								           	requestWeightToStore=payloadArg(0).toDouble()
								           	if(requestWeightToStore + currentWeightStorage < MAXW ){
								forward("updateS", "updateS(requestWeightToStore)" ,"guicontroller" ) 
								
								           	}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="acceptRequest", cond=doswitchGuarded({ resources.ColdStorageService.canStore(requestWeightToStore)  
					}) )
					transition( edgeName="goto",targetState="rejectRequest", cond=doswitchGuarded({! ( resources.ColdStorageService.canStore(requestWeightToStore)  
					) }) )
				}	 
				state("acceptRequest") { //this:State
					action { //it:State
						
						    		TICKETNUMBER = resources.ColdStorageService.getTicketNumber()
						    		var TICKET : resources.model.Ticket = resources.model.Ticket(TICKETNUMBER, TICKETTIME)
						    		resources.ColdStorageService.incrementTicketNumber()
						    		resources.ColdStorageService.getTicketList().add(TICKET)
						answer("storeFood", "storeAccepted", "storeAccepted($TICKETNUMBER)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("rejectRequest") { //this:State
					action { //it:State
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
						CommUtils.outgreen("$name |	in charged")
						request("pickup", "pickup(_)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("taken") { //this:State
					action { //it:State
						CommUtils.outgreen("$name |	in taken")
						answer("deposit", "chargeTaken", "chargeTaken(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("ticketEvaluation") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("sendTicket(TICKETID)"), Term.createTerm("sendTicket(TICKETID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("Ticket evaluation of ticket id ${payloadArg(0)}")
								
								    	        		 val TICKETID = payloadArg(0).toInt()
								    	         	 val TICKETEVALUATION = resources.ColdStorageService.evaluateTicket(TICKETID)  
								if( TICKETEVALUATION == resources.TicketEvaluationResponse.VALID  
								 ){
								    	        			val TICKET = resources.ColdStorageService.getTicketById(TICKETID)
															resources.ColdStorageService.getTicketList().remove(TICKET)
								answer("sendTicket", "ticketValid", "ticketValid(_)"   )  
								}
								if( TICKETEVALUATION == resources.TicketEvaluationResponse.EXPIRED  
								 ){
								    	        			 resources.ColdStorageService.incrementRejectedRequestCounter()
								    	        			val TICKET = resources.ColdStorageService.getTicketById(TICKETID)
															resources.ColdStorageService.getTicketList().remove(TICKET)
								CommUtils.outblue("Ticket of id ${payloadArg(0)} is expired - Reject Request")
								answer("sendTicket", "ticketExpired", "ticketExpired(_)"   )  
								forward("updateR", "updateR(_)" ,"guicontroller" ) 
								}
								if( TICKETEVALUATION == resources.TicketEvaluationResponse.INVALID  
								 ){
								    	        			val TICKET = resources.ColdStorageService.getTicketById(TICKETID)
															resources.ColdStorageService.getTicketList().remove(TICKET)
								CommUtils.outred("Inserted ticket id is not valid")
								answer("sendTicket", "ticketNotValid", "ticketNotValid(_)"   )  
								forward("updateR", "updateR(_)" ,"guicontroller" ) 
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("error") { //this:State
					action { //it:State
						CommUtils.outred("$name | robot failed to move")
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

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
		
			val maxw = coldstorageservice.Constants.MAXW
		    var curretWeightStorage = 0.0
		    var requestWeightToStore = 0.0
		return { //this:ActionBasciFsm
				state("setup") { //this:State
					action { //it:State
						CommUtils.outblack("$name |	setup")
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
						updateResourceRep(maxw.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t05",targetState="requestEvaluation",cond=whenRequest("storeFood"))
				}	 
				state("requestEvaluation") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storeFood(_)"), Term.createTerm("storeFood(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								        		try{
								        			requestWeightToStore = (payloadArg(1).toFloat())
								        		}catch(e : Exception){
								answer("storeFood", "storeRejected", "storeRejected(_)"   )  
								
								        		}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="acceptRequest", cond=doswitchGuarded({ coldRoom.canStore(requestWeightToStore)  
					}) )
					transition( edgeName="goto",targetState="rejectRequest", cond=doswitchGuarded({! ( coldRoom.canStore(requestWeightToStore)  
					) }) )
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
				state("acceptRequest") { //this:State
					action { //it:State
						
						    	TICKET = coldstorageservice.Generate.TOKEN
						    	TICKETID = TOKEN.TICKETID	
						answer("storeFood", "storeAccepted", "storeAccepted(TICKET)"   )  
						request("pickup", "pickup(TICKETID)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t06",targetState="dropout",cond=whenReply("chargeTaken"))
				}	 
				state("dropout") { //this:State
					action { //it:State
						forward("dropout", "dropout(_)" ,"transporttrolley" ) 
						forward("backhome", "backhome(_)" ,"transporttrolley" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
			}
		}
}

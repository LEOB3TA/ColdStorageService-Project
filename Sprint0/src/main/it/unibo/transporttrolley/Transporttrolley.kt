/* Generated by AN DISI Unibo */ 
package main.it.unibo.transporttrolley

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
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
		        val tTstate = transporttrolley.state.TransportTrolleyState(transporttrolley.state.CurrStateTrolley.IDLE)
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("$name	|	setup")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						updateResourceRep(tTstate.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t07",targetState="pickingup",cond=whenRequest("pickup"))
				}	 
				state("pickingup") { //this:State
					action { //it:State
						
									tTstate.updateTTState(transporttrolley.state.CurrStateTrolley.PICKINGUP)
						updateResourceRep(tTstate.toJsonString() 
						)
						answer("pickup", "chargeTaken", "chargeTaken(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t08",targetState="droppingout",cond=whenDispatch("dropout"))
				}	 
				state("droppingout") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("dropout(_)"), Term.createTerm("dropout(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 tTstate.updateTTState(transporttrolley.state.CurrStateTrolley.DROPPINGOUT)  
								updateResourceRep(tTstate.toJsonString() 
								)
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="backhome",cond=whenDispatch("backhome"))
					transition(edgeName="t010",targetState="pickingup",cond=whenRequest("pickup"))
				}	 
				state("backhome") { //this:State
					action { //it:State
						
									tTstate.updateTTState(transporttrolley.state.CurrStateTrolley.MOVING)
						updateResourceRep(tTstate.toJsonString() 
						)
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

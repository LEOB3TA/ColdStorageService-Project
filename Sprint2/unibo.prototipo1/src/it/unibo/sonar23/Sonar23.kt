/* Generated by AN DISI Unibo */ 
package it.unibo.sonar23

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonar23 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						var Appl = sysUtil.getActor("transporttrolley") != null 
						CommUtils.outblack("sonar | start with appl: $Appl")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						updateResourceRep( "Sonar waiting" 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t026",targetState="handlesonardata",cond=whenEvent("sonardata"))
					transition(edgeName="t027",targetState="handleobstacle",cond=whenEvent("obstacle"))
				}	 
				state("handlesonardata") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						updateResourceRep( "sonar23 handles $currentMsg"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleobstacle") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("obstacle(D)"), Term.createTerm("obstacle(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outmagenta("$name handleobstacle ALARM ${payloadArg(0)}")
								emit("alarm", "alarm(obstacle)" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="sonar23", cond=doswitchGuarded({ Appl == true  
					}) )
					transition( edgeName="goto",targetState="work", cond=doswitchGuarded({! ( Appl == true  
					) }) )
				}	 
				state("sonar23") { //this:State
					action { //it:State
						CommUtils.outblack("$name BYE")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}
/* Generated by AN DISI Unibo */ 
package it.unibo.controller23

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Controller23 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 val DLIMIT = 30  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outmagenta("${name} STARTS - Activates the sonar")
						forward("sonaractivate", "info($DLIMIT)" ,"sonar23" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t038",targetState="blinkled",cond=whenEvent("robotmoving"))
					transition(edgeName="t039",targetState="stayoff",cond=whenEvent("robotathome"))
					transition(edgeName="t040",targetState="doBusinessWork",cond=whenEvent("sonardata"))
				}	 
				state("doBusinessWork") { //this:State
					action { //it:State
						CommUtils.outmagenta("${name} BUSINESS WORK")
						if( checkMsgContent( Term.createTerm("distance(D)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var D = payloadArg(0).toInt()  
								CommUtils.outred("$D")
								if(  D <= DLIMIT  
								 ){forward("ledCmd", "ledCmd(ON)" ,"ledqakactor" ) 
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t041",targetState="blinkled",cond=whenEvent("robotmoving"))
					transition(edgeName="t042",targetState="stayoff",cond=whenEvent("robotathome"))
					transition(edgeName="t043",targetState="doBusinessWork",cond=whenEvent("sonardata"))
				}	 
				state("stayoff") { //this:State
					action { //it:State
						forward("ledCmd", "ledCmd(OFF)" ,"ledqakactor" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t044",targetState="blinkled",cond=whenEvent("robotmoving"))
				}	 
				state("blinkled") { //this:State
					action { //it:State
						forward("ledCmd", "ledCmd(BLINK)" ,"ledqakactor" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t045",targetState="blinkled",cond=whenEvent("robotmoving"))
					transition(edgeName="t046",targetState="stayoff",cond=whenEvent("robotathome"))
					transition(edgeName="t047",targetState="doBusinessWork",cond=whenEvent("sonardata"))
				}	 
			}
		}
}

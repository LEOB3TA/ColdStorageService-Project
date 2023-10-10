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
						CommUtils.outblack("${name} STARTS - Activates the sonar")
						forward("sonaractivate", "info($DLIMIT)" ,"sonar23" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t040",targetState="doBusinessWork",cond=whenEvent("sonardistance"))
				}	 
				state("doBusinessWork") { //this:State
					action { //it:State
						CommUtils.outblue("business work controller")
						if( checkMsgContent( Term.createTerm("distance(D)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var D = payloadArg(0).toInt()  
								CommUtils.outred("$D")
								if(  D <= DLIMIT  
								 ){forward("ledCmd", "ledCmd(ON)" ,"ledqakactor" ) 
								}
								else
								 {forward("ledCmd", "ledCmd(OFF)" ,"ledqakactor" ) 
								 }
						}
						if( checkMsgContent( Term.createTerm("robotmoving(_)"), Term.createTerm("robotmoving(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("ledCmd", "ledCmd(BLINK)" ,"ledqakactor" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t041",targetState="doBusinessWork",cond=whenEvent("sonardistance"))
				}	 
			}
		}
}

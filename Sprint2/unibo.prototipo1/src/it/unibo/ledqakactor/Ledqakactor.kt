/* Generated by AN DISI Unibo */ 
package it.unibo.ledqakactor

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Ledqakactor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("${name} STARTS")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t036",targetState="doCmd",cond=whenDispatch("ledCmd"))
				}	 
				state("doCmd") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("ledCmd(CMD)"), Term.createTerm("ledCmd(CMD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var Cmd = payloadArg(0)  
								if(  Cmd=="ON"  
								 ){CommUtils.outmagenta("${name} - on")
								}
								if(  Cmd=="OFF"  
								 ){CommUtils.outmagenta("${name} - off")
								}
								if(  Cmd=="BLINK"  
								 ){CommUtils.outmagenta("${name} - blink")
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t037",targetState="doCmd",cond=whenDispatch("ledCmd"))
				}	 
			}
		}
}

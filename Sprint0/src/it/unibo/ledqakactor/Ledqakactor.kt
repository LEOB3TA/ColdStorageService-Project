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
					 transition(edgeName="t00",targetState="doCmd",cond=whenDispatch("ledCmd"))
				}	 
				state("doCmd") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ledCmd(ONOFFBLINK)"), Term.createTerm("ledCmd(V)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var Cmd = payloadArg(0)  
								if(  Cmd=="on"  
								 ){CommUtils.outmagenta("${name} - on")
								}
								if(  Cmd=="off"  
								 ){CommUtils.outmagenta("${name} - off")
								}
								else
								 {CommUtils.outmagenta("${name} - blink")
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t01",targetState="doCmd",cond=whenDispatch("ledCmd"))
				}	 
			}
		}
}

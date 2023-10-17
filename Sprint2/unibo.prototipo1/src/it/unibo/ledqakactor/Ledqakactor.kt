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
		
				val ledState = state.LedState()
				ledState.setState(state.LState.OFF)
				var current=ledState.getCurrState()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						updateResourceRep(ledState.toJsonString() 
						)
						CommUtils.outblack("${name} STARTS")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t027",targetState="doCmd",cond=whenDispatch("ledCmd"))
				}	 
				state("doCmd") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ledCmd(CMD)"), Term.createTerm("ledCmd(CMD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var Cmd = payloadArg(0)  
								if(  Cmd=="ON"  
								 ){ 
													ledState.setState(state.LState.ON)
													current = ledState.getCurrState()
								updateResourceRep(ledState.toJsonString() 
								)
								CommUtils.outmagenta("${name} - $current")
								}
								if(  Cmd=="OFF"  
								 ){ 
													ledState.setState(state.LState.OFF)
													current = ledState.getCurrState()	
								updateResourceRep(ledState.toJsonString() 
								)
								CommUtils.outmagenta("${name} - $current")
								}
								if(  Cmd=="BLINK"  
								 ){ 
													ledState.setState(state.LState.BLINKS)
													current = ledState.getCurrState()
								updateResourceRep(ledState.toJsonString() 
								)
								CommUtils.outmagenta("${name} - $current")
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t028",targetState="doCmd",cond=whenDispatch("ledCmd"))
				}	 
			}
		}
}

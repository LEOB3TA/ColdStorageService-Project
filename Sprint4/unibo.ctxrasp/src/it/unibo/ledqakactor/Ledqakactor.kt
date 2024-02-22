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
				var current = ledState.getCurrState()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("${name} STARTS")
						resources.ledSupport.create() 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t01",targetState="doCmd",cond=whenDispatch("ledCmd"))
				}	 
				state("doCmd") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ledCmd(CMD)"), Term.createTerm("ledCmd(CMD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var Cmd = payloadArg(0)  
								if(  Cmd=="ON"  
								 ){
													ledState.setState(state.LState.ON)
													resources.ledSupport.on()
								updateResourceRep(ledState.toJsonString() 
								)
								}
								if(  Cmd=="OFF"  
								 ){
													ledState.setState(state.LState.OFF)
													resources.ledSupport.off()
								updateResourceRep(ledState.toJsonString() 
								)
								}
								if(  Cmd=="BLINK"  
								 ){
													ledState.setState(state.LState.BLINKS)
													resources.ledSupport.blink()
								updateResourceRep(ledState.toJsonString() 
								)
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="doCmd",cond=whenDispatch("ledCmd"))
				}	 
			}
		}
}
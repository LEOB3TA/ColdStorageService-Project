/* Generated by AN DISI Unibo */ 
package it.unibo.stateobservercontroller

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023
	
class Stateobservercontroller ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
			var state = ""
			var pos= ""
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CoapObserverSupport(myself, "192.168.178.64","8099","ctxcoldstorageservice","transporttrolley")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t029",targetState="upds",cond=whenDispatch("coapUpdate"))
				}	 
				state("upds") { //this:State
					action { //it:State
						
									state = "${currentMsg.toString().substringAfter("currState\":\"").substringBefore("\"")}"
									pos = "${currentMsg.toString().substringAfter("currPosition\":\"").substringBefore("\"")}"
						
									when{
										pos == "HOME" ->  
						forward("ledCmd", "ledCmd(OFF)" ,"ledqakactor" ) 
						state == "PICKINGUP" || state == "MOVINGTOPORT" || state == "MOVINGTOHOME" -> 
						forward("ledCmd", "ledCmd(BLINK)" ,"ledqakactor" ) 
						state == "STOPPED" -> 
						forward("ledCmd", "ledCmd(ON)" ,"ledqakactor" ) 
						} 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t030",targetState="upds",cond=whenDispatch("coapUpdate"))
				}	 
			}
		}
}

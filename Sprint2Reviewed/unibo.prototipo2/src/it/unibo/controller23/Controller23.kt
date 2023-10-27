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
		 
			val DLIMIT = 30 //valore casuale
			var state = ""	
			var pos= ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CoapObserverSupport(myself, "localhost","8099","ctxprototipo2","transporttrolley")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t040",targetState="doBusinessWork",cond=whenEvent("sonardata"))
					transition(edgeName="t041",targetState="upds",cond=whenDispatch("coapUpdate"))
				}	 
				state("upds") { //this:State
					action { //it:State
						
									state = "${currentMsg.toString().substringAfter("currState\":\"").substringBefore("\"")}"
									pos = "${currentMsg.toString().substringAfter("currPosition\":\"").substringBefore("\"")}"
						
									when{
										pos == "HOME" ->  
						forward("ledCmd", "ledCmd(ON)" ,"ledqakactor" ) 
						state == "MOVINGTOPORT" || state == "MOVINGTOHOME" -> 
						forward("ledCmd", "ledCmd(BLINK)" ,"ledqakactor" ) 
						state == "STOPPED" -> 
						forward("ledCmd", "ledCmd(OFF)" ,"ledqakactor" ) 
						} 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t042",targetState="doBusinessWork",cond=whenEvent("sonardata"))
					transition(edgeName="t043",targetState="upds",cond=whenDispatch("coapUpdate"))
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
					 transition(edgeName="t044",targetState="upds",cond=whenDispatch("coapUpdate"))
					transition(edgeName="t045",targetState="doBusinessWork",cond=whenEvent("sonardata"))
				}	 
			}
		}
}

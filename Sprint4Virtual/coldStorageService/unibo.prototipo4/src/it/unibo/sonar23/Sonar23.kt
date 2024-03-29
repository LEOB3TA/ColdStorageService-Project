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
		var DLIMIT = 30
				var D = 0
				var handled=false
			var Appl = sysUtil.getActor("transporttrolley") != null  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("sonar | start with appl: $Appl")
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
					 transition(edgeName="t00",targetState="handlesonardata",cond=whenEvent("sonardata"))
				}	 
				state("handlesonardata") { //this:State
					action { //it:State
						updateResourceRep( "sonar23 handles $currentMsg"  
						)
						if( checkMsgContent( Term.createTerm("distance(D)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								D = payloadArg(0).toInt() 
								if( D<DLIMIT && handled==false 
								 ){handled=true 
								CommUtils.outmagenta("$name handleobstacle STOP ${payloadArg(0)}")
								emit("stop", "stop(_)" ) 
								}
								if( D>DLIMIT+5 && handled==true 
								 ){handled=false 
								CommUtils.outmagenta("$name sonardata RESUME ${payloadArg(0)}")
								emit("resume", "resume(_)" ) 
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}

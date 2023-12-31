/* Generated by AN DISI Unibo */ 
package it.unibo.guicontroller

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Guicontroller ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var stato = state.GuiState()
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						CommUtils.outgreen("$name | 	started")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="updatestorage",cond=whenDispatch("updateS"))
					transition(edgeName="t01",targetState="updaterejected",cond=whenDispatch("updateR"))
					transition(edgeName="t02",targetState="updateposition",cond=whenReply("robotstate"))
					transition(edgeName="t03",targetState="updategui",cond=whenDispatch("getData"))
				}	 
				state("updatestorage") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("updateS(W)"), Term.createTerm("updateS(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val weight = payloadArg(0).toDouble() // it should be already double
								CommUtils.outblack("$name |	aggiunta peso pari a $weight")
								
											// update state	
											stato.setCurrW(weight)
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="updategui", cond=doswitch() )
				}	 
				state("updaterejected") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("updateR(_)"), Term.createTerm("updateR(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblack("$name |	rejected")
								
												stato.setRejected()	
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="updategui", cond=doswitch() )
				}	 
				state("updateposition") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("robotstate(POS,DIR)"), Term.createTerm("robotstate(Pos,Dir)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val P = payloadArg(0).toInt()
												val D = payloadArg(1).toInt()
												var X = 0
								 				var Y = 0
								 				val regex = """pos\((\d+),(\d+)\)""".toRegex()
												val matchResult = regex.find(P.toString())
												if (matchResult != null) {
												    val (yStr, xStr) = matchResult.destructured
												    X = xStr.toInt()
												    Y = yStr.toInt()
												}
												stato.setTTP(intArrayOf(X,Y))
												//var prova = stato.getTTP()
												//for (i in 0..1) println( prova[i])	
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="updategui", cond=doswitch() )
				}	 
				state("updategui") { //this:State
					action { //it:State
						CommUtils.outblack("$name |	new update for GUI")
						updateResourceRep(stato.toString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
			}
		}
}

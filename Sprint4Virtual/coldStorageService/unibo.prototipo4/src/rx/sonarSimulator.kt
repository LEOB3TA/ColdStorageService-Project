package rx

import it.unibo.kactor.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.utils.CommUtils
import kotlin.random.Random

/*
-------------------------------------------------------------------------------------------------
sonarSimulator.kt
-------------------------------------------------------------------------------------------------
 */

class sonarSimulator ( name : String ) : ActorBasic( name ) {
	init{
		//autostart

		runBlocking{  autoMsg("simulatorstart","do") }
	}
//@kotlinx.coroutines.ObsoleteCoroutinesApi

    override suspend fun actorBody(msg : IApplMessage){
  		println("$tt $name | received  $msg "  )
		//delay(10000)
		if( msg.msgId() == "simulatorstart") startDataReadSimulation(   )
     }
  	
//@kotlinx.coroutines.ObsoleteCoroutinesApi

	suspend fun startDataReadSimulation(    ){
		/*val data = sequence<Int>{
			var v0 = 80
			yield(v0)
			while(true){
				v0 = v0 - 5
				yield( v0 )
			}
		}*/
		delay(20000)
		var i = 0
			while( i < 30 ){
 	 			//val m1 = "distance( ${data.elementAt(i)} )"
				val m1= "distance( ${Random.nextInt(0,80)} )"
				i++
 				val event = CommUtils.buildEvent( name,"sonardistance", m1)
  				//emitLocalStreamEvent( event ) //ORIGINAL FROM NATALI
 				println("$tt $name | generates $event")
 				emit(event)  //APPROPRIATE ONLY IF NOT INCLUDED IN A PIPE
 				delay( 1500 )
  			}
			emit(CommUtils.buildEvent( name,"sonardistance", "distance(35)"))
			terminate()
	}

} 

//@kotlinx.coroutines.ObsoleteCoroutinesApi
//
//fun main() = runBlocking{
// //	val startMsg = MsgUtil.buildDispatch("main","start","start","datasimulator")
//	val consumer  = dataConsumer("dataconsumer")
//	val simulator = sonarSimulator( "datasimulator" )
//	val filter    = dataFilter("datafilter", consumer)
//	val logger    = dataLogger("logger")
//	simulator.subscribe( logger ).subscribe( filter ).subscribe( consumer ) 
//	MsgUtil.sendMsg("start","start",simulator)
//	simulator.waitTermination()
// } 
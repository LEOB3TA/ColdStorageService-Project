package rx

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.utils.CommUtils
import kotlin.random.Random

class testSonar( name : String): ActorBasic(name) {
    init{
        //autostart

        runBlocking{  autoMsg("simulatorstart","do") }
    }
    override suspend fun actorBody(msg : IApplMessage){
        println("$tt $name | received  $msg "  )
        delay(3000)
        if( msg.msgId() == "simulatorstart") startDataReadSimulation(   )
    }
    suspend fun startDataReadSimulation(    ){
        delay( 1500 )
        val m1 = "distance( 19 )"
        val event = CommUtils.buildEvent( name,"sonardistance", m1)
        println("$tt $name | generates $event")
        emit(event)  //APPROPRIATE ONLY IF NOT INCLUDED IN A PIPE
        delay( 1500 )
        emit(event)  //APPROPRIATE ONLY IF NOT INCLUDED IN A PIPE
        delay( 1500 )
        emit(CommUtils.buildEvent( name,"sonardistance", "distance(45)"))
        terminate()
        emit(CommUtils.buildEvent( name,"sonardistance", "distance(10000)"))
        terminate()
    }

}

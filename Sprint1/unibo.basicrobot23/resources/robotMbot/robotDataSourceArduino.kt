package robotMbot
/*
 -------------------------------------------------------------------------------------------------
 Reads data from the serial connection to Arduino
 For each data value V, it emitLocalStreamEvent sonarRobot:sonar(V)
 -------------------------------------------------------------------------------------------------
 */
import it.unibo.kactor.ActorBasic
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.interfaces.Interaction
import unibo.basicomm23.utils.CommUtils


class robotDataSourceArduino( name : String, val owner : ActorBasic ,
					val conn : Interaction
) : ActorBasic(name, owner.scope){
	
companion object {
	val eventId = "sonarRobot"
}		
	init{
 		println("   	%%% $name |  starts conn=$conn")	 
 	}

	override suspend fun actorBody(msg: IApplMessage) {
        //println("   	%%% $name |  handles msg= $msg  ")
		//val vStr  = (Term.createTerm( msg.msgContent()) as Struct).getArg(0).toString()
		//println("   	%%% $name |  handles msg= $msg  vStr=$vStr")
		elabData(   )
	}

	suspend fun elabData(  ){
		var counter   = 0
		println("   	%%% $name |  elabData ... on $conn")
		//GlobalScope.launch{	//to allow message handling
			while (true) {
			try {
 				var curDataFromArduino = conn.receiveMsg()
				//println("   	%%% $name | elabData: $curDataFromArduino  }")
				if( curDataFromArduino != null ) {
					var v = curDataFromArduino.toDouble()
					var dataSonar = v.toInt()
                    println("   	%%% $name | elabData: $dataSonar | ${counter++}")

					var event = CommUtils.buildEvent(name, "sonarRobot", "sonar( $dataSonar )")
					//println("   	%%% $name | robotDataSourceArduino event: ${ event } owner=${owner.name}"   );
					//owner.emit(  event )
					emitLocalStreamEvent(event)
					//delay(100)  //WARNING: if included, it does not change values
				}
			} catch ( e : Exception) {
 				println("   	%%% $name | robotDataSourceArduino | ERROR $e   ")
				//break
            }
 			//delay(100)  //WARNING: if included, it does read correct values
			}//while
		//}//launch
 	}
	
 
}
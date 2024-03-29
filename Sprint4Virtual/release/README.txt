Il sonar simulato viene avviato dopo 20 secondi dall'avvio del sistema e per 30 volte restituisce una distanza tra 0 ed 80.
In seguito conclude con la distanza 35 in modo da far partire di nuovo il robot.
Il limite per fermare il robot è 30, nello sprint4 con il robot era 5.
Viene riportato per completezza parte del codice:
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
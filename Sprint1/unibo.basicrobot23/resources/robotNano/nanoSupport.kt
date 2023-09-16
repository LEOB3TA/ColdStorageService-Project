package robotNano

import it.unibo.kactor.ActorBasic
import java.io.OutputStreamWriter

object nanoSupport {
	lateinit var writer : OutputStreamWriter

	fun create( owner : ActorBasic ){		
		//val p = Runtime.getRuntime().exec("sudo ./Motors")
		val p = Runtime.getRuntime().exec("sudo python3 Motors.py")
		writer = OutputStreamWriter(  p.outputStream)
		println("nanoSupport  | CREATED with writer=$writer")
 	}

	fun move( msg : String="" ){
		println("nanoSupport  | WRITE $msg with $writer")
		writer.write( msg + "\n")
		writer.flush()
	}
	
	fun terminate(){
		
	}

}
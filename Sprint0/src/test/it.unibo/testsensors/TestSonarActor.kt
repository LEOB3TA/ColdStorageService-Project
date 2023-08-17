package test.it.unibo.testsensors

import unibo.basicomm23.interfaces.Interaction2021
import it.unibo.kactor.QakContext
import junit.framework.Assert.assertEquals
import main.it.unibo.ctxledqak.main
import org.junit.Before
import org.junit.Test
import resources.LedState
import resources.SonarState
import test.it.unibo.coapobs.TypedCoapTestObserver
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue

class TestSonarActor{
    private lateinit var conn: Interaction2021
    private lateinit var obs : TypedCoapTestObserver<SonarState>
    private var setupOk = false

    @Before
    fun setup(){
        println("TestSonarQakActor	|	initTest...")
        if(!setupOk){
            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()

            var ledActor = QakContext.getActor("sonar23")
            while(ledActor == null){
                println("TestSonarQakActor	|	waiting for application start...")
                CommUtils.delay(200)
                ledActor = QakContext.getActor("sonar23")
            }
            try {
                conn = TcpClientSupport.connect("192.168.1.xxx", 8086, 5)
            } catch (e: Exception) {
                println("TestSonarQakActor	|	connection failed...")
            }
            startObs("192.168.1.xxx:8086")
            obs.getNext()
            setupOk = true
        }
        else{
            obs.clearHistory()
        }
    }

    fun startObs(addr : String){
        val setupOk = ArrayBlockingQueue<Boolean>(1)
        object : Thread(){
            override fun run(){
                obs = TypedCoapTestObserver { SonarState.fromJsonString(it) }
                var ctx  = "ctxrasp"
                var act  = "sonar23"
                var path = "$ctx/$act"
                val coapConn = CoapConnection(addr, path)
                coapConn.observeResource(obs)
                try {
                    setupOk.put(true)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        setupOk.take()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testActivate(){
        println("TestSonarQakActor  |   testActivate...")
        val turnOn = "msg(Activate, dispatch, testunit, sonar23, Activate, 1)"
        conn.forward(turnOn)

        assertEquals(obs.getNext().getTTState().toString(), "Activate")
    }
}
package test.it.unibo

import it.unibo.ctxprototipo2.main
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import state.LedState
import state.TransportTrolleyState
import test.it.unibo.coapobs.TypedCoapTestObserver
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue

class TestPrototipo2{
    companion object {
        private var setup = false
        private lateinit var connTT: Interaction2021
        private lateinit var connRobot: Interaction2021
        private lateinit var obsTT: TypedCoapTestObserver<TransportTrolleyState>
        private lateinit var obsLS: TypedCoapTestObserver<LedState>
    }
    @Before
    fun setUp() {
        if (!setup) {
            CommUtils.outmagenta("TestTrasportTrolley  |  setup...")
            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()
            var cSS = sysUtil.getActor("coldstorageservice")
            while (cSS == null) {
                CommUtils.outmagenta("TestColdStorageService  |  waiting for coldstorageservice...")
                CommUtils.delay(200)
                cSS = QakContext.getActor("coldstorageservice")
            }
            var tT = sysUtil.getActor("transporttrolley")
            while (tT == null) {
                CommUtils.outmagenta("TestColdStorageService  |  waiting for transporttrolley...")
                CommUtils.delay(200)
                tT = QakContext.getActor("transporttrolley")
            }
            try {
                connTT = TcpClientSupport.connect("localhost", 8099, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestTraposrtTrolley  |  TCP connection failed...")
            }
            try {
                connRobot = TcpClientSupport.connect("127.0.0.1", 8020, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestTraposrtTrolley  |  TCP connection failed...")
            }
            startObs("localhost:8099")
            println(obsTT.getNext().toString())
            println(obsLS.getNext().toString())
        } else {
            val goHome = "msg(moverobot, request, testunit, basicrobot, moverobot(0,0) ,1)"
            var rep = ""
            try {
                rep = connRobot.request(goHome)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestTrasportTrolley	|	 some err in request: $e")
            }
            Thread.sleep(7000)
            obsTT.clearHistory()
            obsLS.clearHistory()

        }
    }

    fun startObs(addr: String?) {
        val setupOkA = ArrayBlockingQueue<Boolean>(1)
        object : Thread() {
            override fun run() {
                var ctx = "ctxprototipo1"
                var actor = "transporttrolley"
                var path = "$ctx/$actor"
                var coapConn = CoapConnection(addr, path)
                obsTT = TypedCoapTestObserver {
                    TransportTrolleyState.fromJsonString(it)
                }
                coapConn.observeResource(obsTT)
                 ctx = "ctxprototipo1"
                 actor = "ledqakactor"
                 path = "$ctx/$actor"
                 coapConn = CoapConnection(addr, path)
                obsLS = TypedCoapTestObserver {
                    LedState.fromJsonString(it)
                }
                coapConn.observeResource(obsLS)
                try {
                    setupOkA.put(true)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        setup=setupOkA.take()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testLedSonar(){
        CommUtils.outmagenta("Test led & sonar | Led state & sonar events ...")
        val pickup = "msg(pickup, request, testunit, transporttrolley, pickup(_) ,1)"
        var rep = ""
        var sonarE =""
        val sonarIng = "msg(sonardistance, event, testunit, transporttrolley, distance(13), 1)"
        /*println(obsTT.currState.toString().substringAfterLast(":").substring(1,5))
        println(obsLS.currState.toString().substringAfterLast(":").substring(1,4))*/
        Assert.assertEquals("OFF", obsLS.currState.toString().substringAfterLast(":").substring(1,4))

        GlobalScope.launch {
            try {
                rep = connTT.request(pickup)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService  |   some err in request: $e")
            }
        }
        var newState= obsTT.getNext()
        var newStateLed = obsLS.getNext()
        Assert.assertEquals("INDOOR", newState.getCurrPosition().toString())
        Assert.assertEquals("PICKINGUP", newState.getCurrState().toString())
        Assert.assertEquals("BLINKS", newStateLed.getCurrState().toString())

        try{
            connTT.forward(sonarIng)
        }catch (e: Exception) {
            CommUtils.outmagenta("Sonardata error	|	 some err in request: $e")
        }
        //println("DEBUGSTATES: ${obsTT.currState.toString().substringAfter(":").substring(1,8)}")
        Assert.assertNotSame("STOPPED", obsTT.currState.toString().substringAfter(":").substring(1,10))
        newState= obsTT.getNext()
        newStateLed = obsLS.getNext()
        newStateLed = obsLS.getNext()
        Assert.assertEquals("INDOOR", newState.getCurrPosition().toString())
        Assert.assertEquals("PICKINGUP", newState.getCurrState().toString())
        Assert.assertEquals("BLINKS", newStateLed.getCurrState().toString())

        newState = obsTT.getNext()
        newStateLed = obsLS.getNext()
        Assert.assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        Assert.assertEquals("MOVINGTOPORT", newState.getCurrState().toString())
        Assert.assertEquals("BLINKS", newStateLed.getCurrState().toString())
        sonarE= "msg(sonardistance, event, testunit, transporttrolley, distance(19), 1)"
        try{
            connTT.forward(sonarE)
            connTT.forward(sonarIng)
        }catch (e: Exception) {
            CommUtils.outmagenta("Sonardata error	|	 some err in request: $e")
        }
        println("ignored because before 2 seconds")

        newState = obsTT.getNext()
        newStateLed = obsLS.getNext()
        Assert.assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        Assert.assertEquals("STOPPED", newState.getCurrState().toString())
        Assert.assertEquals("ON", newStateLed.getCurrState().toString())
        Thread.sleep(3000)//per far vedere che si ferma e riprende, altrimenti è troppo veloce
        sonarE = "msg(sonardistance, event, testunit, transporttrolley, distance(42), 1)"
        try{
            connTT.forward(sonarE)
        }catch (e: Exception) {
            CommUtils.outmagenta("Sonardata error	|	 some err in request: $e")
        }

        newState = obsTT.getNext()

        //println("DEBUGSTATES: ${newState}, ${newStateLed}, ${obsLS.currState.toString()}")

        Assert.assertEquals("PORT", newState.getCurrPosition().toString())
        Assert.assertEquals("DROPPINGOUT", newState.getCurrState().toString())


        newState = obsTT.getNext()
        //println("DEBUGSTATES: ${newState}, ${newStateLed}, ${obsLS.currState.toString()}")

        Assert.assertEquals("PORT", newState.getCurrPosition().toString())
        Assert.assertEquals("IDLE", newState.getCurrState().toString())


        newState = obsTT.getNext()
        //println("DEBUGSTATES: ${newState}, ${newStateLed}, ${obsLS.currState.toString()}")
        Assert.assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        Assert.assertEquals("MOVINGTOHOME", newState.getCurrState().toString())

        try{
            connTT.forward(sonarE)
        }catch (e: Exception) {
            CommUtils.outmagenta("Sonardata error	|	 some err in request: $e")
        }
        //println("DEBUGSTATES: ${obsTT.currState.toString().substringAfter(":").substring(1,8)}")
        Assert.assertNotSame("STOPPED", obsTT.currState.toString().substringAfter(":").substring(1,4))

        newState = obsTT.getNext()
        //newStateLed = obsLS.getNext()
        Assert.assertEquals("HOME", newState.getCurrPosition().toString())
        Assert.assertEquals("IDLE", newState.getCurrState().toString())



        Thread.sleep(1000)
        Assert.assertEquals("OFF",obsLS.currState.toString().substringAfterLast(":").substring(1,4))
        //println("DEBUGSTATES: ${newState}, ${newStateLed}, ${obsLS.currState.toString()}")

        Assert.assertTrue(rep.contains("pickupdone"))

    }

}
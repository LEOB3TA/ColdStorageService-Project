package test.it.unibo

import it.unibo.ctxprototipo1.main
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import state.TransportTrolleyState
import test.it.unibo.coapobs.TypedCoapTestObserver
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue

class TestPrototipo1{
    companion object {
        private var setup = false
        private lateinit var connTT: Interaction2021
        private lateinit var connRobot: Interaction2021
        private lateinit var obsTT: TypedCoapTestObserver<TransportTrolleyState>
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

        }
    }

    fun startObs(addr: String?) {
        val setupOkA = ArrayBlockingQueue<Boolean>(1)
        object : Thread() {
            override fun run() {
                val ctx = "ctxcoldstorageservice"
                val actor = "transporttrolley"
                val path = "$ctx/$actor"
                val coapConn = CoapConnection(addr, path)
                obsTT = TypedCoapTestObserver {
                    TransportTrolleyState.fromJsonString(it)
                }
                coapConn.observeResource(obsTT)
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
    fun testLed(){
        CommUtils.outmagenta("Test led | off at HOME ...")
        var newState = obsTT.getNext()
        val pickup = "msg(pickup, request, testunit, transporttrolley, pickup(_) ,1)"
        var rep = ""

        Assert.assertEquals("HOME", newState.getCurrPosition().toString())
        Assert.assertEquals("OFF", newState.getCurrLed().toString())

        GlobalScope.launch {
            try {
                rep = connTT.request(pickup)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService  |   some err in request: $e")
            }
        }
        newState= obsTT.getNext()
        Assert.assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        Assert.assertEquals("BLINKS", newState.getCurrLed().toString())
        newState = obsTT.getNext()
        println(newState.toString())
        Assert.assertEquals("INDOOR", newState.getCurrPosition().toString())
        Assert.assertEquals("PICKINGUP", newState.getCurrState().toString())
        newState = obsTT.getNext()
        println(newState.toString())
        Assert.assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        Assert.assertEquals("MOVING", newState.getCurrState().toString())
        Thread.sleep(7000)
        Assert.assertTrue(rep.contains("pickupdone"))
    }
}
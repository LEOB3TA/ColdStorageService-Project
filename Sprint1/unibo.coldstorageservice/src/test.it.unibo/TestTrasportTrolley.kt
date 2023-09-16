package test.it.unibo

import it.unibo.ctxcoldstorageservice.main
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
import java.lang.Thread.sleep
import java.util.concurrent.ArrayBlockingQueue

class TestTrasportTrolley {
    companion object {
        private var setup = false
        private lateinit var connTT: Interaction2021
        private lateinit var connRobot: Interaction2021
        private lateinit var obsTT: TypedCoapTestObserver<TransportTrolleyState>
    }

    //TODO rivedere tutte le stampe prima di ogni test
    //TODO capire come gestire la risposta in caso il robot non riuscisse a muoversi
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
            /*
            try {
                conn = TcpClientSupport.connect("localhost", 8099, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService  |  TCP connection failed...")
            }
        }*/
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
            /*var bR = sysUtil.getActor("basicrobot")
            while (bR == null) {
                CommUtils.outmagenta("TestColdStorageService  |  waiting for basicrobot...")
                CommUtils.delay(200)
                bR = QakContext.getActor("basicrobot")
            }*/
            /* try {
                 conn = TcpClientSupport.connect("127.0.0.1", 8020, 5)
             } catch (e: Exception) {
                 CommUtils.outmagenta("TestColdStorageService  |  TCP connection failed...")
             } */

            startObs("localhost:8099")
            println(obsTT.getNext().toString())
        } else {
            val goHome = "msg(moverobot, request, testunit, basicrobot, moverobot(0,0) ,1)"
            var rep=""
            try {
                rep = connRobot.request(goHome)
            }catch (e: Exception) {
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
    fun testPick() {
        CommUtils.outmagenta("TestTrasportTrolley  |  testPickup...")
        var pickup = "msg(pickup, request, testunit, transporttrolley, pickup(_) ,1)"
        var rep = ""
        GlobalScope.launch {
            try {
                rep = connTT.request(pickup)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService  |   some err in request: $e")
            }
        }
        var newState = obsTT.getNext()
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

    @Test
    @Throws(InterruptedException::class)
    fun testPickQueue() {
        CommUtils.outmagenta("TestTrasportTrolley  |  testPickupQueue...")
        val pickup = "msg(pickup, request, testunit, transporttrolley, pickup(_) ,1)"
        var rep = ""
        GlobalScope.launch {
            try {
                rep = connTT.request(pickup)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService  |   some err in request: $e")
            }
        }
        Thread.sleep(7000)
        val pickupQue = "msg(pickup, request, testunit, transporttrolley, pickup(_) ,2)"
        var rep1 = ""
        GlobalScope.launch {
            try {
                rep1 = connTT.request(pickupQue)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService  |   some err in request: $e")
            }
        }
        var newState = obsTT.getNext()
        println(newState.toString())
        Assert.assertEquals("INDOOR", newState.getCurrPosition().toString())
        Assert.assertEquals("PICKINGUP", newState.getCurrState().toString())
        newState = obsTT.getNext()
        println(newState.toString())
        Assert.assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        Assert.assertEquals("MOVING", newState.getCurrState().toString())
        Thread.sleep(7000)
        Assert.assertTrue(rep.contains("pickupdone"))
        Thread.sleep(7000)
        Assert.assertTrue(rep1.contains("pickupdone"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testMoveToHome() {
        CommUtils.outmagenta("TestTrasportTrolley  |  testMoveToIndoor...")
        var pickup = "msg(pickup, request, testunit, transporttrolley, pickup(_) ,1)"
        var repp = ""
        GlobalScope.launch {
            try {
                repp = connTT.request(pickup)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService  |   some err in request: $e")
            }
        }
        Thread.sleep(12000)
        var newState = obsTT.getNext()
        println(newState.toString())
        var move = "msg(moverobot, request, testunit, basicrobot, moverobot(4, 3),1)"
        var rep = ""
        try {
            rep = connRobot.request(move)
        } catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }
        newState = obsTT.currentTypedState!!
        Thread.sleep(5000)
        Assert.assertEquals("IDLE",obsTT.currentTypedState!!.getCurrState().toString())
        Assert.assertEquals("HOME", obsTT.currentTypedState!!.getCurrPosition().toString())
    }

}
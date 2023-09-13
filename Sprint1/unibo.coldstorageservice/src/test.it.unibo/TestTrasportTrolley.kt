package test.it.unibo

import it.unibo.ctxcoldstorageservice.main
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
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

class TestTrasportTrolley {
    companion object {
        private var setup = false
        private lateinit var connTT: Interaction2021
        private lateinit var connRobot: Interaction2021
        private lateinit var obsTT: TypedCoapTestObserver<TransportTrolleyState>
    }

    //TODO rivedere tutte le stampe prima di ogni test
    @Before
    fun setUp() { //TODO rivedere tutte le varie connessioni, non possiamo avere la stessa connessione per tutti gli attori
        if (!setup) {
            CommUtils.outmagenta("TestTrasportTrolley	|	setup...")

            object : Thread() {
                override fun run() {
                    main()
                    it.unibo.ctxbasicrobot.main()
                }
            }.start()
            /*         var cSS = sysUtil.getActor("coldstorageservice")
                     while (cSS == null) {
                         CommUtils.outmagenta("TestColdStorageService	|	waiting for coldstorageservice...")
                         CommUtils.delay(200)
                         cSS = QakContext.getActor("coldstorageservice")
                     }
                 }
                 try {
                     conn = TcpClientSupport.connect("localhost", 8099, 5)
                 } catch (e: Exception) {
                     CommUtils.outmagenta("TestColdStorageService	|	TCP connection failed...")
                 }
             }*/
            var tT = sysUtil.getActor("transporttrolley")
            while (tT == null) {
                CommUtils.outmagenta("TestColdStorageService	|	waiting for transporttrolley...")
                CommUtils.delay(200)
                tT = QakContext.getActor("transporttrolley")
            }
            try {
                connTT = TcpClientSupport.connect("localhost", 8099, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestTraposrtTrolley	|	TCP connection failed...")
            }
            try {
                connRobot = TcpClientSupport.connect("127.0.0.1", 8020, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestTraposrtTrolley	|	TCP connection failed...")
            }
           /*  var bR = sysUtil.getActor("basicrobot")
             while (bR == null) {
                 CommUtils.outmagenta("TestColdStorageService	|	waiting for basicrobot...")
                 CommUtils.delay(200)
                 bR = QakContext.getActor("basicrobot")
             }*/
            /* try {
                 conn = TcpClientSupport.connect("127.0.0.1", 8020, 5)
             } catch (e: Exception) {
                 CommUtils.outmagenta("TestColdStorageService	|	TCP connection failed...")
             } */

            startObs("localhost:8099")
            println(obsTT.getNext().toString())
        } else {
            obsTT.clearHistory()
        }
    }

    fun startObs(addr: String?) {
        val setupOkA = ArrayBlockingQueue<Boolean>(1)
        object : Thread() {
            override fun run() {
                val ctx = "ctxcoldstorageservice"
                val actor = "trasporttrolley"
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
        CommUtils.outmagenta("testPickup")
        var pickup = "msg(pickup, request, testunit, transporttrolley, pickup(_) ,1)"
        var rep = ""
        try {
            rep = connTT.request(pickup)
        } catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }
        var newState = obsTT.getNext()
        Assert.assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        Assert.assertEquals("MOVING", newState.getCurrState().toString())
        newState = obsTT.getNext()
        Assert.assertEquals("INDOOR", newState.getCurrPosition().toString())
        Assert.assertEquals("PICKINGUP", newState.getCurrState().toString())
        Assert.assertTrue(rep.contains("pickupdone"))
        newState = obsTT.getNext()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testEngage() {
        CommUtils.outmagenta("Test Engage")
        var eng = "msg(engage, request, testunit, transporttrolley, engage(transporttrolley, 330),1)"
        var rep = ""
        try {
            rep = connRobot.request(eng)
        } catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        Assert.assertTrue(rep.contains("engagedone"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testEngageF() {
        CommUtils.outmagenta("Test EngageF")
        var eng = "msg(engage, request, testunit, transporttrolley, engage(TT, 330),1)"
        var rep = ""
        try {
            rep = connRobot.request(eng)
        } catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        Assert.assertTrue(rep.contains("engagerefused"))
    }



    @Test
    @Throws(InterruptedException::class)
    fun testMove() {

    }

}
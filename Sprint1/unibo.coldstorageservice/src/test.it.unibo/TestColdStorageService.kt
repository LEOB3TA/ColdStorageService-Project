package test.it.unibo

import test.it.unibo.coapobs.TypedCoapTestObserver
import it.unibo.ctxcoldstorageservice.main
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil.getActor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import state.TransportTrolleyState
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue

class TestColdStorageService {
    companion object {
        private var setup = false
        private lateinit var conn: Interaction2021
        //TODO: creare nuovo obs per CSS
        private lateinit var obs: TypedCoapTestObserver<TransportTrolleyState>

    }

    @Before
    fun setUp(){
        if(!setup){
            CommUtils.outmagenta("TestColdStorageService	|	setup...")

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()
            var cSS = getActor("coldstorageservice")
            while (cSS == null) {
                CommUtils.outmagenta("TestColdStorageService	|	waiting for coldstorageservice...")
                CommUtils.delay(200)
                cSS = QakContext.getActor("coldstorageservice")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8099, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService	|	TCP connection failed...")
            }
            var tT = getActor("transporttrolley")
            while (tT == null) {
                CommUtils.outmagenta("TestColdStorageService	|	waiting for transporttrolley...")
                CommUtils.delay(200)
                tT = QakContext.getActor("transporttrolley")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8099, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService	|	TCP connection failed...")
            }
            var bR = getActor("basicrobot")
            while (bR == null) {
                CommUtils.outmagenta("TestColdStorageService	|	waiting for basicrobot...")
                CommUtils.delay(200)
                bR = QakContext.getActor("basicrobot")
            }
            try {
                conn = TcpClientSupport.connect("127.0.0.1", 8020, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService	|	TCP connection failed...")
            }
            startObs("localhost:8099")
            obs.getNext()
            setup= true
        }else{
            obs.clearHistory()
        }
    }

    fun startObs(addr: String?) {
        val setupOk = ArrayBlockingQueue<Boolean>(1)

        object : Thread() {
            override fun run() {
                obs = TypedCoapTestObserver{
                    TransportTrolleyState.fromJsonString(it)
                }
                val ctx = "ctxcoldstorageservice"
                val actor = "coldstorageservice"
                val path = "$ctx/$actor"
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

    // Interaction CSS -> Truck
    @Test
    @Throws(InterruptedException::class)
    fun testStoreFoodSuccess(){
        CommUtils.outmagenta("TestStoreFood   |  testStoreFood")

        var asw=""
        var storeFood = "msg(storeFood, request, testunit, coldstorageservice, storeFood(10) ,1)"


        try {
            asw =conn.request(storeFood)
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        assertTrue(asw.contains("storeAccepted"))
    }
    @Test
    @Throws(InterruptedException::class)
    fun testStoreFoodFail(){
        CommUtils.outmagenta("TestStoreFood   |  testStoreFood")

        var asw=""
        var storeFood = "msg(storeFood, request, testunit, coldstorageservice, storeFood(10) ,1)"


        try {
            asw =conn.request(storeFood)
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        assertTrue(asw.contains("storeRejected"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testSendTicket(){
        CommUtils.outmagenta("TestSendTicket")
        var sendT = "msg(sendTicket, request, testunit, coldstorageservice, sendTicket(1) ,1)"
        conn.forward(sendT)
        var rep =""
        try {
            rep = conn.reply("msg(ticketValid, reply, testunit, truck, testStore(_), 1)").toString()
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        assertTrue(rep.contains("ticketValid"))

    }

    @Test
    @Throws(InterruptedException::class)
    fun testSendInvalidTicket(){
        CommUtils.outmagenta("TestSendInvalidTicket")
        var sendT = "msg(sendTicket, request, testunit, coldstorageservice, sendTicket(-1) ,1)"
        var rep=""
        try {
            rep = conn.request(sendT)
            //var rep = conn.request("msg(ticketInvalid, reply, testunit, truck, testStore(_), 1)").toString()
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }


        assertTrue(rep.contains("ticketInvalid"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDeposit(){
        CommUtils.outmagenta("TestDeposit")
        var deposit = "msg(deposit, request, testunit, coldstorageservice, deposit(_),1"
        /*conn.request(deposit)*/
        var rep=""
        try {
            rep = conn.request(deposit)
            //rep = conn.reply("msg(ticketInvalid, reply, testunit, truck, testStore(_), 1)").toString()
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        assertTrue(rep.contains("chargeTaken"))
    }

    // Interaction CSS -> TT -> basicrobot

    @Test
    @Throws(InterruptedException::class)
    fun testEngage(){
        CommUtils.outmagenta("Test Engage")
        var eng = "msg(engage, request, testunit, transporttrolley, engage(transporttrolley, 330)"
        var rep=""
        try {
            rep = conn.request(eng)
            }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        assertTrue(rep.contains("engagedone"))
    }
    @Test
    @Throws(InterruptedException::class)
    fun testEngageF(){
        CommUtils.outmagenta("Test EngageF")
        var eng = "msg(engage, request, testunit, transporttrolley, engage(TT, 330)"
        var rep=""
        try {
            rep = conn.request(eng)
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        assertTrue(rep.contains("engagerefused"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testPick(){
        CommUtils.outmagenta("testPickup")
        var pickup = "msg(pickup, request, testunit, transporttrolleycore, pickup(_) ,1)"
        var rep=""

        try {
            rep = conn.request(pickup)
        } catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }

        var newState = obs.getNext()

        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()

        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("PICKINGUP", newState.getCurrState().toString())
        assertTrue(rep.contains("pickupdone"))

        newState = obs.getNext()

    }

    @Test
    @Throws(InterruptedException::class)
    fun testMove(){

    }

}
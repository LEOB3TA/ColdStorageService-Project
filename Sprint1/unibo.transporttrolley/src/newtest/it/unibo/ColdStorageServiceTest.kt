package newtest.it.unibo

import it.unibo.ctxcoldstorageservice.main
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import it.unibo.kactor.sysUtil.getActor
import observers.basicrobotCoapObserver
import observers.coldStorageServiceCoapObserver
import observers.transportTrolleyCoapObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils

class ColdStorageServiceTest {
    companion object {
        private var setup = false
        private lateinit var conn: Interaction2021
        //private lateinit var obs: TypedCoapTestObserver<TransportTrolleyState>

    }

    @Before
    fun setUp(){
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

        coldStorageServiceCoapObserver.activate()
        transportTrolleyCoapObserver.activate()
        basicrobotCoapObserver.activate()

    }

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
        conn.forward(sendT)
        var rep =""
        try {
            rep = conn.reply("msg(ticketInvalid, reply, testunit, truck, testStore(_), 1)").toString()
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }


        assertTrue(rep.contains("ticketInvalid"))
    }

}
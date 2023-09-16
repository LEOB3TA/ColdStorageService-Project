package test.it.unibo

import it.unibo.ctxcoldstorageservice.main
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil.getActor
import kotlinx.coroutines.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import resources.ColdStorageService
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils

class TestColdStorageService {
    companion object {
        private var setup = false
        private lateinit var conn: Interaction2021
    }

    @Before
    fun setUp() {
        if (!setup) {
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
            setup=true
        }
        else{
            ColdStorageService.resetAll()
        }
    }

    // Interaction CSS -> Truck
    @Test
    @Throws(InterruptedException::class)
    fun testStoreFoodSuccess(){
        CommUtils.outmagenta("TestColdStorageService   |  testStoreFoodSuccess")
        var asw=""
        val storeFood = "msg(storeFood, request, testunit, coldstorageservice, storeFood(10) ,1)"
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
        CommUtils.outmagenta("TestColdStorageService   |  testStoreFoodFail")
        var asw=""
        val storeFood = "msg(storeFood, request, testunit, coldstorageservice, storeFood(100000) ,1)"
        try {
            asw =conn.request(storeFood)
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }
        println(asw)
        assertTrue(asw.contains("storeReject"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testSendTicket(){
        val ticket : resources.model.Ticket = resources.model.Ticket(1, 1)
        ColdStorageService.getTicketList().add(ticket)
        CommUtils.outmagenta("TestSendTicket")
        val sendT = "msg(sendTicket, request, testunit, coldstorageservice, sendTicket(1) ,1)" //numero ticket=1
        var rep=""
        try {
            rep = conn.request(sendT)
            //var rep = conn.request("msg(ticketInvalid, reply, testunit, truck, testStore(_), 1)").toString()
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }
        println(rep)
        assertTrue(rep.contains("ticketValid"))

    }

    @Test
    @Throws(InterruptedException::class)
    fun testSendTicketExpired(){
        CommUtils.outmagenta("TestColdStorageService   |   testSendTicketExpired")
        val ticket : resources.model.Ticket = resources.model.Ticket(2, 2)
        ColdStorageService.getTicketList().add(ticket)
       Thread.sleep(5000)
        val sendT = "msg(sendTicket, request, testunit, coldstorageservice, sendTicket(2) ,1)" //numero ticket=1
        var rep=""
        try {
            rep = conn.request(sendT)
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }
        assertTrue(rep.contains("ticketExpired"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testSendInvalidTicket(){
        CommUtils.outmagenta("TestColdStorageService   |   TestSendInvalidTicket")
        val sendT = "msg(sendTicket, request, testunit, coldstorageservice, sendTicket(-1) ,1)"
        var rep=""
        try {
            rep = conn.request(sendT)
            //var rep = conn.request("msg(ticketInvalid, reply, testunit, truck, testStore(_), 1)").toString()
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }
        assertTrue(rep.contains("ticketNotValid"))
    }


    @Test
    @Throws(InterruptedException::class)
    fun testDeposit(){

        CommUtils.outmagenta("TestColdStorageService   |   TestDeposit")
        val deposit = "msg(deposit, request, testunit, coldstorageservice, deposit(_),1)"
        var rep=""
        GlobalScope.launch {
            try {
                rep = conn.request(deposit)
            }catch (e: Exception) {
                CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
            }
        }
        Thread.sleep(1000)
        val pickupDone = "msg(pickupdone, reply, trasporttrolley, coldstorageservice, pickupdone(_),1)" //da notare il mittente trasporttrolley
        try {
            conn.reply(pickupDone)
        }catch (e: Exception) {
            CommUtils.outmagenta("TestColdStorageService	|	 some err in request: $e")
        }
        Thread.sleep(1000)
        assertTrue(rep.contains("chargeTaken"))
    }



}
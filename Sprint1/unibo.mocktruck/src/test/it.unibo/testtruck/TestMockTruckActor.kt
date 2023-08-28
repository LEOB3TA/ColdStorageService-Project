package test.it.unibo.testtruck

import it.unibo.kactor.QakContext.Companion.getActor
import junit.framework.Assert
import it.unibo.ctxtruck.main
import it.unibo.mocktruck.Mocktruck
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runners.Parameterized
import resources.TruckState
import test.it.unibo.coapobs.TypedCoapTestObserver
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue
import kotlin.system.exitProcess

class TestMockTruckActor{

    companion object{ private var setupOk = false
        private lateinit var conn : Interaction2021
        private lateinit var obs : TypedCoapTestObserver<TruckState>}


    @Before
    fun before() {
        if(!setupOk){
            println("TestMockTruckActor       | initTest")
            object : Thread(){
                override fun run(){
                    main()
                }
            }.start()
            var mocktruckactor = getActor("mocktruck")
            while (mocktruckactor == null){
                println("TestMockTruck       | waiting application start")
                CommUtils.delay(200)
                mocktruckactor = getActor("mocktruck")
            }
            println("TestMockTruckActor       | application started")
            try {
                conn = TcpClientSupport.connect("localhost",8092,5)
            }catch (e: Exception){
                println("TestMockTruck       | connection failed")
                exitProcess(1)
            }
            startObs("localhost:8092")
            println(obs.getNext().toString())
            Thread.sleep(2000)
        }else{
            println("TestMockTruckActor       | clearHistory and reset")
            Thread.sleep(2000)
            conn.forward("msg(reset, dispatch, testunit, mockTruck, reset(_), 1)")
            obs.clearHistory()
        }
    }


    fun startObs(addr: String?) {
        val setupOkA = ArrayBlockingQueue<Boolean>(1)
        object : Thread() {
            override fun run() {
                val ctx = "ctxtruck"
                val actor = "mocktruck"
                val path = "$ctx/$actor"
                val coapConn = CoapConnection(addr, path)
                obs = TypedCoapTestObserver{
                    TruckState.fromJsonString(it)
                }
                coapConn.observeResource(obs)
                try {
                    setupOkA.put(true)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        setupOk=setupOkA.take()
    }
    @Test
    @Throws(InterruptedException::class)
    fun testStoreFoodAccepted(){
        conn.forward("msg(testStore, dispatch, testunit, mockTruck, testStore(_), 1)")
       var asw = ""
        val prevState = obs.currentTypedState!!
        println("TestMockTruckActor  |   testStoreFoodAccepted...")
        try {
            conn.reply("msg(storeAccepted, reply, testunit, mockTruck, storeAccepted(1), 1)")
        }catch (e:Exception){
            e.printStackTrace()
        }
        Thread.sleep(2000)
        val newState = obs.currentTypedState!!.toString()
        println(newState)
        Assert.assertTrue(newState.contains("ACCEPTED"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testStoreFoodRejected(){
        conn.forward("msg(testStore, dispatch, testunit, mockTruck, testStore(_), 1)")
        var asw = ""
        val prevState = obs.currentTypedState!!
        println("TestMockTruckActor  |   testStoreFoodRejected...")
        try {
            conn.reply("msg(storeRejected, reply, testunit, mockTruck, storeRejected(_), 1)")
        }catch (e:Exception){
            e.printStackTrace()
        }
        Thread.sleep(2000)
        val newState = obs.currentTypedState!!.toString()
        println(newState)
        Assert.assertTrue(newState.contains("REJECTED"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testSendTicketExpired(){
        conn.forward("msg(testTicket, dispatch, testunit, mockTruck, testTicket(_), 1)")
        var asw = ""
        val prevState = obs.currentTypedState!!
        println("TestMockTruckActor  |   testSendTicketExpired...")
        try {
            conn.reply("msg(ticketExpired, reply, testunit, mockTruck, ticketExpired(_), 1)")
        }catch (e:Exception){
            e.printStackTrace()
        }
        Thread.sleep(2000)
        val newState = obs.currentTypedState!!.toString()
        println(newState)
        Assert.assertTrue(newState.contains("TICKETEXPIRED"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testSendTicketValid(){
        conn.forward("msg(testTicket, dispatch, testunit, mockTruck, testTicket(_), 1)")
        var asw = ""
        val prevState = obs.currentTypedState!!
        println("TestMockTruckActor  |   testSendTicketValid...")
        try {
            conn.reply("msg(ticketValid, reply, testunit, mockTruck, ticketValid(_), 1)")
        }catch (e:Exception){
            e.printStackTrace()
        }
        Thread.sleep(2000)
        val newState = obs.currentTypedState!!.toString()
        println(newState)
        Assert.assertTrue(newState.contains("SENDDEPOSIT"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDeposit(){
        conn.forward("msg(testDeposit, dispatch, testunit, mockTruck, testDeposit(_), 1)")
        var asw = ""
        val prevState = obs.currentTypedState!!
        println("TestMockTruckActor  |   testSendTicket...")
        try {
            conn.reply("msg(chargeTaken, reply, testunit, mockTruck, chargeTaken(_), 1)")
        }catch (e:Exception){
            e.printStackTrace()
        }
        Thread.sleep(3000)
        val newState = obs.currentTypedState!!.toString()
        println(newState)
        Assert.assertTrue(newState.contains("IDLE"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun testError(){
        val prevState = obs.currentTypedState!!
        println("waiting one minute to trigger truck error")
        Thread.sleep(62000)
        val newState = obs.currentTypedState!!.toString()
        println(newState)
        Assert.assertTrue(newState.contains("HANDLEERROR"))
    }



}
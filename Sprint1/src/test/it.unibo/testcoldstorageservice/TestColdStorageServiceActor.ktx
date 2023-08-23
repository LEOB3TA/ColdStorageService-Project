package test.it.unibo.testcoldstorageservice

import it.unibo.kactor.QakContext.Companion.getActor
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import main.it.unibo.ctxstorageservice.main
import org.junit.Before
import org.junit.Test
import resources.ColdStorageServiceState
import test.it.unibo.coapobs.TypedCoapTestObserver
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue

class TestColdStorageServiceActor{
    private lateinit var conn : Interaction2021
    private lateinit var obs : TypedCoapTestObserver<ColdStorageServiceState>
    private var setupOk = false

    private val weight = 100.0
    @Before
    fun before() {
        if(!setupOk){
            println("TestColdStorageServiceActor       | initTest")
            object : Thread(){
                override fun run(){
                    main()
                }
            }.start()
            var coldstorageservicesctor = getActor("coldstorageservice")
            while (coldstorageservicesctor == null){
                println("TestColdStorageServiceActor       | waiting applicatino start")
                CommUtils.delay(200)
                coldstorageservicesctor = getActor("coldstorageservice")
            }
            try {
                conn = TcpClientSupport.connect("localhost",8092,5)
            }catch (e: Exception){
                println("TestColdStorageServiceActor       | connectino failed")
            }
            startObs("localhost:8092")
            obs.getNext()
            setupOk = true
        }else{
            obs.clearHistory()
        }
    }

    fun startObs(addr:String){
        val setupOk = ArrayBlockingQueue<Boolean>(1)
        object : Thread(){
            override fun run(){
                obs = TypedCoapTestObserver {
                    ColdStorageServiceState.fromJsonString(it)
                }
                var ctx  = "ctxstorageservice"
                var act  = "coldstorageservice"
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
    fun testAccept() {
        var asw = ""
        val prevState =obs.currentTypedState!!

        var storeFood = "msg(storeFood, request, testunit, coldstorageserviceactor, storeFood( $weight),1)"
        println("TestColdStorageServiceActor	|	testaccept on message: $storeFood")
        try {
            asw = conn.request(storeFood)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        assertTrue(asw.contains("loadaccept"))

        var newState = obs.getNext()
        assertEquals(prevState.getCurrentBoxWeight()+weight, newState.getCurrentBoxWeight())

    }

    @Test
    fun testRejected() {
        val prevState =obs.currentTypedState!!
        val storeFood = "msg(storeFood, request, testunit, coldstorageserviceactor, storeFood(700),1)"
        println("TestColdStorageServiceActor	|	testrejected on message: $storeFood")
        var asw = ""
        try {
            asw = conn.request(storeFood)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var newState = obs.getNext()
        assertTrue(asw.contains("loadrejected"))
        assertEquals(prevState.getCurrentBoxWeight()+weight, newState.getCurrentBoxWeight())
    }
    @Test
    fun testChargeTaken() {
        val prevState =obs.currentTypedState!!
        val deposit = "msg(deposit, request, testunit, coldstorageserviceactor, deposit(TICKET),1)"
        println("TestColdStorageServiceActor	|	testChargeTacken on message: $deposit")
        var asw = ""
        try {
            asw = conn.request(deposit)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var newState = obs.getNext()
        assertTrue(asw.contains("chargeTaken"))
        // TODO: vedere se fare una assert sul TICKET
//        assertEquals(prevState.getCurrentBoxWeight()+weight, newState.getCurrentBoxWeight())
    }
}

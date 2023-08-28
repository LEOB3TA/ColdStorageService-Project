package test.it.unibo.testtruck

import it.unibo.kactor.QakContext.Companion.getActor
import junit.framework.Assert
import main.it.unibo.ctxstorageservice.main
import org.junit.Before
import org.junit.Test
import resources.ColdStorageServiceState
import resources.ServiceAccessGuiState
import test.it.unibo.coapobs.TypedCoapTestObserver
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue
class TestServiceAccessGuiActor{
    private lateinit var conn : Interaction2021
    private lateinit var obs : TypedCoapTestObserver<ColdStorageServiceState>
    private var setupOk = false

    private val weight = 100.0
    @Before
    fun before() {
        if(!setupOk){
            println("TestServiceAccessGuiActor       | initTest")
            object : Thread(){
                override fun run(){
                    main()
                }
            }.start()
            var serviceaccesguiactor = getActor("serviceaccesgui")
            while (serviceaccesguiactor == null){
                println("TestServiceAccessGuiActor       | waiting applicatino start")
                CommUtils.delay(200)
                serviceaccesguiactor = getActor("serviceaccesgui")
            }
            try {
                conn = TcpClientSupport.connect("localhost",8087,5)
            }catch (e: Exception){
                println("TestServiceAccessGuiActor       | connectino failed")
            }
            startObs("localhost:8087")
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
                    ServiceAccessGuiState.fromJsonString(it)
                }
                var ctx  = "ctxtruck"
                var act  = "serviceaccesgui"
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
    fun testStoreFood(){
        var asw = ""
        val prevState = obs.currentTypedState!!
        println("TestServiceAccessGuiActor  |   testStoreFood...")
        val storeFood = "msg(storeFood, request, testunit, transporttrolley, storeFood(100), 1)"
        try {
            asw = conn.forward(storeFood)
        }catch (e:Exception){
            e.printStackTrace()
        }
        var newState = obs.getNext()

        Assert.assertTrue(newState.contains("storeaccepted"))
    }

    @Test
    fun testDeposit(){
        var asw = ""
        val prevState = obs.currentTypedState!!
        println("TestServiceAccessGuiActor  |   testDeposit...")
        val Ticket = "test ticket"
        val deposit = "msg(deposit, request, testunit, transporttrolley, deposit($Ticket), 1)"
        try {
            asw = conn.forward(deposit)
        }catch (e:Exception){
            e.printStackTrace()
        }
        // TODO: chek on TICKETTIME
        var newState = obs.getNext()
        Assert.assertTrue(newState.contains("chargeTaken"))

    }
}
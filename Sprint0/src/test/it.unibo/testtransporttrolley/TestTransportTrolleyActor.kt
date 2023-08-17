package it.unibo.testtransporttrolley

import main.it.unibo.transporttrolley.Transporttrolley
import main.it.unibo.ctxstorageservice.*
import it.unibo.kactor.QakContext.Companion.getActor
import org.junit.Before
import org.junit.Test
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue
import kotlin.jvm.Throws
import coapobs.*
import unibo.basicomm23.interfaces.Interaction2023

class TestTransportTrolleyActor{
    private lateinit var conn: Interaction2023
    private lateinit var obs : TypedCoapTestOberver<TransportTrolleyState>
    private var setupOk = false

    @Before
    fun setup(){
        println("TestTransportTrolleyActor   |   initTest")
        if(!setupOk){
            object : Thread(){
                override fun run() {
                    main()
                }
            }.start()

            var trolleyActor = getActor("transporttrolley")
            while(trolleyActor == null){
                println("TestTransportTrolleyActor   |   waiting for start")
                CommUtils.delay(200)
                trolleyActor = getActor("transportrolley")
            }
            try{
                conn= TcpClientSupport.connect("localhost",8092,5)
            }catch (e: Exception){
                println("TestTransportTrolleyActor   |   connection failed")
            }
            startObs("localhost:8092")
            obs.getNext()
            setupOk = true
        }else{
            obs.clearHistory()
        }
    }

    fun startObs(addr: String){
        val setupOk = ArrayBlockingQueue<Boolean>(1)
        object : Thread(){
            override fun run(){
                obs = TypedCoapTestObserver { TransportTrolleyState.fromJsonString(it) }
                var ctx  = "ctxstorageservice"
                var act  = "transporttrolley"
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
    fun testPickup(){
        println("TestTransportTrolleyActor  |   testPickup...")
        val pickup = "msg(pickingup, dispatch, testunit, transporttrolley, pickup, 1)"
        conn.forward(pickup)

        assertEquals(obs.getNext().getTTState().toString(), "PICKINGUP")
    }

    @Test
    fun testDropout(){
        println("TestTransportTrolleyActor  |   testDropout...")
        val dropout = "msg(droppingout, dispatch, testunit, transporttrolley, dropout, 1)"
        conn.forward(dropout)

        assertEquals(obs.getNext().getTTState().toString(), "DROPOUT")
    }

    @Test
    fun testBackHome(){
        println("TestTransportTrolleyActor  |   testBackHome...")
        val backHome = "msg(backhome, dispatch, testunit, transporttrolley, backhome, 1)"
        conn.forward(backHome)

        assertEquals(obs.getNext().getTTState().toString(), "IDLE")
    }


}
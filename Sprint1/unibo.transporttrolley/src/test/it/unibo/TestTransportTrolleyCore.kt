package test.it.unibo

import it.unibo.ctxcoldstorageservice.main
import it.unibo.kactor.QakContext
import mockCtx.mockCtx
import coapobs.TypedCoapTestObserver
import it.unibo.kactor.QakContext.Companion.getActor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


import transporttrolley.state.TransportTrolleyState
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.utils.CommUtils
import unibo.basicomm23.tcp.TcpClientSupport
import java.util.concurrent.ArrayBlockingQueue
// TODO: implement engage and disengage for basicrobot
class TestTransportTrolleyCore {
    companion object{

        private var setup = false
        private lateinit var conn: Interaction2021
        private lateinit var obs: TypedCoapTestObserver<TransportTrolleyState>

        @BeforeClass
        @JvmStatic
        fun startMockCtx(){
            CommUtils.outmagenta("TestTransportTrolleyCore   |   launching mockCtx...")
            mockCtx("basicrobot", 8020, ) {
                if(it.msgId() == "engage"){
                    conn.forward("msg(engagedone, reply, testunit, transporttrolleymover, engagedone(OK),1)")
                }
                else if(it.msgId() == "doplan"){
                    conn.forward("msg(doplandone, reply, testunit, transporttrolleymover, doplandone(OK) ,1)")
                }
            }
            mockCtx("coldstorageservice",8092)
        }
    }

    @Before
    fun setUp(){
        if(!setup) {
            CommUtils.outmagenta("TestTransportTrolleyCore	|	setup...")

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()
            var actCore = getActor("basicrobot")
            var otCore = getActor("coldstorageservicecore")
            var tTCore = getActor("transporttrolley")
            while (tTCore == null) {
                CommUtils.outmagenta("TestTransportTrolleyCore	|	waiting for application starts...")
                CommUtils.delay(200)
                tTCore = getActor("transporttrolley")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8092, 5)
            } catch (e: Exception) {
                CommUtils.outmagenta("TestTransportTrolleyCore	|	TCP connection failed...")
            }
            startObs("localhost:8092")
            obs.getNext()
            setup = true
        } else {
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
                val ctx = "ctxtransporttrolley"
                val actor = "transporttrolleycore"
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

    @Test
    @Throws(InterruptedException::class)
    fun test1Pickup(){
       CommUtils.outmagenta("TestTransportTrolleyCore	|	testPickup...")

        var asw = ""

        var pickup = "msg(pickup, request, testunit, transporttrolleycore, pickup(_) ,1)"

        try {
            asw = conn.request(pickup)
        } catch (e: Exception) {
            CommUtils.outmagenta("TestTransportTrolleyCore	|	 some err in request: $e")
        }

        var newState = obs.getNext()

        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()

        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("PICKINGUP", newState.getCurrState().toString())
        assertTrue(asw.contains("pickupdone"))

        newState = obs.getNext()

    }
    @Test
    @Throws(InterruptedException::class)
    fun test2Dropout(){
        CommUtils.outmagenta("TestTransportTrolleyCore	|	testDropout...")

        var dropout = "msg(dropout, dispatch, testunit, transporttrolleycore, dropout(FW) ,1)"

        try{
            conn.forward(dropout)
        } catch (e: Exception) {
            CommUtils.outmagenta("TestTransportTrolleyCore	|	 some err in request: $e")
        }

        var newState = obs.getNext()

        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()

        assertEquals("COLDROOM", newState.getCurrPosition().toString())
        assertEquals("DROPPINGOUT", newState.getCurrState().toString())

    }
    @Test
    @Throws(InterruptedException::class)
    fun test3BackHome(){
        CommUtils.outmagenta("TestTransportTrolleyCore	|	testBackHome...")

        var gotohome = "msg(gotohome, dispatch, testunit, transporttrolleycore, gotohome(_) ,1)"

        try{
            conn.forward(gotohome)
        } catch (e: Exception) {
            println("TestTransportTrolleyCore	|	 some err in request: $e")
        }

        var newState = obs.getNext()

        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()

        assertEquals("HOME", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

    }
}
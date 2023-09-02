package old.transporttrolley

import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.After
import org.junit.Assert.*
//import org.junit.FixMethodOrder
//import org.junit.runner.OrderWith

import kotlin.concurrent.thread
import unibo.basicomm23.interfaces.Interaction2021
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.ColorsOut
import unibo.basicomm23.utils.CommSystemConfig
import unibo.basicomm23.utils.CommUtils
import old.transporttrolley.RunCtxTransportTrolleyTest
import old.transporttrolley.CoapObserver



class TestTranportTrolley {
    companion object {
        const val hostname = "localhost"
        const val port = 11703
        const val actorName = "transporttrolleycore"
        const val ctxName = "ctxtransporttrolley"
    }
    private lateinit var obs: CoapObserver
    private lateinit var interaction: Interaction2021

    @Before
    fun up() {
        CommSystemConfig.tracing = false

        CommUtils.outcyan("===== TEST Started =====");

        obs = CoapObserver()

        var thread = thread {
            RunCtxTransportTrolleyTest().main()
        }
        var tTCore = QakContext.getActor("$actorName")

        waitForActor(actorName)

        obs.addContext(ctxName, Pair(hostname, port))
        obs.addActor(actorName, ctxName)


        obs.createCoapConnection(actorName)

        try {
            interaction = TcpClientSupport.connect(hostname, port, 1);
            CommUtils.outmagenta("TCP Connected to the context established.");
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
    }

    @After
    fun down() {
        obs.clearCoapHistory()
        obs.closeAllCoapConnections()

        try {
            interaction.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        CommUtils.delay(1000)
        CommUtils.outcyan("===== TEST Completed =====")
        CommUtils.delay(1000)
    }

    @Test
    fun testDeposit() {
        CommUtils.outcyan("TEST: Check if the TransportTrolley performs the deposit correctly, according to COAP history.");

        obs.clearCoapHistory()

        CommUtils.outmagenta("[coldService] Sending deposit request to TransportTrolley.");
        var reply = simulateDepositRequest(interaction, 10.0F)
        CommUtils.outmagenta("[WasteService] Received reply: $reply." );

        assertTrue(reply.contains("pickupc"))
        if(reply.contains("pickup"))
            CommUtils.outgreen("TEST Deposit reply containing pickup: PASSED");
        else CommUtils.outred("TEST Deposit reply containing pickup: FAILED");

        // Wait the TransportTrolley to get home
        obs.waitForSpecificHistoryEntry("$actorName(MOVING)", timeout= 5000)

        val actionsList = mutableListOf(
            "$actorName(MOVING)",
            "$actorName(IDLE)",
            "$actorName(STOPPED)",
            "$actorName(PICKINGUP)",
            "$actorName(DROPPINGOUT)"
        )
        assertTrue(obs.checkIfHystoryContainsOrdered(actionsList, strict=true))
        if(obs.checkIfHystoryContainsOrdered(actionsList, strict=true))
            CommUtils.outgreen("TEST TransportTrolley performing the deposit action: PASSED");
        else CommUtils.outred("TEST TransportTrolley performing the deposit action: FAILED");

        obs.clearCoapHistory()
        obs.closeAllCoapConnections()
    }

    private fun simulateStoreRequest(interaction: Interaction2021, FWeight: Float): String {
        val request = MsgUtil.buildRequest("truck", "storerequest",
            "storerequest($FWeight)", "coldstorageservice").toString()
        var reply = ""

        try {
            reply = interaction.request(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return reply
    }
    private fun simulateDepositRequest(interaction: Interaction2021, FWeight: Float): String {
        val request = MsgUtil.buildRequest("coldstorageservice", "deposit",
            "deposit($FWeight)", "$actorName").toString()
        var reply = ""

        try {
            reply = interaction.request(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return reply
    }

    private fun waitForActor(actorName: String) {
        var actor = QakContext.getActor(actorName)
        while(actor == null) {
            ColorsOut.outappl("Waiting for actor $actorName...", ColorsOut.MAGENTA);
            CommUtils.delay(500)
            actor = QakContext.getActor(actorName)
            println(actor)
        }
        ColorsOut.out("Actor $actorName ready.", ColorsOut.MAGENTA)
    }
}
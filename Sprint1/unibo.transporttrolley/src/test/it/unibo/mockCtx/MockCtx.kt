package mockCtx

import io.ktor.network.selector.*
import kotlinx.coroutines.*
import unibo.basicomm23.msg.ApplMessage
import io.ktor.util.network.*
import io.ktor.utils.io.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.CancellationException
import unibo.basicomm23.utils.CommUtils

class MockCtx (val ctxName : String,
               val port : Int,
               private val onMessage: (ApplMessage)  -> Unit = {}) {

    private var socket : ServerSocket? = null
    private val workers = mutableListOf<MockCtxWorker>()
    private val scope = CoroutineScope(CoroutineName("CoroutineScope[$ctxName]"))



    fun start(){
        scope.launch {
            val selectorManager = SelectorManager(Dispatchers.IO)
            socket    = aSocket(selectorManager).tcp().bind("127.0.0.1", port)
            try {
                while(true) {
                    var clientSock = socket!!.accept()
                    workers.add(workerFor(clientSock, ctxName, scope, onMessage))
                    CommUtils.outblue("mockCtx[$ctxName]    |   accepted connection with ${clientSock.remoteAddress}")
                }
            } catch (ce : CancellationException) {
                println("Error: $ce")
                socket!!.close()
            }
        }

    }

    fun kill() {
        scope.cancel()
    }

}

fun mockCtx(name : String, port : Int, onMessage: (ApplMessage) -> Unit = {}) : MockCtx =
    MockCtx(name, port, onMessage).apply { this.start() }
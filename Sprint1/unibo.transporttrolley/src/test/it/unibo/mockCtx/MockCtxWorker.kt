package mockCtx


import unibo.basicomm23.utils.CommUtils
import io.ktor.network.selector.*
import io.ktor.util.network.*
import io.ktor.utils.io.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.*
import unibo.basicomm23.msg.ApplMessage

class MockCtxWorker(
    val clientSock: Socket,
    val ctxName: String,
    val scope: CoroutineScope,
    val onMessage: (ApplMessage) -> Unit = {}
) {

    private val readChannel = clientSock.openReadChannel()
    private var job : Job? = null

    fun start() {
        job = scope.launch {
            try {
                val port = clientSock.localAddress.toJavaAddress().port
                while (true) {
                    CommUtils.outblue("mockCtxWorker[$ctxName]    |   waiting for messages on: $port...")
                    val name = readChannel.readUTF8Line()
                    try {
                        onMessage(ApplMessage(name!!))
                    } catch (e : Exception) {
                        CommUtils.outred("mockCtxWorker[$ctxName]    |   error: $e")
                    }
                    CommUtils.outblue("mockCtxWorker[$ctxName]    |   received message $name on $port")
                }
            } catch (ce: CancellationException) {
                println("Error: $ce")
                clientSock.close()
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}

fun workerFor(clientSock: Socket, ctxName: String, scope: CoroutineScope, onMessage: (ApplMessage) -> Unit = {}) : MockCtxWorker =
    MockCtxWorker(clientSock, ctxName, scope, onMessage).apply { this.start() }
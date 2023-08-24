package mockCtx

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import unibo.basicomm23.msg.ApplMessage
import unibo.basicomm23.utils.CommUtils
import java.net.Socket
import kotlin.coroutines.cancellation.CancellationException


class MockCtxWorker(
    val clientSock: Socket,
    val ctxName: String,
    val scope: CoroutineScope,
    val onMessage: (ApplMessage) -> Unit = {}
) {

    private val readChannel = clientSock.getInputStream()
    private var job : Job? = null

    fun start() {
        job = scope.launch {
            try {
                val port = clientSock.localAddress
                while (true) {
                    CommUtils.outblue("mockCtxWorker[$ctxName]    |   waiting for messages on: $port...")
                    val name = readChannel.readUTF8Line()
                    try {
                        onMessage(ApplMessage(name!!))
                    } catch (e : Exception) {
                        CommUtils.outblue("mockCtxWorker[$ctxName]    |   error: $e")
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
package old.transporttrolley

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

class RunCtxTransportTrolleyTest {
    fun main() = runBlocking {
        QakContext.createContexts(
            "localhost", this, "transporttrolley.pl", "sysRules.pl","ctxtransporttrolley"
        )
    }
}
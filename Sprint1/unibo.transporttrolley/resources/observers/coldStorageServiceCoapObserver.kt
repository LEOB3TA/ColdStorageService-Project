package observers

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse

object coldStorageServiceCoapObserver {
    private val client = CoapClient()

    private val ipaddr      = "localhost:8099"		//5683 default
    private val context     = "ctxcoldstorageserviee"
    private val destactor   = "coldstorageservice"


    fun activate(  ){
        val uriStr = "coap://$ipaddr/$context/$destactor"
        println("CoapObserver | START uriStr: $uriStr")
        client.uri = uriStr
        client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                val content = response.responseText
                println("CoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
            }
            override fun onError() {
                println("CoapObserver | FAILED")
            }
        })
    }
}




fun main( ) {
    coldStorageServiceCoapObserver.activate()
    System.`in`.read()   //to avoid exit
}
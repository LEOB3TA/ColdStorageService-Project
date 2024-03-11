package resources

import com.pi4j.io.gpio.*
import kotlinx.coroutines.*
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import unibo.basicomm23.utils.CommUtils
import java.io.File

object ledSupport {
    lateinit var ledType : String
    lateinit var gpio: GpioController
    lateinit var gpioPin: GpioPinDigitalOutput
    lateinit var pulseState: GpioPin
    val configFileName="ledConfig.json"
    val jsonParser = JSONParser()

    fun create() {
        // Leggi il contenuto del file JSON
        val config = File("${configFileName}").readText(Charsets.UTF_8)
        val jsonObject   = jsonParser.parse( config ) as JSONObject
        ledType = jsonObject["type"].toString()
        if (ledType != "real" && ledType!="simulated"){
            CommUtils.outred(" -- ledSupport |  ${ledType}: unsupported led type")
        }
        CommUtils.outred(" -- ledSupport | CREATING support for ${ledType} led")
        //creazione support gpio
        if(ledType == "real"){
            gpio=GpioFactory.getInstance()
            gpioPin= gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "realLed", PinState.LOW)
            gpioPin.mode= PinMode.DIGITAL_OUTPUT
            gpioPin.blink(500,2000)
            CommUtils.outred("-- ledSupport | CREATED support for real led")
        }
    }
    fun on(){
        when(ledType){
            "simulated" -> CommUtils.outmagenta("LED ON")
            "real" -> {gpioPin.blink(0);gpioPin.high()}
        }
    }

    fun off(){
        when(ledType){
            "simulated" -> CommUtils.outmagenta("LED OFF")
            "real" ->{gpioPin.blink(0); gpioPin.low()}
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun blink(){
        when(ledType){
            "simulated" -> CommUtils.outmagenta("LED BLINKS")
            "real" -> gpioPin.blink(500)
        }
    }




}
package resources

import com.pi4j.io.gpio.*
import com.pi4j.wiringpi.Gpio
import kotlinx.coroutines.*
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import unibo.basicomm23.utils.CommUtils
import java.io.File

object ledSupport {
    lateinit var ledType : String
    lateinit var gpio: GpioController
    lateinit var gpioPin: GpioPinDigitalOutput
    lateinit var job : Job
    val configFileName="ledConfig.json"
    val jsonParser = JSONParser()

    fun create() {
        // Leggi il contenuto del file JSON
        val config = File("${configFileName}").readText(Charsets.UTF_8)
        val jsonObject   = jsonParser.parse( config ) as JSONObject
        ledType = jsonObject.get("type").toString()
        //creazione support gpio
        if(ledType == "real"){
            gpio=GpioFactory.getInstance()
            gpioPin= gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "realLed", PinState.LOW)
            gpioPin.low()
        }
        //Conferma testuale
        CommUtils.outred(" -- ledSupport | CREATING support for ${ledType} led")


    }
    //TODO controllare che i gpio low all'inizio vanno bene o no
    fun on(){
        when(ledType){
            "simulated" -> CommUtils.outmagenta("LED ON")
            "real" -> {if(job.isActive){ runBlocking { job.cancelAndJoin()}};gpioPin.low();gpioPin.high()}
        }
    }

    fun off(){
        when(ledType){
            "simulated" -> CommUtils.outmagenta("LED OFF")
            "real" -> {if(job.isActive){ runBlocking { job.cancelAndJoin()}};gpioPin.low()}
        }
    }
    //TODO controllare che esca di qui o perlomeno che si interrompa
    @OptIn(DelicateCoroutinesApi::class)
    fun blink(){
        when(ledType){
            "simulated" -> CommUtils.outmagenta("LED BLINKS")
            "real" -> {gpioPin.low(); job= GlobalScope.launch { while (true){
                gpioPin.high()
                CommUtils.delay(500)
                gpioPin.low()
            }}}
        }
    }




}
package unibo.testsensors

import unibo.basicomm23.interfaces.Interaction2023

class TestLedActor{
    private lateinit var conn: Interaction2023
    private lateinit var : TypedCoapTestObserver<LedState>
    private var setupOk = false

    @Before
    fun setup(){
        println("TestLedQakActor	|	initTest...")
        if(!setupOk){
            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()

            var ledActor = QakContext.getActor("ledqakactor")
            while(ledActor == null){
                println("TestLedQakActor	|	waiting for application start...")
                CommUtils.delay(200)
                ledActor = QakContext.getActor("ledqakactor")
            }
            try {
                conn = TcpClientSupport.connect("192.168.1.xxx", 8086, 5)
            } catch (e: Exception) {
                println("TestLedQakActor	|	connection failed...")
            }
            startObs("192.168.1.xxx:8086")
            obs.getNext()
            setupOk = true
        }
        else{
            obs.clearHistory()
        }
    }

    fun startObs(addr : String){
        val setupOk = ArrayBlockingQueue<Boolean>(1)
        object : Thread(){
            override fun run(){
                obs = TypedCoapTestObserver { LedState.fromJsonString(it) }
                var ctx  = "ctxrasp"
                var act  = "ledqakactor"
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
    fun testTurnOn(){
        println("TestLedQakActor  |   testOn...")
        val turnOn = "msg(turnOn, dispatch, testunit, ledqakactor, turnOn, 1)"
        conn.forward(turnOn)

        assertEquals(obs.getNext().getTTState().toString(), "ON")
    }

    @Test
    fun testTurnOff(){
        println("TestLedQakActor  |   testOff...")
        val turnOff = "msg(turnOff, dispatch, testunit, ledqakactor, turnOff, 1)"
        conn.forward(turnOff)

        assertEquals(obs.getNext().getTTState().toString(), "OFF")
    }

    @Test
    fun testBlink(){
        println("TestLedQakActor  |   testBlink...")
        val blink = "msg(blink, dispatch, testunit, ledqakactor, blink, 1)"
        conn.forward(blink)

        assertEquals(obs.getNext().getTTState().toString(), "BLINKING")
    }



}
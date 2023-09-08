package resources.state

import org.json.JSONObject
import java.io.File

class SystemState {
    enum class CurrStateTrolley  {
        IDLE,STOPPED,MOVING,PICKINGUP,DROPPINGOUT
    }

    enum class TTPosition{
        HOME, INDOOR, ONTHEROAD, PORT
    }

    data class SystemState(
        private var currState : CurrStateTrolley = CurrStateTrolley.IDLE,
        private var currPosition: TTPosition = TTPosition.HOME,
        private var boxMW : Double = 0.0,
        private var boxCW: Double = 0.0
        ){
        init{
            val config = File("coldServiceSystemConfig.json")
            val jsonObject = JSONObject(config)
            val boxMW = jsonObject.getDouble("MAXW")
        }
    }
}
package state

import com.google.gson.Gson

enum class CurrStateTrolley  {
    IDLE,STOPPED,MOVINGTOPORT,MOVINGTOHOME,PICKINGUP,DROPPINGOUT
}

enum class TTPosition{
    HOME, INDOOR, ONTHEROAD, PORT
}

enum class LedState{
    ON, OFF, BLINKS
}

data class TransportTrolleyState(
    private var currState: CurrStateTrolley = CurrStateTrolley.IDLE,
    private var currPosition: TTPosition = TTPosition.HOME,
    private var currLed : LedState = LedState.OFF
){
    fun setCurrState(state: CurrStateTrolley){
        currState = state
    }
    fun setCurrPosition(position : TTPosition){
        currPosition = position
    }
    fun getCurrState() : CurrStateTrolley{
        return currState
    }
    fun getCurrPosition() : TTPosition{
        return currPosition
    }

    fun getCurrLed(): LedState {
        return currLed
    }
    fun setLed(state: LedState){
        currLed = state
    }

    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : TransportTrolleyState{
            return gson.fromJson(str, TransportTrolleyState::class.java)
        }
    }

    fun toJsonString() : String{
        return gson.toJson(this)
    }
}




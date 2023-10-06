package state

import com.google.gson.Gson

enum class CurrStateTrolley  {
    IDLE,STOPPED,MOVINGTOPORT,MOVINGTOHOME,PICKINGUP,DROPPINGOUT
}

enum class TTPosition{
    HOME, INDOOR, ONTHEROAD, PORT
}

data class TransportTrolleyState(
    private var currState: CurrStateTrolley = CurrStateTrolley.IDLE,
    private var currPosition: TTPosition = TTPosition.HOME
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




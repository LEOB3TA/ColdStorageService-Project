package state

import com.google.gson.Gson
import state.TransportTrolleyState

enum class LState{
    ON, OFF, BLINKS
}

data class LedState(
    private var currLed : LState = LState.OFF
    ){
    fun getCurrState(): LState {
        return currLed
    }
    fun setState(state: LState){
        currLed = state
    }

    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : LState {
            return gson.fromJson(str, LState::class.java)
        }
    }

    fun toJsonString() : String{
        return gson.toJson(this)
    }
}
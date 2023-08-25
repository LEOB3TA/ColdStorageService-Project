package resources

import com.google.gson.Gson

enum class CurrStateTruck { ON, OFF, BLINKING }

data class TruckState(
        private var currState : CurrStateTruck
){

    companion object {
        private val gson = Gson()
        fun fromJsonString(str : String) : TruckState {
            return TruckState.gson.fromJson(str, TruckState::class.java)
        }
    }

    fun updateTruckState(newState : CurrStateTruck){
        currState = newState
    }

    fun getTTState() : CurrStateTruck {
        return currState
    }

    fun toJsonString() : String{
        return TruckState.gson.toJson(this)
    }

}
package resources

import com.google.gson.Gson

enum class CurrStateTruck { IDLE, SENDSTORE, REJECTED, ACCEPTED, SENDTICKET, HANDLETICKETEXPIRED, SENDDEPOSIT, HANDLEERROR}

data class TruckState(
        private var currState : CurrStateTruck = CurrStateTruck.IDLE
){

    companion object {
        private val gson = Gson()
        fun fromJsonString(str : String) : TruckState {
            return gson.fromJson(str, TruckState::class.java)
        }
    }

    fun setState(newState : CurrStateTruck){
        currState = newState
    }

    fun getTruckState() : CurrStateTruck {
        return currState
    }

    fun toJsonString() : String{
        return TruckState.gson.toJson(this)
    }

}
package resources

import com.google.gson.Gson

enum class ColdStorageServiceStateEnum {
    IDLE,
    SETUP,
    REQUESTEVALUATION,
    ACCEPTREQUEST,
    REJECTREQUEST,
    HANDLETICKETEXPIRED,
    DROPOUT,
    CHARGED,
    TICKETEVALUATION
}

const val MAXW = 100.0
const val TICKETTIME = 10000
val RT = (1..101).random()

class ColdStorageServiceState (private var currentState : ColdStorageServiceStateEnum = ColdStorageServiceStateEnum.IDLE) {

    companion object {
        private val gson = Gson()
        fun fromJson(json: String): ColdStorageServiceState {
            return Gson().fromJson(json, ColdStorageServiceState::class.java)
        }
    }

    fun setState(newState : ColdStorageServiceStateEnum){
        currentState = newState
    }

    fun getTruckState() : ColdStorageServiceStateEnum {
        return currentState
    }

    fun toJsonString() : String{
        return gson.toJson(this)
    }


}
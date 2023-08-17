package resources
import com.google.gson.Gson

enum class  CurrStateSonar {ACTIVE, OFF}
data class SonarState(private var currState: CurrStateSonar) {
    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : SonarState {
            return SonarState.gson.fromJson(str, SonarState::class.java)
        }
    }
    fun updateSonarState(newState : CurrStateSonar){
        currState = newState
    }

    fun getTTState() : CurrStateSonar {
        return currState
    }

    fun toJsonString() : String{
        return SonarState.gson.toJson(this)
    }
}
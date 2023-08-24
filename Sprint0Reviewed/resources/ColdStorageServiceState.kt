package resources

import com.google.gson.Gson
import org.json.*

data class ColdStorageServiceState(private val mw : Float, private var curretWeightStorage: Float) {
    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : ColdStorageServiceState{
            return gson.fromJson(str, ColdStorageServiceState::class.java)
        }
    }
    fun updateBoxWeight(value : Float){
        curretWeightStorage = curretWeightStorage.plus(value)
    }

    fun getCurrentBoxWeight() : Float{
        return curretWeightStorage
    }

    fun canStore(value: Double) : Boolean {
        return (curretWeightStorage+value <= curretWeightStorage)
    }

    fun toJsonString() : String{
        return gson.toJson(this)
    }
}
fun String.toColdStorageServiceState() : ColdStorageServiceState{
    return ColdStorageServiceState.fromJsonString(this)
}
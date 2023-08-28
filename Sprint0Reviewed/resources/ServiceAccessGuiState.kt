package resources
import com.google.gson.Gson
import org.json.*

//TODO: inserire classe ticket
data class ServiceAccessGuiState(private var FW: Float, private var Ticket : String) {
    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : ServiceAccessGuiState{
            return gson.fromJson(str, ServiceAccessGuiState::class.java)
        }
    }
    fun updateFW(value : Float){
        this.FW = FW
    }

    fun getFW(value: Float): Float {
        return FW
    }
    fun getTicket(): String {
        return Ticket
    }

    fun toJsonString() : String{
        return gson.toJson(this)
    }
}
fun String.ServiceAccessGuiState() : ServiceAccessGuiState{
    return ServiceAccessGuiState.fromJsonString(this)
}
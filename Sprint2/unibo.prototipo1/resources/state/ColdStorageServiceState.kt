package state

import com.google.gson.Gson


enum class CurrStateCSS  {
    IDLE,REQUESTEVAL,ACCEPTREQ,REJECTREQ,CHARGED,REMOVEEXPIREDTICKET,SENDINVALIDTICKET,TICKETEVAL
}
data class ColdStorageServiceState(
        private var currState: CurrStateCSS = CurrStateCSS.IDLE,
) {
    fun setCurrState(state: CurrStateCSS) {
        currState = state
    }

    fun getCurrState(): CurrStateCSS {
        return currState
    }

    companion object {
        private val gson = Gson()
        fun fromJsonString(str: String): ColdStorageServiceState {
            return gson.fromJson(str, ColdStorageServiceState::class.java)
        }
    }

    fun toJsonString(): String {
        return gson.toJson(this)
    }
}
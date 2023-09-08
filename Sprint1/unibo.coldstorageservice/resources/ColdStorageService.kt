package resources

import cli.System.TimeSpan
import com.google.gson.Gson
import org.json.JSONObject
import resources.model.Ticket
import state.TransportTrolleyState
import java.io.File

class ColdStorageService {
    data class ColdStorageService(
        private var MAXW:Double = 0.0,
        private val TICKETTIME: TimeSpan = TimeSpan(1000),
        private var currentWeightStorage : Double = 0.0,
        private val ticketList : ArrayList<Ticket> = arrayListOf(),
        private var ticketNumber: Int = 0,
        private var rejectedRequestCounter: Int = 0
    ){
//        private var instance: resources.ColdStorageService? = null
//
//        private fun getInstance() =
//            instance ?: synchronized(this) {
//                instance ?: ColdStorageService_old().also { instance = it }
//            }

        //val gson = Gson()
        // read from config
        init{
            val config = File("coldServiceSystemConfig.json")
            val jsonObject = JSONObject(config)
            MAXW = jsonObject.getDouble("MAXW")
        }
        companion object{
            private val gson = Gson()
            fun fromJsonString(str : String) : resources.ColdStorageService {
                return gson.fromJson(str, resources.ColdStorageService::class.java)
            }
        }

        fun getMAXW(): Double {
            return MAXW
        }

        fun getTICKETTIME(): TimeSpan {
            return TICKETTIME
        }

        fun getCurrentWeightStorage(): Double {
            return currentWeightStorage
        }

        fun setCurrentWeightStorage(currentWeightStorage: Double) {
            this.currentWeightStorage = currentWeightStorage
        }

        fun getTicketList(): ArrayList<Ticket> {
            return ticketList
        }

        fun incrementTicketNumber() {
            ticketNumber++
        }

        fun getTicketNumber(): Int {
            return ticketNumber
        }

        private fun resetTicketNumber() {
            ticketNumber = 0
        }

        private fun resetTicketList() {
           ticketList.clear()
        }

        private fun resetCurrentWeightStorage() {
            currentWeightStorage = 0.0
        }

        fun resetAll() {
            resetTicketNumber()
            resetTicketList()
            resetCurrentWeightStorage()
        }

        fun canStore(requestWeightToStore: Double): Boolean{
            return (requestWeightToStore + getCurrentWeightStorage() <= getMAXW())
        }

        fun getTicketById(id: Int): Ticket? {
            return getTicketList().find { it.id == id }
        }

        fun evaluateTicket(ticketId: Int): TicketEvaluationResponse{
            var ticket : Ticket? = getTicketById(ticketId) ?: return TicketEvaluationResponse.INVALID
            if (ticket != null && ticket.isExpired()) {
                return TicketEvaluationResponse.EXPIRED
            }
            return TicketEvaluationResponse.VALID
        }

        private fun addTicket(ticket: Ticket) {
            getTicketList().add(ticket)
        }

        private fun removeTicket(ticket: Ticket) {
            getTicketList().remove(ticket)
        }

        fun getRejectedRequestCounter(): Int {
            return rejectedRequestCounter
        }

        fun incrementRejectedRequestCounter() {
            rejectedRequestCounter++
        }

        fun toJsonString() : String{
            return gson.toJson(this)
        }

        fun fromJson(json: String): ColdStorageService_old {
            return ColdStorageService_old.gson.fromJson(json, ColdStorageService_old::class.java)
        }

        override fun toString(): String {
            return "ColdStorageService [MAXW=${getMAXW()}, TICKETTIME=${getTICKETTIME()}, currentWeightStorage=${getCurrentWeightStorage()}, ticketList=${getTicketList()}, ticketNumber=${getTicketNumber()}, rejectedRequestCounter=${getRejectedRequestCounter()}]"
        }

    }
}
package resources

import com.google.gson.Gson
import resources.model.Ticket


class ColdStorageService {
    private var MAXW : Double = 100.0
    private var DLIMIT : Int =0
    private val TICKETTIME: Long = 3
    private var currentWeightStorage : Double = 0.0
    private val ticketList : ArrayList<Ticket> = arrayListOf()
    private var ticketNumber: Int = 0
    private var rejectedRequestCounter: Int = 0


    companion object {

        private var instance: ColdStorageService? = null

        private fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ColdStorageService().also { instance = it }
            }

        val gson = Gson()

        fun getMAXW(): Double {
            //println(getInstance().json)
            return getInstance().MAXW
        }

        fun getDLIMIT(): Int {
            //getInstance().DLIMIT = json.getInt("DLIMIT")
            return getInstance().DLIMIT
        }

        fun getTICKETTIME(): Long {
            return getInstance().TICKETTIME
        }

        fun getCurrentWeightStorage(): Double {
            return getInstance().currentWeightStorage
        }

        fun setCurrentWeightStorage(currentWeightStorage: Double) {
            getInstance().currentWeightStorage = currentWeightStorage
        }

        fun getTicketList(): ArrayList<Ticket> {
            return getInstance().ticketList
        }

        fun incrementTicketNumber() {
            getInstance().ticketNumber++
        }

        fun getTicketNumber(): Int {
            return getInstance().ticketNumber
        }

        private fun resetTicketNumber() {
            getInstance().ticketNumber = 0
        }

        private fun resetTicketList() {
            getInstance().ticketList.clear()
        }

        private fun resetCurrentWeightStorage() {
            getInstance().currentWeightStorage = 0.0
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

        private fun getRejectedRequestCounter(): Int {
            return getInstance().rejectedRequestCounter
        }

        fun incrementRejectedRequestCounter() {
            getInstance().rejectedRequestCounter++
        }

        fun toJsonString() : String{
            return gson.toJson(this)
        }

        fun fromJson(json: String): ColdStorageService {
            return gson.fromJson(json, ColdStorageService::class.java)
        }



        override fun toString(): String {
            return "ColdStorageService [MAXW=${getMAXW()}, TICKETTIME=${getTICKETTIME()}, currentWeightStorage=${getCurrentWeightStorage()}, ticketList=${getTicketList()}, ticketNumber=${getTicketNumber()}, rejectedRequestCounter=${getRejectedRequestCounter()}]"
        }

    }
}

/*fun main(){
    println(ColdStorageService.getTICKETTIME())
}*/
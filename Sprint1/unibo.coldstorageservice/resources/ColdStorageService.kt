package resources

import cli.System.TimeSpan
import com.google.gson.Gson
import it.unibo.coldstorageservice.model.Ticket

class ColdStorageService private constructor(){

    private val MAXW : Double = 100.0
    private val TICKETTIME: TimeSpan = TimeSpan(1000)
    private var currentWeightStorage : Double = 0.0
    private val ticketList : ArrayList<Ticket> = arrayListOf()
    private var ticketNumber: Int = 0;
    private var rejectedRequestCounter: Int = 0;

    companion object {

        @Volatile
        private var instance: ColdStorageService? = null

        private fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: ColdStorageService().also { instance = it }
                }

        val gson = Gson()

        fun getMAXW(): Double {
            return getInstance().MAXW
        }

        fun getTICKETTIME(): TimeSpan {
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

        fun updateTicketNumber() {
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

        private fun getTicketById(id: Int): Ticket? {
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
            return getInstance().rejectedRequestCounter
        }

        fun incrementRejectedRequestCounter() {
            getInstance().rejectedRequestCounter++
        }

        fun toJsonString() : String{
            return ColdStorageServiceState.gson.toJson(this)
        }

        fun fromJson(json: String): ColdStorageService {
            return ColdStorageServiceState.gson.fromJson(json, ColdStorageService::class.java)
        }

        override fun toString(): String {
            return "ColdStorageService [MAXW=${getMAXW()}, TICKETTIME=${getTICKETTIME()}, currentWeightStorage=${getCurrentWeightStorage()}, ticketList=${getTicketList()}, ticketNumber=${getTicketNumber()}, rejectedRequestCounter=${getRejectedRequestCounter()}]"
        }

    }
}
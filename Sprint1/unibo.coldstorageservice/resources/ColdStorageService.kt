package resources

import it.unibo.coldstorageservice.model.Ticket

class ColdStorageService private constructor(){

    private val MAXW : Double = 100.0
    private val TICKETTIME: Int = 10000
    private var currentWeightStorage : Double = 0.0
    private val ticketList : ArrayList<Ticket> = arrayListOf<Ticket>()
    private var ticketNumber = 0;

    companion object {

        @Volatile
        private var instance: ColdStorageService? = null

        private fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: ColdStorageService().also { instance = it }
                }

        fun getMAXW(): Double {
            return MAXW
        }

        fun getTICKETTIME(): Int {
            return TICKETTIME
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

    }
}
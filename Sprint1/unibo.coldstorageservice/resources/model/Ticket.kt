package resources.model

import cli.System.DateTime
import cli.System.TimeSpan

class Ticket(id: Int, ticketTime: TimeSpan) {
    // TODO: mettere anche il peso?
    val id: Int
    private val ticketTime: DateTime
    private var isExpired: Boolean = false

    init {
        this.id = id
        this.ticketTime = DateTime.op_Addition(DateTime.get_Now(), ticketTime)
    }

    // Getters and setters
    fun getTicketId(): Int {
        return id
    }
    fun getTicketTime(): DateTime {
        return ticketTime
    }
    fun isExpired(): Boolean {
        isExpired = DateTime.get_Now() > ticketTime
        return isExpired
    }
    override fun toString(): String {
        return "Ticket [id=$id, ticketTime=$ticketTime, isExpired=$isExpired]"
    }
}
package resources.model

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class Ticket(id: Int, ticketTime: Long) {
    val id: Int
    private val ticketTime: LocalDateTime
    private var isExpired: Boolean = false

    init {
        this.id = id
        this.ticketTime = LocalDateTime.now().plus(ticketTime,ChronoUnit.SECONDS)
    }

    // Getters and setters
    fun getTicketId(): Int {
        return id
    }
    fun getTicketTime(): LocalDateTime {
        return ticketTime
    }
    fun isExpired(): Boolean {
        isExpired = LocalDateTime.now() > ticketTime
        return isExpired
    }
    override fun toString(): String {
        return "Ticket [id=$id, ticketTime=$ticketTime, isExpired=$isExpired]"
    }
}
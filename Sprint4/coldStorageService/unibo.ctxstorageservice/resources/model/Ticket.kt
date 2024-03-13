package resources.model

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class Ticket(id: Int, ticketTime: Long,weight : Double) {
    val id: Int
    private val ticketTime: LocalDateTime
    private var isExpired: Boolean = false
    private val weight : Double

    init {
        this.id = id
        this.ticketTime = LocalDateTime.now().plus(ticketTime,ChronoUnit.SECONDS)
        this.weight = weight
    }

    // Getters and setters
    fun getTicketId(): Int {
        return id
    }
    fun getTicketTime(): LocalDateTime {
        return ticketTime
    }

    fun getWeight(): Double {
        return weight
    }

    fun controlExpired(): Boolean{
        isExpired = LocalDateTime.now() > ticketTime
        return isExpired
    }


    fun isExpired(): Boolean {
        return isExpired
    }
    override fun toString(): String {
        return "Ticket [id=$id, ticketTime=$ticketTime, isExpired=$isExpired]"
    }


}
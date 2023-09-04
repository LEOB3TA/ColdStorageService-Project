package it.unibo.coldstorageservice.model

import cli.System.DateTime
import cli.System.TimeSpan


class Ticket(id: Int, ticketTime: TimeSpan) {
    // Create Ticket class with id, ticketTime and isExpired
    val id : Int
    private val ticketTime: DateTime
    private var isExpired: Boolean = false

    // Constructor
    init {
        this.id = id
        this.ticketTime = DateTime.op_Addition(DateTime.get_Now(), ticketTime)
    }

    // Getters and setters
    fun getId(): Int {
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
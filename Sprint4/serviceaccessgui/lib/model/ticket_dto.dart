class TicketDTO {
  final int ticketNumber;

  TicketDTO({required this.ticketNumber});

  factory TicketDTO.fromJson(Map<String, dynamic> json) {
    return TicketDTO(
      ticketNumber: json['ticketNumber'],
    );
  }

  Map<String, dynamic> toJson() => {
        'ticketNumber': ticketNumber,
      };
}

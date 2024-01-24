class TicketResponseDTO {
  final int ticketNumber;

  TicketResponseDTO({required this.ticketNumber});

  factory TicketResponseDTO.fromJson(Map<String, dynamic> json) {
    return TicketResponseDTO(
      ticketNumber: json['ticketNumber'],
    );
  }

  Map<String, dynamic> toJson() => {
        'ticketNumber': ticketNumber,
      };
}

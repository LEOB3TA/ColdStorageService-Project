import 'package:servicestatusgui/model/position.dart';

class ServiceStatusDTO {
  double currentWeight;
  double maxWeight;
  String status;
  Position position;
  String stringPosition;
  int rejectedRequests;
  int ticketNumber;
  String ticketStatus;

  ServiceStatusDTO({
    this.currentWeight = 0.0,
    this.maxWeight = 10.0,
    this.status = 'STOPPED',
    this.position = const Position(x: 0, y: 0),
    this.rejectedRequests = 0,
    this.ticketNumber = 0,
    this.ticketStatus = 'INVALID',
    this.stringPosition = 'HOME'
  });

  double get getCurrentWeight => currentWeight;
  String get getStatus => status;
  Position get getPosition => position;
  int get getRejectedRequests => rejectedRequests;

  @override
  String toString() {
    return 'ServiceStatusDTO{currentWeight: $currentWeight, status: $status, position: $position, rejectedRequests: $rejectedRequests}';
  }

  factory ServiceStatusDTO.fromJson(Map<String, dynamic> json) {
    return ServiceStatusDTO(
      currentWeight: json["CurrW"],
      maxWeight: json["MAXW"],
      status: json["act"],
      position: Position.fromJson(json["ttPos"]),
      rejectedRequests: json["rejected"],
      ticketNumber: json['ticketN'],
      ticketStatus: json['TicketStatus'],
      stringPosition: json['pos']
    );
  }

  Map<String, dynamic> toJson() => {
        "CurrW": currentWeight,
    "MAXW": maxWeight,
        "act": status,
        "ttPos": position,
        "rejected": rejectedRequests,
        "ticketN" : ticketNumber,
        "TicketStatus": ticketStatus,
        "pos" : stringPosition,
      };
}

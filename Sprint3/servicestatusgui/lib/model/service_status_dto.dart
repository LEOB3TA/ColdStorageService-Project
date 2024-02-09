import 'package:servicestatusgui/model/position.dart';

class ServiceStatusDTO {
  double currentWeight;
  double maxW;
  String status;
  Position position;
  int rejectedRequests;

  ServiceStatusDTO({
    this.currentWeight = 0.0,
    this.maxW = 0.0,
    this.status = 'STOPPED',
    this.position = const Position(x: 0, y: 0),
    this.rejectedRequests = 0,
  });

  double get getCurrentWeight => currentWeight;
  double get getMaxWeigth =>maxW;
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
      maxW : json["MAXW"],
      status: json["act"],
      position: Position.fromJson(json["ttPos"]),
      rejectedRequests: json["rejected"],
    );
  }

  Map<String, dynamic> toJson() => {
        "CurrW": currentWeight,
        "act": status,
        "ttPos": position,
        "rejected": rejectedRequests,
      };
}

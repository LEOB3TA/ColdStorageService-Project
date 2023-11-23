import 'package:servicestatusgui/model/position.dart';

class ServiceStatusDTO {
  final double currentWeight;
  final String status;
  final Position position;
  final int rejectedRequests;

  ServiceStatusDTO({
    this.currentWeight = 0.0,
    this.status = 'Stopped',
    this.position = const Position(x: 0, y: 0),
    this.rejectedRequests = 0,
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
      currentWeight: json["currentWeight"],
      status: json["status"],
      position: Position.fromJson(json["position"]),
      rejectedRequests: json["rejectedRequests"],
    );
  }

  Map<String, dynamic> toJson() => {
        "currentWeight": currentWeight,
        "status": status,
        "position": position,
        "rejectedRequests": rejectedRequests,
      };
}

import 'package:servicestatusgui/model/position.dart';

class ServiceStatusDTO {
  final double currentWeight;
  final double maxWeight;
  final String status;
  final Position position;
  final int rejectedRequests;

  ServiceStatusDTO({
    this.currentWeight = 0.0,
    this.maxWeight = 10.0,
    this.status = 'Home',
    this.position = const Position(x: 0, y: 0),
    this.rejectedRequests = 0,
  });

  double get getCurrentWeight => currentWeight;
  double get getMaxWeight => maxWeight;
  String get getStatus => status;
  Position get getPosition => position;
  int get getRejectedRequests => rejectedRequests;

  @override
  String toString() {
    return 'ServiceStatusDTO{currentWeight: $currentWeight, maxWeight: $maxWeight, status: $status, position: $position, rejectedRequests: $rejectedRequests}';
  }

  factory ServiceStatusDTO.fromJson(Map<String, dynamic> json) {
    return ServiceStatusDTO(
      currentWeight: json["currentWeight"],
      maxWeight: json["maxWeight"],
      status: json["status"],
      position: Position.fromJson(json["position"]),
      rejectedRequests: json["rejectedRequests"],
    );
  }
}

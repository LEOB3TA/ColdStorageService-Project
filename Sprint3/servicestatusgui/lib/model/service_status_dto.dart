import 'package:servicestatusgui/model/position.dart';

class ServiceStatusDTO {
  final double currentWeight;
  final double maxWeight;
  final String status;
  final Position position;
  final int rejectedRequests;
  final int rows;
  final int cols;
  final List<Position> home ;
  final List<Position> indoor ;
  final List<Position> coldroom;

  ServiceStatusDTO({
    this.currentWeight = 0.0,
    this.maxWeight = 10.0,
    this.status = 'Home',
    this.position = const Position(x: 0, y: 0),
    this.rejectedRequests = 0,
    this.cols =0,
    this.rows=0,
    // TODO: check initialization
    this.home = const [],
    this.indoor= const [],
    this.coldroom = const [],
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
      maxWeight: json["maxWeight"],
      currentWeight: json["currentWeight"],
      //status: json["status"], not used
      rows: json["rows"],
      cols: json["cols"],
      home: json["roomHome"]! as List<Position>,
      indoor: json["roomIndoor"] as List<Position>,
      coldroom: json["roomColdroom"] as List<Position>,
      //position: Position.fromJson(json["position"]),
      //rejectedRequests: json["rejectedRequests"],
    );
  }
}

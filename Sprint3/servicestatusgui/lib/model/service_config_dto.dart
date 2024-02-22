import 'package:servicestatusgui/model/position.dart';

class ServiceConfigDTO {
  double maxWeight;
  int rows;
  int cols;
  List<Position> home;
  List<Position> indoor;
  List<Position> coldRoom;

  ServiceConfigDTO({
    this.maxWeight = 10.0,
    this.rows = 5,
    this.cols = 7,
    this.home = const [Position(x: 0, y: 0)],
    this.indoor = const [Position(x: 4, y: 0), Position(x: 4, y: 1), Position(x: 4, y: 2)],
    this.coldRoom = const [Position(x: 1, y: 4), Position(x: 1, y: 5), Position(x: 2, y: 4), Position(x: 2, y: 5)],
  });

  double get getMaxWeight => maxWeight;
  int get getRows => rows;
  int get getCols => cols;
  List<Position> get getHome => home;
  List<Position> get getIndoor => indoor;
  List<Position> get getColdRoom => coldRoom;

  @override
  String toString() {
    return 'ServiceConfigDTO{maxWeight: $maxWeight, rows: $rows, cols: $cols, home: $home, indoor: $indoor, coldRoom: $coldRoom}';
  }

  factory ServiceConfigDTO.fromJson(Map<String, dynamic> json) {
    return ServiceConfigDTO(
      maxWeight: json["maxWeight"],
      rows: json["rows"],
      cols: json["cols"],
      home: List<Position>.from(json["roomHome"].map((x) => Position.fromJson(x))),
      indoor: List<Position>.from(json["roomIndoor"].map((x) => Position.fromJson(x))),
      coldRoom: List<Position>.from(json["roomColdRoom"].map((x) => Position.fromJson(x))),
    );
  }

  Map<String, dynamic> toJson() => {
        "maxWeight": maxWeight,
        "rows": rows,
        "cols": cols,
        "roomHome": home,
        "roomIndoor": indoor,
        "roomColdRoom": coldRoom,
      };
}

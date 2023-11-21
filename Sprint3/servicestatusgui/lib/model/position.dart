class Position {
  final int x;
  final int y;

  const Position({this.x = 0, this.y = 0});

  int get getX => x;
  int get getY => y;

  @override
  String toString() {
    return 'Position{x: $x, y: $y}';
  }

  // fromJson function
  factory Position.fromJson(Map<String, dynamic> json) {
    return Position(
      x: json["x"],
      y: json["y"],
    );
  }

  // fromJson function
  Map<String, dynamic> toJson() => {
        "x": x,
        "y": y,
      };
}

import 'package:ServiceAccessGUI/model/weight_dto.dart';

class WeightDTO {
  double maxWeight;
  double curr;

  WeightDTO({
    this.maxWeight = 10.0,
    this.curr = 0.0,
  });

  double get getMaxWeight => maxWeight;
  double get getCurr => curr;
   @override
  String toString() {
    return 'ServiceConfigDTO{maxWeight: $maxWeight, current: $curr}';
  }

  factory WeightDTO.fromJson(Map<String, dynamic> json) {
    return WeightDTO(
      maxWeight: json["MAXW"],
      curr: json['CurrW'],
    );
  }

  Map<String, dynamic> toJson() => {
    "maxWeight": maxWeight,
    "curr":curr,
  };
}

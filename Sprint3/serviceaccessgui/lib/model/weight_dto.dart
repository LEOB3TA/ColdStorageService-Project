class WeightDTO {
  double maxWeight;
  double currentWeight;

  WeightDTO({
    this.maxWeight = 10.0,
    this.currentWeight = 0.0,
  });

  double get getMaxWeight => maxWeight;
  double get getCurrentWeight => currentWeight;
  double get percent => (currentWeight / maxWeight);

   @override
  String toString() {
    return 'ServiceConfigDTO{maxWeight: $maxWeight, currentWeight: $currentWeight}';
  }

  factory WeightDTO.fromJson(Map<String, dynamic> json) {
    return WeightDTO(
      maxWeight: json["maxWeight"],
      currentWeight: json['currentWeight'],
    );
  }

  Map<String, dynamic> toJson() => {
    "MAXW": maxWeight,
    "CurrW":currentWeight,
  };
}

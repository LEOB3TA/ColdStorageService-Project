class StoreFoodRequestDTO {
  final double quantity;

  StoreFoodRequestDTO({required this.quantity});

  factory StoreFoodRequestDTO.fromJson(Map<String, dynamic> json) {
    return StoreFoodRequestDTO(
      quantity: json['quantity'],
    );
  }

  Map<String, dynamic> toJson() => {
        'quantity': quantity,
      };
}

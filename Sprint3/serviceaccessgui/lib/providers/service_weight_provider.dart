import 'package:ServiceAccessGUI/model/weight_dto.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final serviceWeightProvider = StateProvider<WeightDTO>((ref) => WeightDTO());
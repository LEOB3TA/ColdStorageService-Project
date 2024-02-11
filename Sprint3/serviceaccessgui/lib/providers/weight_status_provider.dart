import 'package:ServiceAccessGUI/model/weight_dto.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final weightStatusProvider = StateProvider<WeightDTO>((ref) => WeightDTO());
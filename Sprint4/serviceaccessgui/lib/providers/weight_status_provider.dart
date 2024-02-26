import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:service_access_gui/model/weight_dto.dart';

final weightStatusProvider = StateProvider<WeightDTO>((ref) => WeightDTO());

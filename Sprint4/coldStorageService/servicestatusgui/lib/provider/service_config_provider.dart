import 'package:riverpod/riverpod.dart';
import 'package:servicestatusgui/model/service_config_dto.dart';

final serviceConfigProvider = StateProvider<ServiceConfigDTO>((ref) => ServiceConfigDTO());

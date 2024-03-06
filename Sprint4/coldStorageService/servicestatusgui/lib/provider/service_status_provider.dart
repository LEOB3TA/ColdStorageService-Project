import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:servicestatusgui/model/service_status_dto.dart';

final serviceStatusProvider = StateProvider<ServiceStatusDTO>((ref) => ServiceStatusDTO());

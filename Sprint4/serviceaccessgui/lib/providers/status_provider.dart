import 'package:flutter_riverpod/flutter_riverpod.dart';

enum StatusEnum { /*toConnect,*/ ticketRequest, ticketResponse, depositTicket, result }

final statusEnumProvider = StateProvider<StatusEnum>((ref) => StatusEnum.ticketRequest);

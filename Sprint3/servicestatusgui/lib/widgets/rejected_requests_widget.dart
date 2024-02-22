import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';

import 'package:servicestatusgui/globals.dart';
import 'package:servicestatusgui/provider/service_status_provider.dart';
import 'package:servicestatusgui/widgets/layout/spaced_column.dart';

class RejectedRequestsWidget extends ConsumerWidget {
  const RejectedRequestsWidget({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final status = ref.watch(serviceStatusProvider);
    return SizedBox(
      height: double.maxFinite,
      child: DecoratedBox(
        decoration: BoxDecoration(
          borderRadius: const BorderRadius.all(Radius.circular(8)),
          color: Globals.backgroundWidgetColor,
        ),
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
          child: SpacedColumn(
            spacing: 16,
            children: [
              Expanded(
                flex: 1,
                child: Text(
                  'REJECTED REQUESTS',
                  maxLines: 2,
                  textAlign: TextAlign.center,
                  style: GoogleFonts.roboto(
                      textStyle: TextStyle(
                          fontWeight: FontWeight.w800, color: Globals.headerColor, fontSize: 24)),
                ),
              ),
              Expanded(
                flex: 4,
                child: FittedBox(
                  fit: BoxFit.cover,
                  child: Text(
                    status.getRejectedRequests.toString(),
                    style: const TextStyle(fontSize: 96, color: Colors.black54),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

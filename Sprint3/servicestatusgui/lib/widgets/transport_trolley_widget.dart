import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:servicestatusgui/globals.dart';
import 'package:servicestatusgui/widgets/layout/spaced_column.dart';

import '../provider/service_status_provider.dart';

class TransportTrolleyWidget extends ConsumerWidget {
  const TransportTrolleyWidget({super.key});

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
                child: FittedBox(
                  fit: BoxFit.fitWidth,
                  child: Text(
                    'Transport Trolley',
                    textAlign: TextAlign.center,
                    style: GoogleFonts.roboto(
                        textStyle: const TextStyle(
                            fontWeight: FontWeight.w800, color: Colors.blueAccent)),
                  ),
                ),
              ),
              Expanded(
                flex: 2,
                child: SpacedColumn(
                    spacing: 0,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Expanded(
                        child: FittedBox(
                          fit: BoxFit.cover,
                          child: Text(
                            'STATE',
                            style: TextStyle(
                                color: Colors.black26, fontWeight: FontWeight.w900),
                          ),
                        ),
                      ),
                      Expanded(
                        flex: 2,
                        child: FittedBox(
                          fit: BoxFit.cover,
                          child: Text(
                            status.getStatus,
                            style: const TextStyle(
                                fontSize: 36, color: Colors.black54),
                          ),
                        ),
                      ),
                    ]),
              ),
              Expanded(
                flex: 2,
                child: SpacedColumn(
                    spacing: 0,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Expanded(
                        child: FittedBox(
                          fit: BoxFit.cover,
                          child: Text(
                            'POSITION',
                            style: TextStyle(
                                color: Colors.black26, fontWeight: FontWeight.w900),
                          ),
                        ),
                      ),
                      Expanded(
                        flex: 2,
                        child: FittedBox(
                          fit: BoxFit.cover,
                          child: Text(
                            '(${status.getPosition.getX}, ${status.getPosition.getY})',
                            style: const TextStyle(
                                fontSize: 36, color: Colors.black54),
                          ),
                        ),
                      ),
                    ]),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

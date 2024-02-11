import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';
import 'package:servicestatusgui/provider/service_status_provider.dart';

import '../globals.dart';
import 'layout/spaced_column.dart';

class CurrentWeightWidget extends ConsumerWidget {
  const CurrentWeightWidget({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final serviceStatus = ref.watch(serviceStatusProvider);
    final progressColor = serviceStatus.percentage < 0.5
        ? Colors.green
        : serviceStatus.percentage < 0.8
        ? Colors.orange
        : Colors.red;

    return SizedBox(
      width: double.maxFinite,
      child: DecoratedBox(
          decoration: BoxDecoration(
            borderRadius: const BorderRadius.all(Radius.circular(16)),
            color: Globals.backgroundWidgetColor,
          ),
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: SpacedColumn(
              spacing: 8,
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  child: Text(
                    'CURRENT WEIGHT',
                    style: GoogleFonts.inter(
                        textStyle: TextStyle(
                            fontWeight: FontWeight.w600, color: Colors.blue.shade900, fontSize: 24)),
                  ),
                ),
                Expanded(
                    flex: 3,
                    child: FittedBox(
                      fit: BoxFit.contain,
                      child: CircularPercentIndicator(
                        radius: 96,
                        animation: true,
                        lineWidth: 16,
                        percent: serviceStatus.percentage,
                        center: RichText(
                          textAlign: TextAlign.center,
                          text: TextSpan(
                            text: 'KG\n',
                            style: const TextStyle(
                                fontWeight: FontWeight.w600, fontSize: 16, color: Colors.black26),
                            children: <TextSpan>[
                              TextSpan(
                                  text: '${serviceStatus.getCurrentWeight}\n',
                                  style: TextStyle(
                                      letterSpacing: 0,
                                      fontWeight: FontWeight.w600,
                                      fontSize: 32,
                                      color: progressColor)),
                              TextSpan(
                                  text: 'Out of ${serviceStatus.getMaxWeight} kg',
                                  style: const TextStyle(
                                      fontWeight: FontWeight.w400,
                                      fontSize: 16,
                                      color: Colors.black38)),
                            ],
                          ),
                        ),
                        progressColor: progressColor,
                        backgroundColor: Colors.grey.shade200,
                        circularStrokeCap: CircularStrokeCap.round,
                      ),
                    )),
              ],
            ),
          )),
    );
  }

}

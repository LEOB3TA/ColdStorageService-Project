import 'package:ServiceAccessGUI/globals.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';

import 'layout/spaced_column.dart';
import '../providers/weight_status_provider.dart';

class CurrentWeightWidget extends ConsumerWidget {
  const CurrentWeightWidget({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final weightStatus = ref.watch(weightStatusProvider);
    final progressColor = weightStatus.percent < 0.5
        ? Colors.green
        : weightStatus.percent < 0.8
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
                            fontWeight: FontWeight.w600, color: Colors.blue.shade900, fontSize: Globals.headerWidgetFontSize)),
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
                        percent: weightStatus.percent,
                        center: RichText(
                          textAlign: TextAlign.center,
                          text: TextSpan(
                            text: 'KG\n',
                            style: const TextStyle(
                                fontWeight: FontWeight.w600, fontSize: 16, color: Colors.black26),
                            children: <TextSpan>[
                              TextSpan(
                                  text: '${weightStatus.getCurrentWeight}\n',
                                  style: TextStyle(
                                      letterSpacing: 0,
                                      fontWeight: FontWeight.w600,
                                      fontSize: 32,
                                      color: progressColor)),
                              TextSpan(
                                  text: 'Out of ${weightStatus.getMaxWeight} kg',
                                  style: const TextStyle(
                                      fontWeight: FontWeight.w400,
                                      fontSize: 16,
                                      color: Colors.black38)),
                            ],
                          ),
                        ),
                        progressColor: progressColor,
                        backgroundColor: Colors.blue.shade200,
                        circularStrokeCap: CircularStrokeCap.round,
                      ),
                    )),
              ],
            ),
          )),
    );
  }

}

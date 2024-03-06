import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:servicestatusgui/globals.dart';
import 'package:servicestatusgui/provider/service_config_provider.dart';

import 'package:servicestatusgui/widgets/layout/spaced_column.dart';
import 'package:skeletonizer/skeletonizer.dart';

import 'layout/map_grid.dart';

class GridWidget extends ConsumerWidget {
  const GridWidget({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final config = ref.watch(serviceConfigProvider);
    return SizedBox(
      height: double.maxFinite,
      width: double.maxFinite,
      child: DecoratedBox(
        decoration: BoxDecoration(
          borderRadius: const BorderRadius.all(Radius.circular(8)),
          color: Globals.backgroundWidgetColor,
        ),
        child: Padding(
            padding: const EdgeInsets.all(16),
            child: SpacedColumn(spacing: 16, children: [
              Expanded(
                child: Text(
                  'GRID',
                  textAlign: TextAlign.center,
                  style: GoogleFonts.inter(
                      textStyle: TextStyle(
                          fontWeight: FontWeight.w600, color: Globals.headerColor, fontSize: 24)),
                ),
              ),
              Expanded(
                  flex: 11,
                  child: MapGrid(
                      home: config.home,
                      indoor: config.indoor,
                      coldRoom: config.coldRoom,
                      rows: config.rows,
                      cols: config.cols,
                    ),
                  ),
            ])),
      ),
    );
  }
}

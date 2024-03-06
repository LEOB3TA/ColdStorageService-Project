import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:servicestatusgui/widgets/layout/pixel.dart';

import '../../model/position.dart';
import '../../provider/service_config_provider.dart';

class MapGrid extends StatelessWidget {
  const MapGrid(
      {super.key,
      required this.rows,
      required this.cols,
      required this.home,
      required this.indoor,
      required this.coldRoom});

  final int rows;
  final int cols;
  final List<Position> home;
  final List<Position> indoor;
  final List<Position> coldRoom;

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    final screenHeight = MediaQuery.of(context).size.height;
    return Consumer(
      builder: (_, WidgetRef ref, __) {
        final config = ref.watch(serviceConfigProvider);
        return SizedBox(
          width: screenWidth * 0.35,
          height: screenHeight * 0.3,
          child: GridView.builder(
              itemCount: config.rows * config.cols,
              physics: const NeverScrollableScrollPhysics(),
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: config.cols),
              itemBuilder: (context, index) {
                return Pixel(
                  row: index ~/ cols,
                  col: index % cols,
                );
              }),
        );
      },
    );
  }
}

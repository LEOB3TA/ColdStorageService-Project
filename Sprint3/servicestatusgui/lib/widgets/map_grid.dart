import 'package:flutter/material.dart';
import 'package:servicestatusgui/widgets/pixel.dart';

import '../model/position.dart';

class MapGrid extends StatefulWidget {
  const MapGrid({super.key, required this.currentPosition});

  final Position currentPosition;

  @override
  State<MapGrid> createState() => _MapGridState();
}

class _MapGridState extends State<MapGrid> {
  static const rowLength = 8;
  static const colLength = 6;
  //
  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    final screenHeight = MediaQuery.of(context).size.height;
    final currentIndex = widget.currentPosition.x + (widget.currentPosition.y * rowLength);
    return SizedBox(
      width: screenWidth * 0.35,
      height: screenHeight * 0.3,
      child: GridView.builder(
          itemCount: rowLength * colLength,
          physics: const NeverScrollableScrollPhysics(),
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: rowLength),
          itemBuilder: (context, index) {
            if (index == currentIndex) {
              // Current
              return const Pixel(
                color: Colors.yellow,
                value: 'X',
                robot: true,
              );
            }
            if (index == 0) {
              // Home
              return const Pixel(color: Colors.red, value: 'H');
            }
            if (index == 12 || index == 13 || index == 20 || index == 21) {
              // ColdRoom
              return const Pixel(color: Colors.green, value: 'C');
            }
            if (index == 40 || index == 41 || index == 42) {
              // Indoor
              return const Pixel(color: Colors.blue, value: 'I');
            }
            return const Pixel(color: Colors.grey, value: '');
          }),
    );
  }
}

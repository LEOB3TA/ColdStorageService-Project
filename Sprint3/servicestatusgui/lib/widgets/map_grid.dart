import 'package:flutter/material.dart';
import 'package:servicestatusgui/widgets/pixel.dart';

class MapGrid extends StatefulWidget {
  const MapGrid({super.key});

  @override
  State<MapGrid> createState() => _MapGridState();
}

class _MapGridState extends State<MapGrid> {
  static const gridWidth = 8;
  static const gridHeight = 6;
  //
  @override
  Widget build(BuildContext context) {
    return GridView.builder(
        itemCount: gridWidth * gridHeight,
        //physics: const NeverScrollableScrollPhysics(),
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: gridWidth),
        itemBuilder: (context, index) {
          if (index == 0) {
            // Home
            return Pixel(color: Colors.red, value: 'H');
          }
          if (index == 12 || index == 13 || index == 20 || index == 21) {
            // ColdRoom
            return Pixel(color: Colors.green, value: 'C');
          }
          if (index == 40 || index == 41 || index == 42) {
            // Indoor
            return Pixel(color: Colors.blue, value: 'I');
          }
          return Pixel(color: Colors.grey, value: '');
        });
  }
}

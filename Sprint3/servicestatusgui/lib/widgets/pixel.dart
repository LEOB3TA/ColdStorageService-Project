import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../model/position.dart';
import '../provider/service_config_provider.dart';
import '../provider/service_status_provider.dart';

class Pixel extends StatelessWidget {
  final bool robot;
  final int row;
  final int col;
  const Pixel({super.key, this.robot = false, required this.row, required this.col});

  @override
  Widget build(BuildContext context) {
    Position position = Position(x: row, y: col);
    MaterialColor? color;
    String value;
    return Consumer(builder: (_, WidgetRef ref, __) {
      final config = ref.watch(serviceConfigProvider);
      final currentPos = ref.watch(serviceStatusProvider).position;
      if (config.getHome.contains(position)) {
        color = Colors.green;
        value = 'H';
      } else if (config.getIndoor.contains(position)) {
        color = Colors.blue;
        value = 'I';
      } else if (config.getColdRoom.contains(position)) {
        color = Colors.red;
        value = 'C';
      } else {
        color = null;
        value = '';
      }
      return Padding(
        padding: const EdgeInsets.all(2),
        child: SizedBox.square(
          dimension: 8,
          child: DecoratedBox(
            decoration: BoxDecoration(
              borderRadius: const BorderRadius.all(Radius.circular(4)),
              color: color != null ? color!.shade100 : Colors.grey.shade100,
            ),
            child: Padding(
              padding: const EdgeInsets.all(2),
              child: FittedBox(
                fit: BoxFit.contain,
                child: Stack(
                  children: [
                    Center(
                        child: Text(value,
                            style: TextStyle(
                                fontSize: 64,
                                fontWeight: FontWeight.bold,
                                color: color != null ? color!.shade400.withOpacity(0.5) : Colors.transparent))),
                    if (currentPos == position) const Image(image: AssetImage('assets/images/robot.png'))
                  ],
                ),
              ),
            ),
          ),
        ),
      );
    });
  }
}

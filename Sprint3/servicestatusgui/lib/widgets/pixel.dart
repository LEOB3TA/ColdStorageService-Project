import 'package:flutter/material.dart';

class Pixel extends StatelessWidget {
  final MaterialColor color;
  final String value;
  const Pixel({super.key, required this.color, required this.value});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(2),
      child: SizedBox.square(
        dimension: 32,
        child: DecoratedBox(
          decoration: BoxDecoration(
            borderRadius: const BorderRadius.all(Radius.circular(8)),
            color: color.shade100,
          ),
          child: Center(
              child: Text(
            value,
            style: TextStyle(fontSize: 64, fontWeight: FontWeight.bold, color: color.shade400.withOpacity(0.5)),
          )),
        ),
      ),
    );
  }
}

import 'package:ServiceAccessGUI/widgets/layout/spaced_column.dart';
import 'package:flutter/material.dart';

class TicketWidget extends StatelessWidget {
  const TicketWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: Alignment.center,
      children: [
        SizedBox(
          height: 200,
          width: 300,
          child: DecoratedBox(
            decoration: BoxDecoration(
              color: Colors.amber.shade200,
              borderRadius: BorderRadius.circular(4),
            ),
          ),
        ),
        SizedBox(
          height: 184,
          width: 284,
          child: DecoratedBox(
            decoration: BoxDecoration(
              color: Colors.amber.shade200,
              borderRadius: BorderRadius.circular(8),
              border: Border.all(
                color: Colors.amber.shade400,
                width: 4,
              ),
            ),
          ),
        ),
        Positioned(
          right: -32,
          child: SizedBox.square(
            dimension: 64,
            child: DecoratedBox(
              decoration: BoxDecoration(
                  color: Colors.blue.shade100, shape: BoxShape.circle),
            ),
          ),
        ),
        Positioned(
          left: -32,
          child: SizedBox.square(
            dimension: 64,
            child: DecoratedBox(
              decoration: BoxDecoration(
                  color: Colors.blue.shade100, shape: BoxShape.circle),
            ),
          ),
        ),
        Positioned.fill(
          top: 36,
          child: Align(
            alignment: Alignment.bottomCenter,
            child: SpacedColumn(
              spacing: 0,
              children: [
                const Text(
                  'Your Ticket is',
                  style: TextStyle(
                      fontWeight: FontWeight.w300,
                      fontSize: 12,
                      color: Colors.black45),
                ),
                Text('0',
                    style: TextStyle(
                        letterSpacing: -5,
                        fontWeight: FontWeight.w900,
                        fontSize: 96,
                        color: Colors.lime.shade900)),
              ],
            ),
          ),
        ),
      ],
    );
  }
}

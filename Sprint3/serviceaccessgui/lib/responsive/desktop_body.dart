import 'package:ServiceAccessGUI/widgets/authors_widget.dart';
import 'package:ServiceAccessGUI/widgets/current_weight_widget.dart';
import 'package:ServiceAccessGUI/widgets/layout/spaced_column.dart';
import 'package:ServiceAccessGUI/widgets/layout/spaced_row.dart';
import 'package:ServiceAccessGUI/widgets/stepper_widget.dart';
import 'package:flutter/material.dart';

class DesktopBody extends StatelessWidget {
  final String url;
  const DesktopBody({super.key, required this.url});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blue.shade50,
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SpacedColumn(spacing: 16, children: [
          Expanded(
            flex: 4,
            child: SpacedRow(spacing: 16, children: [
              Expanded(flex: 2, child: StepperWidget(url: url)),
              const Expanded(child: CurrentWeightWidget()),
            ],),
          ),

          const Expanded(child: AuthorsWidget()),

        ],),
      ),
    );
  }
}

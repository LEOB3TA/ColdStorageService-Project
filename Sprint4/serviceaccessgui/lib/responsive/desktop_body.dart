import 'package:flutter/material.dart';
import 'package:service_access_gui/widgets/authors_widget.dart';
import 'package:service_access_gui/widgets/current_weight_widget.dart';
import 'package:service_access_gui/widgets/layout/spaced_column.dart';
import 'package:service_access_gui/widgets/layout/spaced_row.dart';
import 'package:service_access_gui/widgets/stepper_widget.dart';

class DesktopBody extends StatelessWidget {
  final String url;
  const DesktopBody({super.key, required this.url});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blue.shade50,
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SpacedColumn(
          spacing: 16,
          children: [
            Expanded(
              flex: 4,
              child: SpacedRow(
                spacing: 16,
                children: [
                  Expanded(flex: 2, child: StepperWidget(url: url)),
                  const Expanded(child: CurrentWeightWidget()),
                ],
              ),
            ),
            const Expanded(child: AuthorsWidget()),
          ],
        ),
      ),
    );
  }
}

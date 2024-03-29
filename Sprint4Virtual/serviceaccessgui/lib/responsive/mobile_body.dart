import 'package:flutter/material.dart';
import 'package:service_access_gui/globals.dart';
import 'package:service_access_gui/widgets/authors_widget.dart';
import 'package:service_access_gui/widgets/current_weight_widget.dart';
import 'package:service_access_gui/widgets/layout/spaced_column.dart';
import 'package:service_access_gui/widgets/stepper_widget.dart';

class MobileBody extends StatelessWidget {
  final String url;
  const MobileBody({Key? key, required this.url}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Globals.backgroundColor,
      body: Padding(
        padding: const EdgeInsets.all(8),
        child: SpacedColumn(
          spacing: 8,
          children: [
            const Expanded(flex: 2, child: CurrentWeightWidget()),
            Expanded(flex: 6, child: StepperWidget(url: url)),
            const Expanded(child: AuthorsWidget()),
          ],
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';
import 'package:servicestatusgui/globals.dart';
import 'package:servicestatusgui/widgets/authors_widget.dart';
import 'package:servicestatusgui/widgets/current_weight_widget.dart';
import 'package:servicestatusgui/widgets/grid_widget.dart';
import 'package:servicestatusgui/widgets/layout/spaced_column.dart';
import 'package:servicestatusgui/widgets/layout/spaced_row.dart';
import 'package:servicestatusgui/widgets/rejected_requests_widget.dart';
import 'package:servicestatusgui/widgets/transport_trolley_widget.dart';


class MobileBody extends StatelessWidget {
  final String url;
  const MobileBody({super.key, required this.url});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Globals.backgroundColor,
      body: const Padding(
        padding: EdgeInsets.all(8),
        child: SpacedColumn(
            spacing: 8,
            children: [
              Expanded( flex: 2,
                child: SpacedRow(spacing: 8, children: [
                  Expanded(
                    child: TransportTrolleyWidget(),
                  ),
                  Expanded(
                    child: RejectedRequestsWidget(),
                  )
                ])),
            Expanded(flex: 4, child: SpacedRow(spacing: 8, children: [
              Expanded(child: GridWidget()), Expanded(child: CurrentWeightWidget()),
            ])),
              Expanded(
                  child: AuthorsWidget()),
            ]),
      ),
    );
  }
}


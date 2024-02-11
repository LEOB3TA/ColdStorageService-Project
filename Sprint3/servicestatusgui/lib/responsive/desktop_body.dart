import 'package:flutter/material.dart';
import 'package:servicestatusgui/globals.dart';
import 'package:servicestatusgui/widgets/authors_widget.dart';
import 'package:servicestatusgui/widgets/current_weight_widget.dart';
import 'package:servicestatusgui/widgets/grid_widget.dart';

import 'package:servicestatusgui/widgets/layout/spaced_column.dart';
import 'package:servicestatusgui/widgets/layout/spaced_row.dart';
import 'package:servicestatusgui/widgets/rejected_requests_widget.dart';
import 'package:servicestatusgui/widgets/transport_trolley_widget.dart';

class DesktopBody extends StatelessWidget {
  final String url;
  const DesktopBody({super.key, required this.url});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Globals.backgroundColor,
      body: const Padding(
        padding: EdgeInsets.all(16),
        child: SpacedColumn(
          spacing: 16,
          children: [
            Expanded(
              flex: 7,
              child: SpacedRow(
              spacing: 8,
              children: [
                Expanded(
                    child: SizedBox(
                      height: double.maxFinite,
                      width: double.maxFinite,
                      child: DecoratedBox(
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.all(Radius.circular(8)),
                          //color: Colors.grey.shade200,
                        ),
                        child: SpacedColumn(
                          spacing: 8,
                          children: [
                            Expanded(
                                child: CurrentWeightWidget()),
                            Expanded(
                                child: SizedBox(
                                  height: double.maxFinite,
                                  width: double.maxFinite,
                                  child: DecoratedBox(
                                      decoration: BoxDecoration(
                                        borderRadius: BorderRadius.all(Radius.circular(8)),
                                      ),
                                      child: SpacedRow(spacing: 8, children: [
                                        Expanded(
                                          child: TransportTrolleyWidget(),
                                        ),
                                        Expanded(
                                          child: RejectedRequestsWidget(),
                                        )
                                      ])),
                                )),
                          ],
                        ),
                      ),
                    )),
                Expanded(
                    flex: 1,
                    child: GridWidget()),
              ],),
            ),
            Expanded(
              child: AuthorsWidget()),
          ]),
      ),
    );
  }
}

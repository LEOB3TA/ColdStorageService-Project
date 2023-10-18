import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';
import 'package:servicestatusgui/widgets/map_grid.dart';
import 'package:servicestatusgui/widgets/spaced_column.dart';
import 'package:servicestatusgui/widgets/spaced_row.dart';

class HomePageView extends StatefulWidget {
  const HomePageView({super.key});

  @override
  State<HomePageView> createState() => _HomePageViewState();
}

class _HomePageViewState extends State<HomePageView> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Center(
            child: Padding(
      padding: const EdgeInsets.all(16),
      child: SpacedColumn(
        spacing: 8,
        children: [
          Expanded(
              flex: 2,
              child: SizedBox(
                width: double.maxFinite,
                child: DecoratedBox(
                  decoration: const BoxDecoration(
                    borderRadius: BorderRadius.all(Radius.circular(8)),
                    //color: Color(0xFFE0E0E0),
                  ),
                  child: SpacedColumn(
                    spacing: 4,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        'SERVICESTATUSGUI',
                        style: GoogleFonts.vt323(
                            textStyle: const TextStyle(fontSize: 72, fontWeight: FontWeight.bold, color: Colors.blue)),
                      ),
                      Text(
                        'Dashboard',
                        style: GoogleFonts.inter(
                            textStyle: TextStyle(fontSize: 16, fontWeight: FontWeight.w400, color: Colors.grey[500])),
                      )
                    ],
                  ),
                ),
              )),
          Expanded(
              flex: 8,
              child: SizedBox(
                width: double.maxFinite,
                child: DecoratedBox(
                  decoration: const BoxDecoration(
                    borderRadius: BorderRadius.all(Radius.circular(8)),
                    color: Colors.transparent,
                  ),
                  child: SpacedRow(
                    spacing: 8,
                    children: [
                      Expanded(
                          child: SizedBox(
                        height: double.maxFinite,
                        width: double.maxFinite,
                        child: DecoratedBox(
                          decoration: const BoxDecoration(
                            borderRadius: BorderRadius.all(Radius.circular(8)),
                            //color: Colors.grey.shade200,
                          ),
                          child: SpacedColumn(
                            spacing: 8,
                            children: [
                              Expanded(
                                  child: SizedBox(
                                height: double.maxFinite,
                                width: double.maxFinite,
                                child: DecoratedBox(
                                    decoration: BoxDecoration(
                                      borderRadius: const BorderRadius.all(Radius.circular(8)),
                                      color: Colors.grey.shade200,
                                    ),
                                    child: Padding(
                                      padding: const EdgeInsets.all(8),
                                      child: SpacedColumn(
                                        spacing: 4,
                                        children: [
                                          Expanded(
                                            child: SpacedColumn(
                                              spacing: 0,
                                              children: [
                                                Text(
                                                  'Current Weight',
                                                  style: GoogleFonts.inter(
                                                      textStyle: const TextStyle(
                                                          fontWeight: FontWeight.w800,
                                                          fontSize: 32,
                                                          color: Colors.black54)),
                                                ),
                                                const Text(
                                                  'Stored in ColdRoom',
                                                  style: TextStyle(
                                                      fontWeight: FontWeight.w500, fontSize: 14, color: Colors.black45),
                                                ),
                                              ],
                                            ),
                                          ),
                                          Expanded(
                                              flex: 3,
                                              child: SizedBox.square(
                                                  dimension: 128,
                                                  child: CircularPercentIndicator(
                                                    radius: 96,
                                                    animation: true,
                                                    lineWidth: 16,
                                                    percent: 0.75,
                                                    center: RichText(
                                                      textAlign: TextAlign.center,
                                                      text: const TextSpan(
                                                        text: 'KG\n',
                                                        style: TextStyle(
                                                            fontWeight: FontWeight.w600,
                                                            fontSize: 16,
                                                            color: Colors.black26),
                                                        children: <TextSpan>[
                                                          TextSpan(
                                                              text: '7.5\n',
                                                              style: TextStyle(
                                                                  letterSpacing: 0,
                                                                  fontWeight: FontWeight.w600,
                                                                  fontSize: 32,
                                                                  color: Colors.redAccent)),
                                                          TextSpan(
                                                              text: 'Out of 10',
                                                              style: TextStyle(
                                                                  fontWeight: FontWeight.w400,
                                                                  fontSize: 16,
                                                                  color: Colors.black38)),
                                                        ],
                                                      ),
                                                    ),
                                                    progressColor: Colors.redAccent,
                                                    backgroundColor: Colors.redAccent.shade100,
                                                    circularStrokeCap: CircularStrokeCap.round,
                                                  ))),
                                        ],
                                      ),
                                    )),
                              )),
                              Expanded(
                                  child: SizedBox(
                                height: double.maxFinite,
                                width: double.maxFinite,
                                child: DecoratedBox(
                                    decoration: const BoxDecoration(
                                      borderRadius: BorderRadius.all(Radius.circular(8)),
                                    ),
                                    child: SpacedRow(spacing: 8, children: [
                                      Expanded(
                                        child: SizedBox(
                                          height: double.maxFinite,
                                          child: DecoratedBox(
                                            decoration: BoxDecoration(
                                              borderRadius: const BorderRadius.all(Radius.circular(8)),
                                              color: Colors.grey.shade200,
                                            ),
                                            child: Padding(
                                              padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
                                              child: SpacedColumn(
                                                spacing: 16,
                                                children: [
                                                  Text(
                                                    'Transport Trolley',
                                                    textAlign: TextAlign.center,
                                                    style: GoogleFonts.inter(
                                                        textStyle: const TextStyle(
                                                            fontWeight: FontWeight.w800,
                                                            fontSize: 32,
                                                            color: Colors.black54)),
                                                  ),
                                                  SpacedColumn(
                                                      spacing: 0,
                                                      mainAxisAlignment: MainAxisAlignment.center,
                                                      children: [
                                                        Text(
                                                          'STATE',
                                                          style: TextStyle(
                                                              color: Colors.blueAccent.shade200,
                                                              fontWeight: FontWeight.w900),
                                                        ),
                                                        const Text(
                                                          'Home',
                                                          style: TextStyle(fontSize: 36, color: Colors.black26),
                                                        ),
                                                      ]),
                                                  SpacedColumn(spacing: 0, children: [
                                                    Text(
                                                      'POSITION',
                                                      style: TextStyle(
                                                          color: Colors.blueAccent.shade200,
                                                          fontWeight: FontWeight.w900),
                                                    ),
                                                    const Text(
                                                      '(X,Y)',
                                                      style: TextStyle(fontSize: 36, color: Colors.black26),
                                                    ),
                                                  ]),
                                                ],
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                      Expanded(
                                        child: SizedBox(
                                          height: double.maxFinite,
                                          child: DecoratedBox(
                                            decoration: BoxDecoration(
                                              borderRadius: const BorderRadius.all(Radius.circular(8)),
                                              color: Colors.grey.shade200,
                                            ),
                                            child: Padding(
                                              padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
                                              child: SpacedColumn(
                                                spacing: 16,
                                                children: [
                                                  Text(
                                                    'Rejected Requests',
                                                    textAlign: TextAlign.center,
                                                    style: GoogleFonts.inter(
                                                        textStyle: const TextStyle(
                                                            fontWeight: FontWeight.w800,
                                                            fontSize: 32,
                                                            color: Colors.black54)),
                                                  ),
                                                  const Text(
                                                    '0',
                                                    style: TextStyle(fontSize: 96, color: Colors.black26),
                                                  ),
                                                ],
                                              ),
                                            ),
                                          ),
                                        ),
                                      )
                                    ])),
                              )),
                            ],
                          ),
                        ),
                      )),
                      Expanded(
                          flex: 2,
                          child: SizedBox(
                            height: double.maxFinite,
                            width: double.maxFinite,
                            child: DecoratedBox(
                              decoration: BoxDecoration(
                                borderRadius: const BorderRadius.all(Radius.circular(8)),
                                color: Colors.grey.shade200,
                              ),
                              child: Padding(
                                  padding: EdgeInsets.all(16),
                                  child: SpacedColumn(spacing: 16, children: [
                                    /*
                                    Expanded(
                                      child: Text(
                                        'Grid',
                                        textAlign: TextAlign.center,
                                        style: GoogleFonts.inter(
                                            textStyle: const TextStyle(
                                                fontWeight: FontWeight.w800, fontSize: 32, color: Colors.black54)),
                                      ),
                                    ),*/
                                    const Expanded(flex: 16, child: MapGrid())
                                  ])),
                            ),
                          )),
                      /*
                      Expanded(
                          child: SizedBox(
                        height: double.maxFinite,
                        width: double.maxFinite,
                        child: DecoratedBox(
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.all(Radius.circular(8)),
                          ),
                        ),
                      ))*/
                    ],
                  ),
                ),
              )),
          const Expanded(
              child: SizedBox(
            width: double.maxFinite,
            child: DecoratedBox(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.all(Radius.circular(8)),
                //color: Color(0xFFE0E0E0),
              ),
            ),
          )),
        ],
      ),
    )));
  }
}

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
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 8),
                    child: SpacedColumn(
                      spacing: 4,
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Expanded(
                          flex: 5,
                          child: SizedBox(
                            height: double.maxFinite,
                            child: FittedBox(
                              fit: BoxFit.cover,
                              child: Text(
                                'SERVICE STATUS',
                                style: GoogleFonts.vt323(
                                    textStyle: const TextStyle(
                                        fontWeight: FontWeight.bold, color: Colors.blue, letterSpacing: 2)),
                              ),
                            ),
                          ),
                        ),
                        Expanded(
                          child: FittedBox(
                            fit: BoxFit.cover,
                            child: Text(
                              'Dashboard',
                              style: GoogleFonts.ptMono(
                                  textStyle: TextStyle(fontWeight: FontWeight.w400, color: Colors.grey[500])),
                            ),
                          ),
                        )
                      ],
                    ),
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
                                      padding: const EdgeInsets.all(16),
                                      child: SpacedColumn(
                                        spacing: 16,
                                        children: [
                                          Expanded(
                                            child: SpacedColumn(
                                              spacing: 0,
                                              children: [
                                                Expanded(
                                                  flex: 2,
                                                  child: FittedBox(
                                                    fit: BoxFit.cover,
                                                    child: Text(
                                                      'Current Weight',
                                                      style: GoogleFonts.roboto(
                                                          textStyle: const TextStyle(
                                                              fontWeight: FontWeight.w800, color: Colors.blueAccent)),
                                                    ),
                                                  ),
                                                ),
                                                const Expanded(
                                                  child: FittedBox(
                                                    fit: BoxFit.cover,
                                                    child: Text(
                                                      'Stored in ColdRoom',
                                                      style:
                                                          TextStyle(fontWeight: FontWeight.w500, color: Colors.black45),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),
                                          ),
                                          Expanded(
                                              flex: 3,
                                              child: FittedBox(
                                                fit: BoxFit.cover,
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
                                                ),
                                              )),
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
                                              padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                                              child: SpacedColumn(
                                                spacing: 16,
                                                children: [
                                                  Expanded(
                                                    flex: 1,
                                                    child: FittedBox(
                                                      fit: BoxFit.fitWidth,
                                                      child: Text(
                                                        'Transport Trolley',
                                                        textAlign: TextAlign.center,
                                                        style: GoogleFonts.roboto(
                                                            textStyle: const TextStyle(
                                                                fontWeight: FontWeight.w800, color: Colors.blueAccent)),
                                                      ),
                                                    ),
                                                  ),
                                                  const Expanded(
                                                    flex: 2,
                                                    child: SpacedColumn(
                                                        spacing: 0,
                                                        mainAxisAlignment: MainAxisAlignment.center,
                                                        children: [
                                                          Expanded(
                                                            child: FittedBox(
                                                              fit: BoxFit.cover,
                                                              child: Text(
                                                                'STATE',
                                                                style: TextStyle(
                                                                    color: Colors.black26, fontWeight: FontWeight.w900),
                                                              ),
                                                            ),
                                                          ),
                                                          Expanded(
                                                            flex: 2,
                                                            child: FittedBox(
                                                              fit: BoxFit.cover,
                                                              child: Text(
                                                                'Home',
                                                                style: TextStyle(fontSize: 36, color: Colors.black54),
                                                              ),
                                                            ),
                                                          ),
                                                        ]),
                                                  ),
                                                  const Expanded(
                                                    flex: 2,
                                                    child: SpacedColumn(
                                                        spacing: 0,
                                                        mainAxisAlignment: MainAxisAlignment.center,
                                                        children: [
                                                          Expanded(
                                                            child: FittedBox(
                                                              fit: BoxFit.cover,
                                                              child: Text(
                                                                'POSITION',
                                                                style: TextStyle(
                                                                    color: Colors.black26, fontWeight: FontWeight.w900),
                                                              ),
                                                            ),
                                                          ),
                                                          Expanded(
                                                            flex: 2,
                                                            child: FittedBox(
                                                              fit: BoxFit.cover,
                                                              child: Text(
                                                                '(X,Y)',
                                                                style: TextStyle(fontSize: 36, color: Colors.black54),
                                                              ),
                                                            ),
                                                          ),
                                                        ]),
                                                  ),
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
                                              padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                                              child: SpacedColumn(
                                                spacing: 16,
                                                children: [
                                                  Expanded(
                                                    flex: 1,
                                                    child: FittedBox(
                                                      fit: BoxFit.fitWidth,
                                                      child: Text(
                                                        'Rejected Requests',
                                                        maxLines: 2,
                                                        textAlign: TextAlign.center,
                                                        style: GoogleFonts.roboto(
                                                            textStyle: const TextStyle(
                                                                fontWeight: FontWeight.w800, color: Colors.blueAccent)),
                                                      ),
                                                    ),
                                                  ),
                                                  const Expanded(
                                                    flex: 4,
                                                    child: FittedBox(
                                                      fit: BoxFit.cover,
                                                      child: Text(
                                                        '0',
                                                        style: TextStyle(fontSize: 96, color: Colors.black54),
                                                      ),
                                                    ),
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
                          flex: 1,
                          child: SizedBox(
                            height: double.maxFinite,
                            width: double.maxFinite,
                            child: DecoratedBox(
                              decoration: BoxDecoration(
                                borderRadius: const BorderRadius.all(Radius.circular(8)),
                                color: Colors.grey.shade200,
                              ),
                              child: Padding(
                                  padding: const EdgeInsets.all(16),
                                  child: SpacedColumn(spacing: 16, children: [
                                    Expanded(
                                      child: FittedBox(
                                        fit: BoxFit.contain,
                                        child: Text(
                                          'Grid',
                                          textAlign: TextAlign.center,
                                          style: GoogleFonts.inter(
                                              textStyle: const TextStyle(
                                                  fontWeight: FontWeight.w800, color: Colors.blueAccent)),
                                        ),
                                      ),
                                    ),
                                    const Expanded(flex: 11, child: MapGrid())
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

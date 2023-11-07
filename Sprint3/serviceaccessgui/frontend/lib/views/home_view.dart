import 'package:flutter/material.dart';
import 'package:frontend/widgets/spaced_column.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';

import '../widgets/spaced_row.dart';

class HomeView extends StatefulWidget {
  const HomeView({super.key});

  @override
  State<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends State<HomeView> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Padding(
      padding: const EdgeInsets.all(16),
      child: SpacedColumn(
        spacing: 8,
        children: [
          Expanded(
              flex: 1,
              child: SizedBox(
                width: double.maxFinite,
                child: SpacedColumn(
                  spacing: 0,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Text(
                      'SERVICEACCESSGUI',
                      style: GoogleFonts.vt323(
                          textStyle: const TextStyle(fontSize: 48, fontWeight: FontWeight.bold, color: Colors.blue)),
                    ),
                    Text(
                      'Dashboard',
                      style: GoogleFonts.vt323(
                          textStyle: TextStyle(fontSize: 32, fontWeight: FontWeight.w400, color: Colors.grey[500])),
                    )
                  ],
                ),
              )),
          Expanded(
            flex: 5,
            child: SpacedRow(
              spacing: 8,
              children: [
                Expanded(
                    flex: 1,
                    child: SizedBox(
                      height: double.maxFinite,
                      child: DecoratedBox(
                        decoration: BoxDecoration(
                          color: Colors.grey.shade100,
                          borderRadius: BorderRadius.circular(16),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.all(16),
                          child: SpacedColumn(
                            spacing: 8,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Expanded(
                                  flex: 1,
                                  child: SpacedColumn(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    spacing: 2,
                                    children: [
                                      Expanded(
                                        child: SpacedColumn(
                                          spacing: 2,
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
                                                  fontWeight: FontWeight.w500, fontSize: 20, color: Colors.black45),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ],
                                  )),
                              const Spacer(),
                              Expanded(
                                  flex: 4,
                                  child: SizedBox.square(
                                      dimension: 128,
                                      child: CircularPercentIndicator(
                                        radius: 136,
                                        animation: true,
                                        lineWidth: 16,
                                        percent: 0.75,
                                        center: RichText(
                                          textAlign: TextAlign.center,
                                          text: const TextSpan(
                                            text: 'KG\n',
                                            style: TextStyle(
                                                fontWeight: FontWeight.w600, fontSize: 16, color: Colors.black26),
                                            children: <TextSpan>[
                                              TextSpan(
                                                  text: '7.5\n',
                                                  style: TextStyle(
                                                      letterSpacing: -5,
                                                      fontWeight: FontWeight.w600,
                                                      fontSize: 96,
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
                              const Expanded(
                                child: Spacer(),
                              ),
                              /*
                              Expanded(
                                flex: 4,
                                child: SpacedColumn(
                                  spacing: 2,
                                  children: [
                                    Text(
                                      '7.5kg',
                                      style: GoogleFonts.inter(
                                          textStyle: const TextStyle(
                                              fontWeight: FontWeight.w600, fontSize: 48, color: Colors.redAccent)),
                                    ),
                                    Text(
                                      'Out of 10kg',
                                      style: GoogleFonts.inter(
                                          textStyle: const TextStyle(
                                              fontWeight: FontWeight.w400, fontSize: 16, color: Colors.black38)),
                                    )
                                  ],
                                ),
                              ),
                              */
                            ],
                          ),
                        ),
                      ),
                    )),
                Expanded(
                    flex: 2,
                    child: SpacedColumn(
                      spacing: 8,
                      children: [
                        Expanded(
                          child: SizedBox(
                            child: DecoratedBox(
                              decoration: BoxDecoration(
                                color: Colors.grey.shade200,
                                borderRadius: BorderRadius.circular(16),
                              ),
                              child: Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: SpacedRow(
                                  spacing: 8,
                                  children: [
                                    Expanded(
                                      child: SizedBox(
                                        width: double.maxFinite,
                                        child: DecoratedBox(
                                          decoration: BoxDecoration(
                                            color: Colors.grey.shade100,
                                            borderRadius: BorderRadius.circular(16),
                                          ),
                                          child: Padding(
                                            padding: const EdgeInsets.all(16),
                                            child: SpacedColumn(
                                              mainAxisSize: MainAxisSize.max,
                                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                              spacing: 8,
                                              children: [
                                                Text(
                                                  'Ticket Request',
                                                  style: GoogleFonts.inter(
                                                      textStyle: const TextStyle(
                                                          fontWeight: FontWeight.w800,
                                                          fontSize: 32,
                                                          color: Colors.black54)),
                                                ),
                                                const TextField(
                                                  decoration: InputDecoration(
                                                      fillColor: Colors.white,
                                                      prefixIcon: Icon(Icons.scale),
                                                      border: OutlineInputBorder(
                                                          borderRadius: BorderRadius.all(Radius.circular(32))),
                                                      //labelText: 'Ticket Request',
                                                      hintText: 'How many kg?'),
                                                ),
                                                FilledButton(
                                                    onPressed: () => get(Uri.parse("http://localhost:8080/users")),
                                                    child: const Text('Submit'))
                                              ],
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                    Expanded(
                                      child: SizedBox(
                                        width: double.maxFinite,
                                        child: DecoratedBox(
                                          decoration: BoxDecoration(
                                            color: Colors.grey.shade100,
                                            borderRadius: BorderRadius.circular(16),
                                          ),
                                          child: Padding(
                                            padding: const EdgeInsets.all(16),
                                            child: SpacedColumn(
                                              spacing: 8,
                                              children: [
                                                Text(
                                                  'Ticket Response',
                                                  style: GoogleFonts.inter(
                                                      textStyle: const TextStyle(
                                                          fontWeight: FontWeight.w800,
                                                          fontSize: 32,
                                                          color: Colors.black54)),
                                                ),
                                                Stack(
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
                                                              color: Colors.grey.shade100, shape: BoxShape.circle),
                                                        ),
                                                      ),
                                                    ),
                                                    Positioned(
                                                      left: -32,
                                                      child: SizedBox.square(
                                                        dimension: 64,
                                                        child: DecoratedBox(
                                                          decoration: BoxDecoration(
                                                              color: Colors.grey.shade100, shape: BoxShape.circle),
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
                                                            Text('12',
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
                                                ),
                                              ],
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ),
                        Expanded(
                          child: SizedBox(
                            width: double.maxFinite,
                            child: DecoratedBox(
                              decoration: BoxDecoration(
                                color: Theme.of(context).colorScheme.secondaryContainer,
                                borderRadius: BorderRadius.circular(16),
                              ),
                            ),
                          ),
                        ),
                      ],
                    )),
              ],
            ),
          ),
          Expanded(
              flex: 1,
              child: SizedBox(
                width: double.maxFinite,
                child: DecoratedBox(
                  decoration: BoxDecoration(
                    color: Theme.of(context).colorScheme.secondaryContainer,
                    borderRadius: BorderRadius.circular(16),
                  ),
                ),
              ))
        ],
      ),
    ));
  }
}

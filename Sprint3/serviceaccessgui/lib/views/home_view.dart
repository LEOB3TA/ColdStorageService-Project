import 'package:flutter/material.dart';
import 'package:frontend/widgets/spaced_column.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart';

import '../widgets/spaced_row.dart';

class HomeView extends StatefulWidget {
  const HomeView({super.key});

  @override
  State<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends State<HomeView> {
  int _index = 0;

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
                          textStyle: const TextStyle(
                              fontSize: 48,
                              fontWeight: FontWeight.bold,
                              color: Colors.blue)),
                    ),
                    Text(
                      'Dashboard',
                      style: GoogleFonts.vt323(
                          textStyle: TextStyle(
                              fontSize: 32,
                              fontWeight: FontWeight.w400,
                              color: Colors.grey[500])),
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
                          color: Colors.blue.shade100,
                          borderRadius: BorderRadius.circular(16),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.all(16),
                          child: SpacedColumn(
                            spacing: 8,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Text(
                                'Ticket Request',
                                style: GoogleFonts.inter(
                                    textStyle: TextStyle(
                                        fontWeight: FontWeight.w800,
                                        fontSize: 32,
                                        color: Colors.blue.shade900)),
                              ),
                              Text('Procedure - What to do',
                                  style: GoogleFonts.inter(
                                      textStyle: TextStyle(
                                          fontSize: 16,
                                          color: Colors.blue.shade900))),
                              const Spacer(),
                              Stepper(
                                currentStep: _index,
                                onStepCancel: () {
                                  if (_index > 0) {
                                    setState(() {
                                      _index -= 1;
                                    });
                                  }
                                },
                                onStepContinue: () {
                                  if (_index <= 0) {
                                    setState(() {
                                      _index += 1;
                                    });
                                  }
                                },
                                onStepTapped: (int index) {
                                  setState(() {
                                    _index = index;
                                  });
                                },
                                controlsBuilder: (context, details) =>
                                    const Padding(
                                  padding: EdgeInsets.only(top: 16),
                                  child: SpacedRow(
                                    spacing: 8,
                                  ),
                                ),
                                steps: <Step>[
                                  Step(
                                    title: Text('Store Food Request',
                                        style: TextStyle(
                                            color: Colors.blue.shade900,
                                            fontWeight: FontWeight.bold)),
                                    content: Text(
                                      'Insert the amount of kg you want to deposit.',
                                      style: TextStyle(
                                          color: Colors.blue.shade900),
                                    ),
                                  ),
                                  Step(
                                    title: Text('Rescue Ticket',
                                        style: TextStyle(
                                            color: Colors.blue.shade900,
                                            fontWeight: FontWeight.bold)),
                                    content: Text(
                                        'Save your ticket number to deposit food.',
                                        style: TextStyle(
                                            color: Colors.blue.shade900)),
                                  ),
                                  Step(
                                    title: Text('Ticket Request',
                                        style: TextStyle(
                                            color: Colors.blue.shade900,
                                            fontWeight: FontWeight.bold)),
                                    content: Text(
                                        'Insert your ticket to deposit food.',
                                        style: TextStyle(
                                            color: Colors.blue.shade900)),
                                  ),
                                ],
                              ),
                              const Spacer(),
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
                                            borderRadius:
                                                BorderRadius.circular(16),
                                          ),
                                          child: Padding(
                                            padding: const EdgeInsets.all(16),
                                            child: SpacedColumn(
                                              mainAxisSize: MainAxisSize.max,
                                              mainAxisAlignment:
                                                  MainAxisAlignment
                                                      .spaceBetween,
                                              spacing: 8,
                                              children: [
                                                Text(
                                                  'Ticket Request',
                                                  style: GoogleFonts.inter(
                                                      textStyle:
                                                          const TextStyle(
                                                              fontWeight:
                                                                  FontWeight
                                                                      .w800,
                                                              fontSize: 32,
                                                              color: Colors
                                                                  .black54)),
                                                ),
                                                const TextField(
                                                  decoration: InputDecoration(
                                                      fillColor: Colors.white,
                                                      prefixIcon:
                                                          Icon(Icons.scale),
                                                      border: OutlineInputBorder(
                                                          borderRadius:
                                                              BorderRadius.all(
                                                                  Radius
                                                                      .circular(
                                                                          32))),
                                                      //labelText: 'Ticket Request',
                                                      hintText: 'How many kg?'),
                                                ),
                                                FilledButton(
                                                    onPressed: () => get(Uri.parse(
                                                        "http://localhost:8080/users")),
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
                                            borderRadius:
                                                BorderRadius.circular(16),
                                          ),
                                          child: Padding(
                                            padding: const EdgeInsets.all(16),
                                            child: SpacedColumn(
                                              spacing: 8,
                                              children: [
                                                Text(
                                                  'Ticket Response',
                                                  style: GoogleFonts.inter(
                                                      textStyle:
                                                          const TextStyle(
                                                              fontWeight:
                                                                  FontWeight
                                                                      .w800,
                                                              fontSize: 32,
                                                              color: Colors
                                                                  .black54)),
                                                ),
                                                Stack(
                                                  alignment: Alignment.center,
                                                  children: [
                                                    SizedBox(
                                                      height: 200,
                                                      width: 300,
                                                      child: DecoratedBox(
                                                        decoration:
                                                            BoxDecoration(
                                                          color: Colors
                                                              .amber.shade200,
                                                          borderRadius:
                                                              BorderRadius
                                                                  .circular(4),
                                                        ),
                                                      ),
                                                    ),
                                                    SizedBox(
                                                      height: 184,
                                                      width: 284,
                                                      child: DecoratedBox(
                                                        decoration:
                                                            BoxDecoration(
                                                          color: Colors
                                                              .amber.shade200,
                                                          borderRadius:
                                                              BorderRadius
                                                                  .circular(8),
                                                          border: Border.all(
                                                            color: Colors
                                                                .amber.shade400,
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
                                                              color: Colors.grey
                                                                  .shade100,
                                                              shape: BoxShape
                                                                  .circle),
                                                        ),
                                                      ),
                                                    ),
                                                    Positioned(
                                                      left: -32,
                                                      child: SizedBox.square(
                                                        dimension: 64,
                                                        child: DecoratedBox(
                                                          decoration: BoxDecoration(
                                                              color: Colors.grey
                                                                  .shade100,
                                                              shape: BoxShape
                                                                  .circle),
                                                        ),
                                                      ),
                                                    ),
                                                    Positioned.fill(
                                                      top: 36,
                                                      child: Align(
                                                        alignment: Alignment
                                                            .bottomCenter,
                                                        child: SpacedColumn(
                                                          spacing: 0,
                                                          children: [
                                                            const Text(
                                                              'Your Ticket is',
                                                              style: TextStyle(
                                                                  fontWeight:
                                                                      FontWeight
                                                                          .w300,
                                                                  fontSize: 12,
                                                                  color: Colors
                                                                      .black45),
                                                            ),
                                                            Text('12',
                                                                style: TextStyle(
                                                                    letterSpacing:
                                                                        -5,
                                                                    fontWeight:
                                                                        FontWeight
                                                                            .w900,
                                                                    fontSize:
                                                                        96,
                                                                    color: Colors
                                                                        .lime
                                                                        .shade900)),
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
                                color: Colors.grey.shade200,
                                borderRadius: BorderRadius.circular(16),
                              ),
                              child: Padding(
                                padding: const EdgeInsets.all(16),
                                child: SpacedColumn(
                                  mainAxisSize: MainAxisSize.max,
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
                                  spacing: 8,
                                  children: [
                                    Text(
                                      'Deposit Ticket',
                                      style: GoogleFonts.inter(
                                          textStyle: const TextStyle(
                                              fontWeight: FontWeight.w800,
                                              fontSize: 32,
                                              color: Colors.black54)),
                                    ),
                                    const TextField(
                                      decoration: InputDecoration(
                                          fillColor: Colors.white,
                                          prefixIcon: Icon(Icons.numbers),
                                          border: OutlineInputBorder(
                                              borderRadius: BorderRadius.all(
                                                  Radius.circular(32))),
                                          //labelText: 'Ticket Request',
                                          hintText: 'Ticket Id'),
                                    ),
                                    FilledButton(
                                        onPressed: () => get(Uri.parse(
                                            "http://localhost:8080/deposit")),
                                        child: const Text('Submit'))
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ),
                      ],
                    )),
              ],
            ),
          ),
        ],
      ),
    ));
  }
}

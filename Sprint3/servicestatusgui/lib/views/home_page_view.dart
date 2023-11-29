import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:logger/logger.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';
import 'package:servicestatusgui/model/service_config_dto.dart';
import 'package:servicestatusgui/model/service_status_dto.dart';
import 'package:servicestatusgui/provider/service_config_provider.dart';
import 'package:servicestatusgui/widgets/map_grid.dart';
import 'package:servicestatusgui/widgets/spaced_column.dart';
import 'package:servicestatusgui/widgets/spaced_row.dart';
import 'package:skeletonizer/skeletonizer.dart';
import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';

import '../provider/service_status_provider.dart';

const socketUrl = 'ws://localhost:11804/ws-message';

var logger = Logger(printer: PrettyPrinter());

class HomePageView extends ConsumerStatefulWidget {
  const HomePageView({super.key});

  @override
  ConsumerState<HomePageView> createState() => _HomePageViewState();
}

class _HomePageViewState extends ConsumerState<HomePageView> {
  late StompClient stompClient;
  final ServiceStatusDTO serviceStatusDTO = ServiceStatusDTO();
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    stompClient = StompClient(
        config: StompConfig(
      url: socketUrl,
      onConnect: onConnect,
      onWebSocketError: (dynamic error) {
        logger.e("WebSocket Error: ${error.toString()}");
        const snackBar = SnackBar(
          content: Text("Generic Error - Can't connect to Server"),
          backgroundColor: Colors.red,
          behavior: SnackBarBehavior.floating,
        );
        ScaffoldMessenger.of(context).showSnackBar(snackBar);
      },
      beforeConnect: () async {
        logger.i('WebSocket Info: Waiting to connect...');
        await Future.delayed(const Duration(milliseconds: 200));
        logger.i('WebSocket Info: Connecting...');
      },
      onDisconnect: (dynamic frame) {
        logger.i('WebSocket Info: Disconnected.');
      },
    ));

    stompClient.activate();
  }

  void onConnect(StompFrame frame) {
    //stompClient.subscribe(destination: '/subscribe', callback: (StompFrame frame) => logger.i(frame.body!));
    setState(() {
      isLoading = true;
    });
    stompClient.subscribe(
        destination: '/topic/message',
        callback: (StompFrame frame) {
          if (frame.body != null) {
            Map<String, dynamic> result = json.decode(frame.body!);
            if (result.containsKey('currentWeight')) {
              ref.read(serviceStatusProvider.notifier).state = ServiceStatusDTO.fromJson(result);
            } else {
              ref.read(serviceConfigProvider.notifier).state = ServiceConfigDTO.fromJson(result);
            }
          }
        });
    logger.i('WebSocket Info: Connected.');
    const snackBar = SnackBar(
      content: Text("Connected to Server"),
      backgroundColor: Colors.green,
      behavior: SnackBarBehavior.floating,
    );
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
    setState(() {
      isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    final config = ref.watch(serviceConfigProvider);
    final status = ref.watch(serviceStatusProvider);
    final percent = status.currentWeight / config.maxWeight;
    final progressColor = percent < 0.5
        ? Colors.green
        : percent < 0.8
            ? Colors.orange
            : Colors.red;
    logger.i('Service Config: ${config.toString()}');
    logger.i('Service Status: ${status.toString()}');
    return Scaffold(
        body: Center(
            child: Padding(
      padding: const EdgeInsets.all(16),
      child: SpacedColumn(
        spacing: 8,
        children: [
          Expanded(
              flex: 2,
              child: Skeletonizer(
                enabled: isLoading,
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
                                                  percent: percent,
                                                  center: RichText(
                                                    textAlign: TextAlign.center,
                                                    text: TextSpan(
                                                      text: 'KG\n',
                                                      style: const TextStyle(
                                                          fontWeight: FontWeight.w600,
                                                          fontSize: 16,
                                                          color: Colors.black26),
                                                      children: <TextSpan>[
                                                        TextSpan(
                                                            text: '${status.currentWeight}\n',
                                                            style: TextStyle(
                                                                letterSpacing: 0,
                                                                fontWeight: FontWeight.w600,
                                                                fontSize: 32,
                                                                color: progressColor)),
                                                        TextSpan(
                                                            text: 'Out of ${config.maxWeight} kg',
                                                            style: const TextStyle(
                                                                fontWeight: FontWeight.w400,
                                                                fontSize: 16,
                                                                color: Colors.black38)),
                                                      ],
                                                    ),
                                                  ),
                                                  progressColor: progressColor,
                                                  backgroundColor: progressColor.shade100,
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
                                                  Expanded(
                                                    flex: 2,
                                                    child: SpacedColumn(
                                                        spacing: 0,
                                                        mainAxisAlignment: MainAxisAlignment.center,
                                                        children: [
                                                          const Expanded(
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
                                                                status.status,
                                                                style: const TextStyle(
                                                                    fontSize: 36, color: Colors.black54),
                                                              ),
                                                            ),
                                                          ),
                                                        ]),
                                                  ),
                                                  Expanded(
                                                    flex: 2,
                                                    child: SpacedColumn(
                                                        spacing: 0,
                                                        mainAxisAlignment: MainAxisAlignment.center,
                                                        children: [
                                                          const Expanded(
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
                                                                '(${status.position.getX}, ${status.position.getY})',
                                                                style: const TextStyle(
                                                                    fontSize: 36, color: Colors.black54),
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
                                                  Expanded(
                                                    flex: 4,
                                                    child: FittedBox(
                                                      fit: BoxFit.cover,
                                                      child: Text(
                                                        status.rejectedRequests.toString(),
                                                        style: const TextStyle(fontSize: 96, color: Colors.black54),
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
                          child: Skeletonizer(
                            enabled: isLoading,
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
                                      Expanded(
                                          flex: 11,
                                          child: Skeletonizer(
                                            enabled: isLoading,
                                            child: MapGrid(
                                              home: config.home,
                                              indoor: config.indoor,
                                              coldRoom: config.coldRoom,
                                              rows: config.rows,
                                              cols: config.cols,
                                            ),
                                          ))
                                    ])),
                              ),
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

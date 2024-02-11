import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:logger/logger.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';
import 'package:servicestatusgui/globals.dart';
import 'package:servicestatusgui/model/position.dart';
import 'package:servicestatusgui/model/service_config_dto.dart';
import 'package:servicestatusgui/model/service_status_dto.dart';
import 'package:servicestatusgui/provider/service_config_provider.dart';
import 'package:servicestatusgui/provider/service_status_provider.dart';
import 'package:servicestatusgui/responsive/desktop_body.dart';
import 'package:servicestatusgui/responsive/mobile_body.dart';
import 'package:servicestatusgui/responsive/responsive_layout.dart';
import 'package:servicestatusgui/widgets/current_weight_widget.dart';
import 'package:servicestatusgui/widgets/grid_widget.dart';
import 'package:servicestatusgui/widgets/layout/map_grid.dart';
import 'package:servicestatusgui/widgets/layout/spaced_column.dart';
import 'package:servicestatusgui/widgets/layout/spaced_row.dart';
import 'package:servicestatusgui/widgets/rejected_requests_widget.dart';
import 'package:servicestatusgui/widgets/transport_trolley_widget.dart';
import 'package:skeletonizer/skeletonizer.dart';
import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';
import 'package:uuid/uuid.dart';

var logger = Logger(printer: PrettyPrinter());

class HomePageView extends ConsumerStatefulWidget {
  final String url;
  const HomePageView({super.key, required this.url});

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
      url: widget.url,
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
      destination: '/queue/status',
      callback: (StompFrame frame){
        if (frame.body == null) {
          return;
        }
        if (json.decode(frame.body!)['currW'] != null) {
          // Service Config
          final configProvider = ref.read(serviceConfigProvider);
          ServiceConfigDTO config = ServiceConfigDTO.fromJson(json.decode(frame.body!));
          configProvider.maxWeight = config.getMaxWeight;
          configProvider.maxWeight = config.getMaxWeight;
          configProvider.home = config.getHome;
          configProvider.indoor = config.getIndoor;
          configProvider.coldRoom = config.getColdRoom;
          configProvider.rows = config.getRows;
          configProvider.cols = config.getCols;
        } else {
          // Service Status Update
          ServiceStatusDTO status = ServiceStatusDTO.fromJson(json.decode(frame.body!));

        }
        if(frame.body != null){
          print("Update : ${frame.body!}");
          ServiceConfigDTO update = ServiceConfigDTO.fromJson(json.decode(frame.body!));
          print(update);
        /*
          ref.read(serviceStatusProvider).currentWeight = update.getMaxWeight;
          ref.read(serviceStatusProvider).maxWeight = update.getMaxWeight;
          ref.read(serviceStatusProvider).rejectedRequests = update.getRejectedRequests;
          ref.read(serviceStatusProvider).status = update.getStatus;
          ref.read(serviceStatusProvider).position = update.getPosition;

          print(ref.read(serviceStatusProvider).toString());

         */
        }
      }
    );
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

  void updates(){
    if(kDebugMode){
      print("Update-------");
    }
    stompClient.send(
      destination:'/topic/updates',
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          'SERVICE STATUS',
          style: GoogleFonts.vt323(
              textStyle:
              TextStyle(fontSize: 64, fontWeight: FontWeight.bold, color: Globals.headerColor)),
        ),
        backgroundColor: Globals.backgroundColor,
        toolbarHeight: 96,
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : ResponsiveLayout(
        mobile: MobileBody(url: widget.url),
        desktop: DesktopBody(url: widget.url),
      ),
    );
  }
}


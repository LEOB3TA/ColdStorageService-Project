import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:logger/logger.dart';
import 'package:servicestatusgui/globals.dart';
import 'package:servicestatusgui/model/service_config_dto.dart';
import 'package:servicestatusgui/model/service_status_dto.dart';
import 'package:servicestatusgui/provider/service_config_provider.dart';
import 'package:servicestatusgui/provider/service_status_provider.dart';
import 'package:servicestatusgui/responsive/desktop_body.dart';
import 'package:servicestatusgui/responsive/mobile_body.dart';
import 'package:servicestatusgui/responsive/responsive_layout.dart';
import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';

var logger = Logger(printer: PrettyPrinter());

class HomePage extends ConsumerStatefulWidget {
  final String url;
  const HomePage({super.key, required this.url});

  @override
  ConsumerState<HomePage> createState() => _HomePageState();
}

class _HomePageState extends ConsumerState<HomePage> {
  late StompClient stompClient;
  final ServiceStatusDTO serviceStatusDTO = ServiceStatusDTO();
  late bool isLoading;

  @override
  void initState() {
    setState(() {
      isLoading = true;
    });
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
    stompClient.subscribe(
      destination: '/topic/status',
      callback: (StompFrame frame){
        debugPrint(frame.toString());
        if (frame.body == null) {
          return;
        }
        if (json.decode(frame.body!)['CurrW'] == null) {
          // Service Config
          ServiceConfigDTO config = ServiceConfigDTO.fromJson(json.decode(frame.body!));
          ref.read(serviceConfigProvider.notifier).state = config;
          debugPrint("Service Config: ${ref.read(serviceConfigProvider)}");
        } else {
          // Service Status Update
          ServiceStatusDTO status = ServiceStatusDTO.fromJson(json.decode(frame.body!));
          ref.read(serviceStatusProvider.notifier).state = status;
          debugPrint("Service Status Update: ${ref.read(serviceStatusProvider)}");
        }
        setState(() {
          isLoading = false;
        });
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


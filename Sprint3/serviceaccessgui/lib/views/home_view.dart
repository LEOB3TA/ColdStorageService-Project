import 'dart:convert';

import 'package:ServiceAccessGUI/model/store_food_request_dto.dart';
import 'package:ServiceAccessGUI/model/ticket_response_dto.dart';
import 'package:ServiceAccessGUI/providers/status_provider.dart';
import 'package:ServiceAccessGUI/widgets/custom_button.dart';
import 'package:ServiceAccessGUI/widgets/spaced_column.dart';
import 'package:ServiceAccessGUI/widgets/spaced_row.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_form_builder/flutter_form_builder.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:logger/logger.dart';
import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';

// const socketUrl = 'ws://localhost:11804/ws-message'; // if in local
const socketUrl = 'ws://192.168.1.2:11804/ws-message'; // if in another pc

var logger = Logger(printer: PrettyPrinter());

class HomeView extends ConsumerStatefulWidget {
  const HomeView({super.key});

  @override
  ConsumerState<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends ConsumerState<HomeView> {
  late StompClient stompClient;
  int ticketNumber = -1;
  //static const storeFoodKey = GlobalKey<storeFoodKey>();
  //static const depositTicketKey = Key('depositTicket');

  @override
  void initState() {
    super.initState();
    stompClientConfig();
  }

  void stompClientConfig() {
    stompClient = StompClient(
        config: StompConfig(
      url: socketUrl,
      onConnect: socketConnection,
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

  void socketConnection(StompFrame frame) {
    stompClient.subscribe(
        destination: '/topic/message',
        callback: (StompFrame frame) {
          if (frame.body != null) {
            print("RESULT: ${frame.body!}");
            Map<String, dynamic> result = json.decode(frame.body!);
            logger.t('WebSocket Connection Result: $result');
          }
        });

    stompClient.subscribe(
        destination: '/queue/store-food',
        callback: (StompFrame frame) {
          if (frame.body != null) {
            print("RESULT: ${frame.body!}");
            TicketResponseDTO result = TicketResponseDTO.fromJson(json.decode(frame.body!));
            setState(() {
              ticketNumber = result.ticketNumber;
            });
          }
        });

    logger.i('WebSocket Info: Connected.');
    ref.read(statusEnumProvider.notifier).state = StatusEnum.ticketRequest;
    const snackBar = SnackBar(
      content: Text("Connected to Server"),
      backgroundColor: Colors.green,
      behavior: SnackBarBehavior.floating,
    );
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }

  @override
  Widget build(BuildContext context) {
    final status = ref.watch(statusEnumProvider);
    final TextEditingController storeFoodController = TextEditingController();
    final TextEditingController _depositTicketController = TextEditingController();

    return Scaffold(
        body: Padding(
      padding: const EdgeInsets.all(16),
      child: SpacedColumn(
        spacing: 8,
        children: [
          /*
          Expanded(
              flex: 1,
              child: SizedBox(
                width: double.maxFinite,
                child: SpacedColumn(
                  spacing: 0,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Expanded(
                      flex: 3,
                      child: FittedBox(
                        fit: BoxFit.contain,
                        child: Text(
                          'SERVICEACCESSGUI',
                          style: GoogleFonts.vt323(
                              textStyle:
                                  const TextStyle(fontSize: 48, fontWeight: FontWeight.bold, color: Colors.blue)),
                        ),
                      ),
                    ),
                    Expanded(
                      flex: 1,
                      child: FittedBox(
                        fit: BoxFit.contain,
                        child: Text(
                          'Dashboard',
                          style: GoogleFonts.vt323(
                              textStyle: TextStyle(fontSize: 32, fontWeight: FontWeight.w400, color: Colors.grey[500])),
                        ),
                      ),
                    )
                  ],
                ),
              )),
           */
          Expanded(
            flex: 9,
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
                              /*
                              Expanded(
                                child: FittedBox(
                                  fit: BoxFit.contain,
                                  child: Text(
                                    'Ticket Request',
                                    style: GoogleFonts.inter(
                                        textStyle: TextStyle(fontWeight: FontWeight.w800, color: Colors.blue.shade900)),
                                  ),
                                ),
                              ),
                              FittedBox(
                                fit: BoxFit.fitHeight,
                                child: Text('Procedure - What to do',
                                    style: GoogleFonts.inter(textStyle: TextStyle(color: Colors.blue.shade900))),
                              )*/
                              //const Spacer(),
                              Expanded(
                                flex: 2,
                                child: FittedBox(
                                  fit: BoxFit.contain,
                                  child: Text(
                                    'SERVICEACCESSGUI',
                                    style: GoogleFonts.vt323(
                                        textStyle: TextStyle(
                                            fontSize: 48, fontWeight: FontWeight.bold, color: Colors.blue.shade900)),
                                  ),
                                ),
                              ),
                              /*
                              Expanded(
                                flex: 1,
                                child: FittedBox(
                                  fit: BoxFit.contain,
                                  child: Text(
                                    'Dashboard',
                                    style: GoogleFonts.vt323(
                                        textStyle: TextStyle(
                                            fontSize: 32, fontWeight: FontWeight.w400, color: Colors.grey[500])),
                                  ),
                                ),
                              ),
                               */
                              Expanded(
                                flex: 13,
                                child: FormBuilder(
                                  child: Stepper(
                                    currentStep: status.index,
                                    onStepCancel: () {
                                      if (status.index > 0) {
                                        ref.read(statusEnumProvider.notifier).state =
                                            StatusEnum.values[status.index - 1];
                                      }
                                    },
                                    onStepContinue: () {
                                      ref.read(statusEnumProvider.notifier).state = StatusEnum.values[status.index + 1];
                                    },
                                    onStepTapped: (int index) {
                                      ref.read(statusEnumProvider.notifier).state = StatusEnum.values[index];
                                    },
                                    controlsBuilder: (context, details) => const Padding(
                                      padding: EdgeInsets.only(top: 16),
                                      child: SpacedColumn(
                                        spacing: 8,
                                        children: [],
                                      ),
                                    ),
                                    steps: <Step>[
                                      /*
                                      Step(
                                        title: Text('Connection',
                                            style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                                        content: SpacedColumn(
                                            spacing: 8,
                                            crossAxisAlignment: CrossAxisAlignment.start,
                                            children: [
                                              Text(
                                                'Insert your username to connect to Server.',
                                                style: TextStyle(color: Colors.blue.shade900),
                                              ),
                                              TextFormField(
                                                decoration: InputDecoration(
                                                  border: OutlineInputBorder(
                                                      borderRadius: const BorderRadius.all(Radius.circular(32)),
                                                      borderSide: BorderSide(color: Colors.blue.shade900)),
                                                  labelText: 'Username',
                                                ),
                                              ),
                                              SpacedRow(spacing: 8, children: [
                                                CustomButton(onPressed: () => (), label: 'Connect'),
                                                /*
                                            Expanded(
                                              child: FilledButton(
                                                  onPressed: () => get(Uri.parse("http://localhost:8080/users")),
                                                  child: const Text('Disconnect')),
                                            ),

                                             */
                                              ])
                                            ]),
                                      ),*/
                                      Step(
                                        title: Text('Store Food Request',
                                            style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                                        content: SpacedColumn(
                                            spacing: 8,
                                            crossAxisAlignment: CrossAxisAlignment.start,
                                            children: [
                                              Text(
                                                'Insert the amount of kg you want to deposit.',
                                                style: TextStyle(color: Colors.blue.shade900),
                                              ),
                                              /*
                                              FormBuilderTextField(
                                                key: storeFoodKey,
                                                name: 'storeFood',
                                                validator: FormBuilderValidators.compose([
                                                  FormBuilderValidators.required(),
                                                  FormBuilderValidators.numeric(),
                                                  FormBuilderValidators.min(0),
                                                ]),
                                                decoration: const InputDecoration(
                                                    fillColor: Colors.white,
                                                    prefixIcon: Icon(Icons.scale),
                                                    border: OutlineInputBorder(
                                                        borderRadius: BorderRadius.all(Radius.circular(32))),
                                                    //labelText: 'Ticket Request',
                                                    hintText: 'How many kg?'),
                                              ),*/
                                              TextField(
                                                controller: storeFoodController,
                                                decoration: const InputDecoration(
                                                    errorText: 'Please enter a valid number',
                                                    fillColor: Colors.white,
                                                    prefixIcon: Icon(Icons.scale),
                                                    border: OutlineInputBorder(
                                                        borderRadius: BorderRadius.all(Radius.circular(32))),
                                                    //labelText: 'Ticket Request',
                                                    hintText: 'How many kg?'),
                                              ),
                                              CustomButton(
                                                  onPressed: () =>
                                                      storeFoodRequest(double.parse(storeFoodController.text)),
                                                  label: 'Submit')
                                            ]),
                                      ),
                                      Step(
                                        title: Text('Rescue Ticket',
                                            style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                                        content: SpacedColumn(
                                          spacing: 8,
                                          crossAxisAlignment: CrossAxisAlignment.start,
                                          children: [
                                            Text('Save your ticket number to deposit food.',
                                                style: TextStyle(color: Colors.blue.shade900)),
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
                                                          color: Colors.blue.shade100, shape: BoxShape.circle),
                                                    ),
                                                  ),
                                                ),
                                                Positioned(
                                                  left: -32,
                                                  child: SizedBox.square(
                                                    dimension: 64,
                                                    child: DecoratedBox(
                                                      decoration: BoxDecoration(
                                                          color: Colors.blue.shade100, shape: BoxShape.circle),
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
                                                        Text(ticketNumber.toString(),
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
                                            )
                                          ],
                                        ),
                                      ),
                                      Step(
                                        title: Text('Deposit Request',
                                            style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                                        content: SpacedColumn(
                                          spacing: 8,
                                          crossAxisAlignment: CrossAxisAlignment.start,
                                          children: [
                                            Text('Insert your ticket to deposit food.',
                                                style: TextStyle(color: Colors.blue.shade900)),
                                            const TextField(
                                              decoration: InputDecoration(
                                                  fillColor: Colors.white,
                                                  prefixIcon: Icon(Icons.numbers),
                                                  border: OutlineInputBorder(
                                                      borderRadius: BorderRadius.all(Radius.circular(32))),
                                                  //labelText: 'Ticket Request',
                                                  hintText: 'Ticket Id'),
                                            ),
                                            CustomButton(
                                                onPressed: () {
                                                  ref.read(statusEnumProvider.notifier).state = StatusEnum.result;
                                                },
                                                label: 'Submit')
                                          ],
                                        ),
                                      ),
                                      Step(
                                        title: Text('Result',
                                            style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                                        content: Text('Result.', style: TextStyle(color: Colors.blue.shade900)),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                              //const Spacer(),
                            ],
                          ),
                        ),
                      ),
                    )),
                /*
                Expanded(
                    flex: 2,
                    child: SpacedColumn(
                      spacing: 8,
                      children: [
                        Expanded(
                          child: SizedBox(
                            child: DecoratedBox(
                              decoration: BoxDecoration(
                                color: Colors.transparent,
                                borderRadius: BorderRadius.circular(16),
                              ),
                              child: Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: SpacedRow(
                                  spacing: 8,
                                  children: [
                                    if (status.index >= StatusEnum.ticketRequest.index)
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
                                    if (status.index >= StatusEnum.ticketResponse.index)
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
                        if (status == StatusEnum.depositTicket)
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
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    spacing: 8,
                                    children: [
                                      Text(
                                        'Deposit Ticket',
                                        style: GoogleFonts.inter(
                                            textStyle: const TextStyle(
                                                fontWeight: FontWeight.w800, fontSize: 32, color: Colors.black54)),
                                      ),
                                      const TextField(
                                        decoration: InputDecoration(
                                            fillColor: Colors.white,
                                            prefixIcon: Icon(Icons.numbers),
                                            border:
                                                OutlineInputBorder(borderRadius: BorderRadius.all(Radius.circular(32))),
                                            //labelText: 'Ticket Request',
                                            hintText: 'Ticket Id'),
                                      ),
                                      FilledButton(
                                          onPressed: () => get(Uri.parse("http://localhost:8080/deposit")),
                                          child: const Text('Submit'))
                                    ],
                                  ),
                                ),
                              ),
                            ),
                          ),
                      ],
                    )),

                 */
              ],
            ),
          ),
        ],
      ),
    ));
  }

  void storeFoodRequest(double quantity) {
    if (kDebugMode) {
      print('Store Food Request: $quantity');
    }
    stompClient.send(
      destination: '/app/store-food',
      body: json.encode(StoreFoodRequestDTO(quantity: quantity).toJson()),
    );

    ref.read(statusEnumProvider.notifier).state = StatusEnum.ticketResponse;
  }
}

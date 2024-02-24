import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:logger/logger.dart';
import 'package:service_access_gui/globals.dart';
import 'package:service_access_gui/model/stepper_model.dart';
import 'package:service_access_gui/model/store_food_request_dto.dart';
import 'package:service_access_gui/model/ticket_dto.dart';
import 'package:service_access_gui/model/weight_dto.dart';
import 'package:service_access_gui/providers/status_provider.dart';
import 'package:service_access_gui/providers/weight_status_provider.dart';
import 'package:service_access_gui/widgets/custom_button.dart';
import 'package:service_access_gui/widgets/layout/spaced_column.dart';
import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';
import 'package:uuid/uuid.dart';

var logger = Logger(printer: PrettyPrinter());

class StepperWidget extends ConsumerStatefulWidget {
  final String url;
  const StepperWidget({super.key, required this.url});

  @override
  ConsumerState<StepperWidget> createState() => _StepperWidgetState();
}

class _StepperWidgetState extends ConsumerState<StepperWidget> {
  late StompClient stompClient;
  int ticketNumber = -1;
  String id = const Uuid().v4();
  // Controller
  final TextEditingController storeFoodController = TextEditingController();
  final TextEditingController depositController = TextEditingController();
  // FormKey
  final formKey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();
    stompClientConfig();
  }

  void stompClientConfig() {
    stompClient = StompClient(
      config: StompConfig(
        url: widget.url,
        onConnect: socketConnection,
        //comment
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
      ),
    );

    stompClient.activate();
  }

  void socketConnection(StompFrame frame) {
    debugPrint(frame.headers.toString());
    //comment
    stompClient.subscribe(
        destination: '/topic/message',
        headers: {'id': id},
        callback: (StompFrame frame) {
          if (frame.body != null) {
            debugPrint("RESULT: ${frame.body!}");
            Map<String, dynamic> result = json.decode(frame.body!);
            logger.t('WebSocket Connection Result: $result');
          }
        });
    stompClient.subscribe(
        destination: '/topic/updates',
        callback: (StompFrame frame) {
          if (frame.body != null) {
            debugPrint("Weight Status Update received");
            WeightDTO update = WeightDTO.fromJson(json.decode(frame.body!));
            ref.read(weightStatusProvider.notifier).state = update;
            debugPrint("Weight Status Update: ${ref.read(weightStatusProvider)}");
          }
        });
    stompClient.subscribe(
        destination: '/user/queue/store-food/$id',
        callback: (StompFrame frame) {
          debugPrint('Store Food Response: ${frame.body}');
          if (frame.body != null) {
            TicketDTO result = TicketDTO.fromJson(json.decode(frame.body!));
            if (result.ticketNumber == -1) {
              const snackBar = SnackBar(
                content: Text("Error - the inserted weight could not be stored!"),
                backgroundColor: Colors.red,
                behavior: SnackBarBehavior.floating,
              );
              ScaffoldMessenger.of(context).showSnackBar(snackBar);
            } else {
              setState(() {
                ticketNumber = result.ticketNumber;
              });
              ref.read(stepperProvider.notifier).setCompleted(StatusEnum.ticketRequest.index);
              ref.read(stepperProvider.notifier).setIndex(StatusEnum.ticketResponse.index);
              ref.read(statusEnumProvider.notifier).state = StatusEnum.ticketResponse;
            }
          }
        });
    stompClient.subscribe(
        destination: '/user/queue/deposit/$id',
        callback: (StompFrame frame) {
          if (frame.body != null) {
            debugPrint("RESULT: ${frame.headers}");
            //String result = String.fromJson
            debugPrint("SS");
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
    final model = ref.watch(stepperProvider);

    return DecoratedBox(
      decoration: BoxDecoration(
        color: Globals.backgroundWidgetColor,
        borderRadius: BorderRadius.circular(16),
      ),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: SpacedColumn(
          spacing: 48,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Expanded(
              child: Text(
                'STEPPER',
                style: GoogleFonts.inter(
                    textStyle: TextStyle(
                        fontWeight: FontWeight.w600,
                        color: Colors.blue.shade900,
                        fontSize: Globals.headerWidgetFontSize)),
              ),
            ),
            Expanded(
              flex: 13,
              child: Stepper(
                type: StepperType.vertical,
                currentStep: model.index,
                // onStepTapped: (index) => ref.read(stepperProvider.notifier).setIndex(index),
                onStepCancel: () {
                  if (status.index > 0) {
                    ref.read(statusEnumProvider.notifier).state = StatusEnum.values[status.index - 1];
                  }
                },
                onStepContinue: () {
                  switch (status) {
                    case StatusEnum.ticketRequest:
                      if (formKey.currentState!.validate()) {
                        storeFoodRequest(double.parse(storeFoodController.text));
                      }
                      break;
                    case StatusEnum.ticketResponse:
                      ref.read(stepperProvider.notifier).setCompleted(StatusEnum.ticketResponse.index);
                      ref.read(stepperProvider.notifier).setIndex(StatusEnum.depositTicket.index);
                      ref.read(statusEnumProvider.notifier).state = StatusEnum.depositTicket;
                      break;
                    case StatusEnum.depositTicket:
                      if (formKey.currentState!.validate()) {
                        depositRequest(int.parse(depositController.text));
                      }
                      ref.read(stepperProvider.notifier).setCompleted(StatusEnum.depositTicket.index);
                      ref.read(stepperProvider.notifier).setIndex(StatusEnum.result.index);
                      ref.read(stepperProvider.notifier).setCompleted(StatusEnum.result.index);
                      break;
                    case StatusEnum.result:
                  }
                },
                controlsBuilder: (context, details) {
                  StatusEnum status = StatusEnum.values[details.currentStep];
                  switch (status) {
                    case StatusEnum.ticketRequest:
                      return CustomButton(
                        onPressed: details.onStepContinue,
                        label: 'Submit',
                      );
                    case StatusEnum.ticketResponse:
                      return CustomButton(
                        onPressed: details.onStepContinue,
                        label: 'Next',
                      );
                    case StatusEnum.depositTicket:
                      return CustomButton(
                        onPressed: details.onStepContinue,
                        label: 'Next',
                      );
                    case StatusEnum.result:
                      return const SizedBox();
                  }
                },
                steps: <Step>[
                  Step(
                    title: Text('Store Food Request',
                        style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                    state: _getState(model, StatusEnum.ticketRequest.index),
                    isActive: _getActive(model, StatusEnum.ticketRequest.index),
                    content: SpacedColumn(spacing: 8, crossAxisAlignment: CrossAxisAlignment.start, children: [
                      Text(
                        'Insert the amount of kg you want to deposit.',
                        style: TextStyle(color: Globals.textColor),
                      ),
                      Form(
                        key: formKey,
                        child: TextFormField(
                          controller: storeFoodController,
                          inputFormatters: <TextInputFormatter>[
                            FilteringTextInputFormatter.allow(RegExp(r'^(\d+)?\.?\d{0,2}')),
                          ],
                          autovalidateMode: AutovalidateMode.onUserInteraction,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter some digits';
                            }
                            return null;
                          },
                          decoration: const InputDecoration(
                              fillColor: Colors.white,
                              prefixIcon: Icon(Icons.scale),
                              border: OutlineInputBorder(borderRadius: BorderRadius.all(Radius.circular(32))),
                              hintText: 'How many kg?'),
                        ),
                      ),
                      const SizedBox(height: 16),
                    ]),
                  ),
                  Step(
                    title: Text('Rescue Ticket',
                        style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                    state: _getState(model, StatusEnum.ticketResponse.index),
                    isActive: _getActive(model, StatusEnum.ticketResponse.index),
                    content: SpacedColumn(
                      spacing: 8,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Save your ticket number to deposit food.', style: TextStyle(color: Colors.blue.shade900)),
                        Padding(
                          padding: const EdgeInsets.symmetric(vertical: 8),
                          child: Stack(
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
                                    decoration: BoxDecoration(color: Colors.blue.shade100, shape: BoxShape.circle),
                                  ),
                                ),
                              ),
                              Positioned(
                                left: -32,
                                child: SizedBox.square(
                                  dimension: 64,
                                  child: DecoratedBox(
                                    decoration: BoxDecoration(color: Colors.blue.shade100, shape: BoxShape.circle),
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
                                        style:
                                            TextStyle(fontWeight: FontWeight.w300, fontSize: 12, color: Colors.black45),
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
                          ),
                        )
                      ],
                    ),
                  ),
                  Step(
                    title: Text('Deposit Request',
                        style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                    state: _getState(model, StatusEnum.depositTicket.index),
                    isActive: _getActive(model, StatusEnum.depositTicket.index),
                    content: Padding(
                      padding: const EdgeInsets.symmetric(vertical: 8),
                      child: SpacedColumn(
                        spacing: 8,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('Insert your ticket to deposit food.', style: TextStyle(color: Colors.blue.shade900)),
                          TextField(
                            controller: depositController,
                            inputFormatters: [
                              FilteringTextInputFormatter.digitsOnly,
                            ],
                            decoration: const InputDecoration(
                                fillColor: Colors.white,
                                prefixIcon: Icon(Icons.numbers),
                                border: OutlineInputBorder(borderRadius: BorderRadius.all(Radius.circular(32))),
                                //labelText: 'Ticket Request',
                                hintText: 'Ticket Id'),
                          ),
                        ],
                      ),
                    ),
                  ),
                  Step(
                    title: Text('Result', style: TextStyle(color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                    state: _getState(model, StatusEnum.result.index),
                    isActive: _getActive(model, StatusEnum.result.index),
                    content: SpacedColumn(
                      spacing: 0,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Your request has been fulfilled!', style: TextStyle(color: Colors.blue.shade900)),
                      ],
                    ),
                  ),
                ],
              ),
              //), TODO: check
            ),
            //const Spacer()
          ],
        ),
      ),
    );
  }

  StepState _getState(StepperModel model, int index) {
    if (model.index == index) {
      return StepState.editing;
    } else if (model.index > index || model.index == 3) {
      return StepState.complete;
    } else {
      return StepState.disabled;
    }
  }

  bool _getActive(StepperModel model, int index) => _getState(model, index) != StepState.disabled;

  void storeFoodRequest(double quantity) {
    if (kDebugMode) {
      debugPrint('Store Food Request: $quantity');
    }
    stompClient.send(
      destination: '/app/store-food/$id',
      body: json.encode(StoreFoodRequestDTO(quantity: quantity).toJson()),
    );
  }

  void depositRequest(int ticket) {
    if (kDebugMode) {
      debugPrint('Deposit - Ticket id: $ticket');
    }
    stompClient.send(
      destination: '/app/deposit/$id',
      body: json.encode(TicketDTO(ticketNumber: ticket).toJson()),
    );
  }
}

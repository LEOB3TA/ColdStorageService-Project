import 'dart:convert';

import 'package:ServiceAccessGUI/model/stepper_model.dart';
import 'package:ServiceAccessGUI/model/store_food_request_dto.dart';
import 'package:ServiceAccessGUI/model/ticket_response_dto.dart';
import 'package:ServiceAccessGUI/model/weight_dto.dart';
import 'package:ServiceAccessGUI/providers/service_weight_provider.dart';
import 'package:ServiceAccessGUI/providers/status_provider.dart';
import 'package:ServiceAccessGUI/widgets/custom_button.dart';
import 'package:ServiceAccessGUI/widgets/spaced_column.dart';
import 'package:ServiceAccessGUI/widgets/spaced_row.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:logger/logger.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';
import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';
import 'package:uuid/uuid.dart';

const socketUrl = 'ws://localhost:11804/ws-message'; // if in local
//const socketUrl = 'ws://192.168.1.2:11804/ws-message'; // if in another pc

var logger = Logger(printer: PrettyPrinter());

class HomeView extends ConsumerStatefulWidget {
  const HomeView({super.key});

  @override
  ConsumerState<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends ConsumerState<HomeView> {
  late StompClient stompClient;
  int ticketNumber = -1;
  String id = const Uuid().v4();
  double curr = 0.0;
  double max = 0.0;

  final storeFoodFormKey = const Key('storeFoodRequest');
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
    print(frame.headers);
    //comment
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
        destination: '/topic/updates',
        callback: (StompFrame frame) {
          if (frame.body != null) {
            print("UPDATE :${frame.body!}");
            WeightDTO update = WeightDTO.fromJson(json.decode(frame.body!));
            print(update);
            setState(() {
              curr = update.getCurr;
              max = update.getMaxWeight;
            });
            ref.read(serviceWeightProvider).currentWeight = update.getCurr;
            ref.read(serviceWeightProvider).maxWeight = update.maxWeight;
            print(ref.read(serviceWeightProvider).toString());
          }
        });
    stompClient.subscribe(
        destination: '/user/queue/store-food/$id',
        callback: (StompFrame frame) {
          if (frame.body != null) {
            TicketResponseDTO result = TicketResponseDTO.fromJson(json.decode(frame.body!));
            setState(() {
              ticketNumber = result.ticketNumber;
            });
            ref.read(stepperProvider(false).notifier).setCompleted(StatusEnum.ticketRequest.index);
            ref.read(statusEnumProvider.notifier).state = StatusEnum.ticketResponse;
          }
        });
    stompClient.subscribe(
        destination: '/user/queue/deposit/$id',
        callback: (StompFrame frame) {
          if (frame.body != null) {
            print("RESULT: ${frame.headers!}");
            //String result = String.fromJson
            print("SS");
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

    updates();
  }

  final formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    final status = ref.watch(statusEnumProvider);
    final TextEditingController storeFoodController = TextEditingController();
    final TextEditingController depositController = TextEditingController();
    bool storeFoodValid = false;
    final TextEditingController _depositTicketController = TextEditingController();
    final model = ref.watch(stepperProvider(false));

    final percent = curr / max;
    final progressColor = percent < 0.5
        ? Colors.green
        : percent < 0.8
            ? Colors.orange
            : Colors.red;

    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SizedBox(
          height: double.maxFinite,
          child: DecoratedBox(
            decoration: BoxDecoration(
              color: Colors.transparent,
              borderRadius: BorderRadius.circular(16),
            ),
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: SpacedColumn(
                spacing: 8,
                children: [
                  Expanded(
                    flex: 1,
                    child: FittedBox(
                      fit: BoxFit.contain,
                      child: Text(
                        'SERVICEACCESSGUI',
                        style: GoogleFonts.vt323(
                            textStyle:
                                TextStyle(fontSize: 48, fontWeight: FontWeight.bold, color: Colors.blue.shade900)),
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 7,
                    child: SpacedRow(
                      spacing: 8,
                      children: [
                        // current weight start
                        Expanded(
                            child: SizedBox(
                          height: double.maxFinite,
                          width: double.maxFinite,
                          child: DecoratedBox(
                              decoration: BoxDecoration(
                                borderRadius: const BorderRadius.all(Radius.circular(16)),
                                color: Colors.grey.shade100,
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
                                            flex: 1,
                                            child: FittedBox(
                                              fit: BoxFit.contain,
                                              child: Text(
                                                'CURRENT WEIGHT',
                                                style: GoogleFonts.vt323(
                                                    textStyle: TextStyle(
                                                        fontWeight: FontWeight.w800, color: Colors.blue.shade900)),
                                              ),
                                            ),
                                          ),
                                          const Expanded(
                                            flex: 1,
                                            child: Padding(
                                              padding: EdgeInsets.symmetric(horizontal: 96.0),
                                              child: FittedBox(
                                                fit: BoxFit.fitWidth,
                                                child: Text(
                                                  'Stored in ColdRoom',
                                                  style: TextStyle(fontWeight: FontWeight.w500, color: Colors.black45),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                    Expanded(
                                        flex: 3,
                                        child: FittedBox(
                                          fit: BoxFit.contain,
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
                                                    fontWeight: FontWeight.w600, fontSize: 16, color: Colors.black26),
                                                children: <TextSpan>[
                                                  TextSpan(
                                                      text: '$curr\n',
                                                      style: TextStyle(
                                                          letterSpacing: 0,
                                                          fontWeight: FontWeight.w600,
                                                          fontSize: 32,
                                                          color: progressColor)),
                                                  TextSpan(
                                                      text: 'Out of $max kg',
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
                        //current weight end
                        Expanded(
                            flex: 2,
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
                                      //const Spacer(),

                                      Expanded(
                                        flex: 13,
                                        child: Stepper(
                                          type: StepperType.vertical,
                                          currentStep: model.index,
                                          onStepTapped: (index) =>
                                              ref.read(stepperProvider(false).notifier).setIndex(index),
                                          onStepCancel: () {
                                            if (status.index > 0) {
                                              ref.read(statusEnumProvider.notifier).state =
                                                  StatusEnum.values[status.index - 1];
                                            }
                                          },
                                          onStepContinue: () {
                                            ref.read(statusEnumProvider.notifier).state =
                                                StatusEnum.values[status.index + 1];
                                          },
                                          controlsBuilder: (context, details) => Padding(
                                            padding: const EdgeInsets.only(top: 16),
                                            child: SpacedColumn(
                                              crossAxisAlignment: CrossAxisAlignment.start,
                                              spacing: 8,
                                              children: [
                                                if (details.currentStep == StatusEnum.ticketRequest.index)
                                                  CustomButton(
                                                      onPressed: storeFoodValid
                                                          ? () =>
                                                              storeFoodRequest(double.parse(storeFoodController.text))
                                                          : null,
                                                      label: 'Submit'),
                                              ],
                                            ),
                                          ),
                                          steps: <Step>[
                                            Step(
                                              title: Text('Store Food Request',
                                                  style: TextStyle(
                                                      color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                                              state: _getState(model, StatusEnum.ticketRequest.index),
                                              isActive: _getActive(model, StatusEnum.ticketRequest.index),
                                              content: SpacedColumn(
                                                  spacing: 8,
                                                  crossAxisAlignment: CrossAxisAlignment.start,
                                                  children: [
                                                    Text(
                                                      'Insert the amount of kg you want to deposit.',
                                                      style: TextStyle(color: Colors.blue.shade900),
                                                    ),
                                                    Form(
                                                      key: formKey,
                                                      child: TextFormField(
                                                        controller: storeFoodController,
                                                        inputFormatters: [
                                                          FilteringTextInputFormatter.digitsOnly,
                                                        ],
                                                        onChanged: (value) {
                                                          print(value);
                                                          print(formKey.currentState?.validate() ?? false);
                                                          // storeFoodValid = storeFoodFormKey ?? false;
                                                        },
                                                        autovalidateMode: AutovalidateMode.onUserInteraction,
                                                        validator: (value) {
                                                          print(formKey.currentState.toString());
                                                          if (value == null || value.isEmpty) {
                                                            return 'Please enter some text';
                                                          }
                                                          return null;
                                                        },
                                                        decoration: const InputDecoration(
                                                            // errorText: 'Please enter a valid number',
                                                            fillColor: Colors.white,
                                                            prefixIcon: Icon(Icons.scale),
                                                            border: OutlineInputBorder(
                                                                borderRadius: BorderRadius.all(Radius.circular(32))),
                                                            //labelText: 'Ticket Request',
                                                            hintText: 'How many kg?'),
                                                      ),
                                                    ),
                                                    CustomButton(
                                                        onPressed: () {
                                                          if (storeFoodController.text.isNotEmpty &&
                                                              formKey.currentState!.validate()) {
                                                            storeFoodRequest(double.parse(storeFoodController.text));
                                                          }
                                                        },
                                                        label: 'Submit')
                                                  ]),
                                            ),
                                            Step(
                                              title: Text('Rescue Ticket',
                                                  style: TextStyle(
                                                      color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
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
                                                  style: TextStyle(
                                                      color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                                              content: SpacedColumn(
                                                spacing: 8,
                                                crossAxisAlignment: CrossAxisAlignment.start,
                                                children: [
                                                  Text('Insert your ticket to deposit food.',
                                                      style: TextStyle(color: Colors.blue.shade900)),
                                                  TextField(
                                                    controller: depositController,
                                                    decoration: const InputDecoration(
                                                        fillColor: Colors.white,
                                                        prefixIcon: Icon(Icons.numbers),
                                                        border: OutlineInputBorder(
                                                            borderRadius: BorderRadius.all(Radius.circular(32))),
                                                        //labelText: 'Ticket Request',
                                                        hintText: 'Ticket Id'),
                                                  ),
                                                  CustomButton(
                                                      onPressed: () =>
                                                          depositRequest(int.parse(depositController.text)),
                                                      //ref.read(statusEnumProvider.notifier).state = StatusEnum.result
                                                      label: 'Submit')
                                                ],
                                              ),
                                            ),
                                            Step(
                                              title: Text('Result',
                                                  style: TextStyle(
                                                      color: Colors.blue.shade900, fontWeight: FontWeight.bold)),
                                              content: SpacedColumn(
                                                spacing: 0,
                                                crossAxisAlignment: CrossAxisAlignment.start,
                                                children: [
                                                  Text('Congratulazioni ce l\'hai fatta !',
                                                      style: TextStyle(color: Colors.blue.shade900)),
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
            ),
          ),
        ),
      ),
    );
  }

  StepState _getState(StepperModel model, int index) {
    if (model.index == index) {
      return StepState.editing;
    } else if (model.index > index) {
      return StepState.complete;
    } else {
      return StepState.disabled;
    }
  }

  bool _getActive(StepperModel model, int index) => _getState(model, index) != StepState.disabled;

// quandoo mi connetto chiedo l'update
  void updates() {
    if (kDebugMode) {
      print("Update-------");
    }
    stompClient.send(
      destination: '/topic/updates',
    );
  }

  void storeFoodRequest(double quantity) {
    if (kDebugMode) {
      print('Store Food Request: $quantity');
    }
    stompClient.send(
      destination: '/app/store-food/$id',
      body: json.encode(StoreFoodRequestDTO(quantity: quantity).toJson()),
    );
  }

  void depositRequest(int ticket) {
    if (kDebugMode) {
      print('Deposit - Ticket id: $ticket');
    }
    stompClient.send(
      destination: '/app/deposit/$id',
      body: json.encode(TicketResponseDTO(ticketNumber: ticket).toJson()),
    );
  }
}

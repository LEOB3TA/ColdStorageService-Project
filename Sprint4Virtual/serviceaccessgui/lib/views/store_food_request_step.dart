import 'package:flutter/material.dart';

import '../widgets/layout/spaced_column.dart';

class StoreFoodRequestStep extends StatelessWidget {
  static const index = 0;

  const StoreFoodRequestStep({super.key});

  @override
  Widget build(BuildContext context) {
    return SpacedColumn(spacing: 8, crossAxisAlignment: CrossAxisAlignment.start, children: [
      Text(
        'Insert the amount of kg you want to deposit.',
        style: TextStyle(color: Colors.blue.shade900),
      ),
      /*
      TextFormField(
        controller: storeFoodController,
        autovalidateMode: AutovalidateMode.onUserInteraction,
        validator: FormBuilderValidators.compose([
          FormBuilderValidators.required(),
          FormBuilderValidators.numeric(),
          FormBuilderValidators.min(0),
        ]),
        decoration: const InputDecoration(
            // errorText: 'Please enter a valid number',
            fillColor: Colors.white,
            prefixIcon: Icon(Icons.scale),
            border: OutlineInputBorder(borderRadius: BorderRadius.all(Radius.circular(32))),
            //labelText: 'Ticket Request',
            hintText: 'How many kg?'),
      ),
      CustomButton(
          onPressed: storeFoodValid ? () => storeFoodRequest(double.parse(storeFoodController.text)) : null,
          label: 'Submit')
          */
    ]);
  }
}

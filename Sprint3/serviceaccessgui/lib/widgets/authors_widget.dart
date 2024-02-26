import 'package:flutter/material.dart';
import 'package:service_access_gui/globals.dart';

import 'layout/spaced_column.dart';
import 'layout/spaced_row.dart';

class AuthorsWidget extends StatelessWidget {
  const AuthorsWidget({super.key});

  @override
  Widget build(BuildContext context) {
    //return SizedBox(width: double.maxFinite, child: DecoratedBox(decoration: BoxDecoration(color: Globals.backgroundWidgetColor, borderRadius: BorderRadius.circular(16)), child: const Center(child: Text('Authors', style: TextStyle(fontSize: 24,),)),));
    return SizedBox(
        width: double.maxFinite,
        child: DecoratedBox(
          decoration: BoxDecoration(color: Globals.backgroundWidgetColor, borderRadius: BorderRadius.circular(16)),
          //child: const Center(child: Text('Authors', style: TextStyle(fontSize: 24,),))
          child: const Padding(
            padding: const EdgeInsets.all(16),
            child: Center(
              child: SpacedColumn(
                spacing: 8,
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Text('Authors', style: TextStyle(fontWeight: FontWeight.bold)),
                  SpacedRow(
                    spacing: 8,
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [Text('Leonardo Focardi'), Text('Christian Galeone'), Text('Gianmiriano Porrazzo')],
                  ),
                ],
              ),
            ),
          ),
        ));
  }
}

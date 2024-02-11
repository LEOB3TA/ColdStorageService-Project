import 'package:flutter/material.dart';
import 'package:servicestatusgui/globals.dart';

class AuthorsWidget extends StatelessWidget {
  const AuthorsWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(width: double.maxFinite, child: DecoratedBox(decoration: BoxDecoration(color: Globals.backgroundWidgetColor, borderRadius: BorderRadius.circular(16)), child: const Center(child: Text('Authors', style: TextStyle(fontSize: 24,),)),));
  }
}

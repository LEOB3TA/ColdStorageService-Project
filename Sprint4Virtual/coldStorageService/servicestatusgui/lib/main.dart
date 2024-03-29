import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:servicestatusgui/pages/home_page.dart';

void main() {
  String url = setupUrl();
  runApp(ProviderScope(child: MyApp(url: url)));
}

String setupUrl() {
  const ip = String.fromEnvironment("IP", defaultValue: "localhost");
  const port = int.fromEnvironment("PORT", defaultValue: 11804);
  debugPrint("IP: $ip");
  return "ws://$ip:$port/ws-message";
}

class MyApp extends StatelessWidget {
  final String url;
  const MyApp({super.key, required this.url});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ServiceStatusGUI',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: HomePage(url: url),
    );
  }
}

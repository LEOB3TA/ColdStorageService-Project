import 'package:ServiceAccessGUI/views/home_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

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
      title: 'ServiceAccess',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        // This is the theme of your application.
        //
        // TRY THIS: Try running your application with "flutter run". You'll see
        // the application has a blue toolbar. Then, without quitting the app,
        // try changing the seedColor in the colorScheme below to Colors.green
        // and then invoke "hot reload" (save your changes or press the "hot
        // reload" button in a Flutter-supported IDE, or press "r" if you used
        // the command line to start the app).
        //
        // Notice that the counter didn't reset back to zero; the application
        // state is not lost during the reload. To reset the state, use hot
        // restart instead.
        //
        // This works for code too, not just values: Most code changes can be
        // tested with just a hot reload.
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: HomeView(url: url),
    );
  }
}

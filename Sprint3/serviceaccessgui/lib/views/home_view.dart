import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:logger/logger.dart';
import 'package:service_access_gui/globals.dart';
import 'package:service_access_gui/responsive/desktop_body.dart';
import 'package:service_access_gui/responsive/mobile_body.dart';
import 'package:service_access_gui/responsive/responsive_layout.dart';

var logger = Logger(printer: PrettyPrinter());

class HomeView extends ConsumerStatefulWidget {
  final String url;
  const HomeView({super.key, required this.url});

  @override
  ConsumerState<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends ConsumerState<HomeView> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          'SERVICEACCESSGUI',
          style: GoogleFonts.vt323(
              textStyle: TextStyle(fontSize: 64, fontWeight: FontWeight.bold, color: Globals.headerColor)),
        ),
        backgroundColor: Globals.backgroundColor,
        toolbarHeight: 96,
      ),
      body: ResponsiveLayout(
        mobile: MobileBody(url: widget.url),
        desktop: DesktopBody(url: widget.url),
      ),
    );
  }
}

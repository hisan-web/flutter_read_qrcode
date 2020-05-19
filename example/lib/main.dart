import 'package:flutter/material.dart';
import 'dart:async';

import 'package:readqrcode/readqrcode.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    init();
  }

  @override
  void dispose() {
    unInit();
    super.dispose();
  }

  Future<void> init() async {
    bool result = await Readqrcode.init();
    if (result) {
      print("init success");
      Readqrcode.dataStreamListen((data) {
        print(data);
      });
    }
  }

  Future<void> unInit() async {
    await Readqrcode.unInit();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}

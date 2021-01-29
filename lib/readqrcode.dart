import 'dart:async';

import "package:flutter/services.dart";

class Readqrcode {
  static const MethodChannel _channel = const MethodChannel('com.hs.flutter/readQrCodeMethodChannel');
  static const EventChannel _eventChannel = const EventChannel('com.hs.flutter/readQrCodeEventChannel');

  /// 检测扫码结果数据
  static void dataStreamListen(dynamic success, {dynamic error}) {
    _eventChannel.receiveBroadcastStream().listen((data) {
      success(data);
    }, onError: error);
  }

  static Future<bool> init(String path) async {
    try {
      await _channel.invokeMethod('init',{'path': path});
      return true;
    } catch(e) {
      print(e.toString());
      return false;
    }
  }

  static Future<void> unInit() async {
    await _channel.invokeMethod('unInit');
  }
}

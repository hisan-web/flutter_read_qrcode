# 这是一个flutter实现串口二维码阅读器读取数据的插件

## 使用方法

### 使用注意

最小依赖sdk必须为19

### 加载依赖
```
readqrcode:
    git:
        https://github.com/hisan-web/flutter_read_qrcode
```

### 使用demo
```
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

```
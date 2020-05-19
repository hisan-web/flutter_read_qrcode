package com.hs.flutter.readqrcode;

import android.util.Log;

import androidx.annotation.NonNull;

import com.hs.flutter.readqrcode.impl.StreamHandlerImpl;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import top.keepempty.sph.library.SerialPortConfig;
import top.keepempty.sph.library.SerialPortHelper;
import top.keepempty.sph.library.SphCmdEntity;
import top.keepempty.sph.library.SphResultCallback;

/** ReadqrcodePlugin */
public class ReadqrcodePlugin implements FlutterPlugin, MethodCallHandler{

  private static final String TAG = "ReadqrcodePlugin";
  private static final String METHOD_CHANNEL = "com.hs.flutter/readQrCodeMethodChannel";
  private static final String EVENT_CHANNEL = "com.hs.flutter/readQrCodeEventChannel";
  private SerialPortHelper serialPortHelper;

  private StreamHandlerImpl streamHandlerImpl;

  public static void registerWith(Registrar registrar) {
    final ReadqrcodePlugin instance = new ReadqrcodePlugin();
    instance.pluginRegister(registrar.messenger());
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    pluginRegister(binding.getBinaryMessenger());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    pluginDestroy();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "init":
        serialPortHandler(result);
        break;
      case "unInit":
        pluginDestroy();
        break;
      default:
        result.notImplemented();
    }
  }


  // 注册插件
  private void pluginRegister(BinaryMessenger messenger) {
    final MethodChannel channel = new MethodChannel(messenger, METHOD_CHANNEL);
    channel.setMethodCallHandler(this);
    streamHandlerImpl = new StreamHandlerImpl(messenger, EVENT_CHANNEL);
  }

  // 插件销毁
  private void pluginDestroy() {
    if (serialPortHelper!=null) {
      serialPortHelper.closeDevice();
    }
  }

  /**
   * 处理串口
   * @param result
   */
  private void serialPortHandler(Result result) {
    // 窗口配置
    SerialPortConfig serialPortConfig = new SerialPortConfig();
    serialPortConfig.path = "dev/ttyS3";
    // 初始化串口
    serialPortHelper = new SerialPortHelper(32, false);
    // 设置串口参数
    serialPortHelper.setConfigInfo(serialPortConfig);
    // 开启串口
    boolean isOpen = serialPortHelper.openDevice();
    if(!isOpen){
//      Log.d(TAG, "打开串口失败");
      result.error("-1","打开串口失败", null);
      return;
    }
//    Log.d(TAG, "打开串口成功");
    result.success(true);
    // 数据接收回调
    serialPortHelper.setSphResultCallback(new SphResultCallback() {
      @Override
      public void onSendData(SphCmdEntity sendCom) {
//        Log.d(TAG, "发送命令：" + sendCom.commandsHex);
      }

      @Override
      public void onReceiveData(SphCmdEntity data) {
        String result = "";
        try {
          result = fromHex2String(data.commandsHex);
//          Log.d(TAG, "收到命令：" + result);
        } catch (Exception e) {
          e.printStackTrace();
        }
        Log.i(TAG,""+streamHandlerImpl);
        if (null!=streamHandlerImpl) {
          streamHandlerImpl.eventSinkSuccess(result);
        }
      }

      @Override
      public void onComplete() {
        Log.d(TAG, "完成");
      }
    });
  }

  // hex转String
  private String fromHex2String(String hexString) throws Exception {
    String result = "";
    hexString = hexString.toUpperCase();
    String hexDigital = "0123456789ABCDEF";
    char[] hexs = hexString.toCharArray();
    byte[] bytes = new byte[hexString.length() / 2];
    int n;
    for (int i=0; i<bytes.length; i++) {
      n = hexDigital.indexOf(hexs[2 * i]) * 16 + hexDigital.indexOf(hexs[2 * i + 1]);
      bytes[i] = (byte) (n & 0xff);
    }
    result = new String(bytes, "UTF-8");
    return result;
  }
}

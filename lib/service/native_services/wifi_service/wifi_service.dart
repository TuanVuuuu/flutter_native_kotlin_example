import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_native_kotlin_example/service/native_services/method_channel.dart';

class WifiService {
  static final MethodChannel _channel = MethodChannel(MethodChannelMethods.GET_WIFI_NAME.method);

  static Future<String?> getWifiName() async {
    try {
      final String? wifiName = await _channel.invokeMethod(MethodChannelMethods.GET_WIFI_NAME.method);
      return wifiName;
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print("Failed to get wifi name: '${e.message}'.");
      }
      return null;
    }
  }
}

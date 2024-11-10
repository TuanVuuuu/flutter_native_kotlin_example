import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_native_kotlin_example/service/native_services/method_channel.dart';

class BatteryLevelService {
  static final MethodChannel _channel = MethodChannel(MethodChannelMethods.GET_BATTERY_LEVEL.method);

  static Future<int?> getBatteryLevel() async {
    try {
      final int? batteryLevel = await _channel.invokeMethod(MethodChannelMethods.GET_BATTERY_LEVEL.method);
      return batteryLevel;
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print("Failed to get battery level: '${e.message}'.");
      }
      return null;
    }
  }
}
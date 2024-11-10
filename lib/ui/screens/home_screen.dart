import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_native_kotlin_example/service/native_services/battery_level_service/battery_level_service.dart';
import 'package:flutter_native_kotlin_example/service/native_services/wifi_service/wifi_service.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int? batteryLevel = 0;
  String? wifiName = "";

  Future<void> _getBatteryLevel() async {
    final level = await BatteryLevelService.getBatteryLevel();
    if (kDebugMode) {
      print("Battery level: $level%");
    }
    setState(() {
      batteryLevel = level;
    });
  }

  Future<void> _getWifiName() async {
    final name = await WifiService.getWifiName();
    if (kDebugMode) {
      print("Wifi name: $name");
    }

    setState(() {
      wifiName = name;
    });
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            _buildButtonAndContent(
              content: batteryLevel != null ? "$batteryLevel%" : "Battery level unavailable",
              buttonText: "Get Battery Level",
              onClick: () {
                _getBatteryLevel();
              },
            ),
            _buildButtonAndContent(
              content: wifiName != null ? "$wifiName" : "Wifi name unavailable",
              buttonText: "Get Wifi Name",
              onClick: () {
                _getWifiName();
              },
            ),
          ],
        ),
      ),
    );
  }

  Column _buildButtonAndContent({
    required String buttonText,
    required String content,
    required Function() onClick,
  }) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Text(
          content,
          style: const TextStyle(color: Colors.red, fontSize: 24),
        ),
        const SizedBox(height: 20),
        ElevatedButton(
          onPressed: () {
            onClick.call();
          },
          child: Text(buttonText),
        ),
      ],
    );
  }
}

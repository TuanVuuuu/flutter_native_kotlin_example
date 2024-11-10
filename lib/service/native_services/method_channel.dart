// ignore_for_file: constant_identifier_names

enum MethodChannelMethods {
  GET_BATTERY_LEVEL('getBatteryLevel'),
  GET_WIFI_NAME('getWifiName');

  final String methodName;

  const MethodChannelMethods(this.methodName);

  // Getter để lấy giá trị methodName
  String get method => methodName;
}


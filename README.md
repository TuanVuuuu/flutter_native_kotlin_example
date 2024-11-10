# flutter_native_kotlin_example

## Giới thiệu

Dự án này minh họa cách sử dụng **Method Channel** để giao tiếp giữa `Flutter` (`Dart`) và mã `Native` (`Kotlin`) trên `Android`. Cụ thể, ứng dụng này sử dụng `Method Channel` để gọi phương thức từ Flutter và nhận kết quả trả về từ mã `Native` (ví dụ: lấy tên `Wi-Fi`).

## Cài đặt

### 1. Cấu hình Method Channel trong Flutter (Dart)

Trong `Flutter`, sử dụng `MethodChannel` để gửi và nhận dữ liệu từ mã `Native`. Đầu tiên, bạn cần khai báo `MethodChannel` với một tên riêng và gọi các phương thức từ `Native`.

#### Ví dụ Flutter (Dart):

```dart
import 'package:flutter/services.dart';

class WifiService {
  static const MethodChannel _channel = MethodChannel('com.example.flutter_native_kotlin_example/wifi');

  static Future<String?> getWifiName() async {
    try {
      // Gọi phương thức native để lấy tên Wi-Fi
      final String? wifiName = await _channel.invokeMethod('getWifiName');
      return wifiName;
    } on PlatformException catch (e) {
      print("Lỗi khi lấy tên Wi-Fi: '${e.message}'");
      return null;
    }
  }
}
```

### 2. Cấu hình Method Channel trong mã Native (Kotlin)
Trong mã `Native` (Android - Kotlin), tạo một `MethodChannel` với tên tương ứng và xử lý yêu cầu từ `Flutter`.

#### Ví dụ Native (Kotlin):

```kotlin
import android.content.Context
import android.net.wifi.WifiManager
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.android.FlutterActivity

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.flutter_native_kotlin_example/wifi"

    override fun configureFlutterEngine() {
        super.configureFlutterEngine()

        // Thiết lập MethodChannel
        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "getWifiName") {
                val wifiName = getWifiName()
                if (wifiName != null) {
                    result.success(wifiName)  // Trả kết quả Wi-Fi cho Flutter
                } else {
                    result.error("UNAVAILABLE", "Không thể lấy tên Wi-Fi", null)
                }
            } else {
                result.notImplemented()  // Phương thức không được hỗ trợ
            }
        }
    }

    // Lấy tên Wi-Fi
    private fun getWifiName(): String? {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return if (wifiInfo.ssid != "<unknown ssid>") wifiInfo.ssid else null
    }
}
```

### 3. Gọi phương thức từ Flutter và nhận kết quả
Khi người dùng gọi phương thức `getWifiName()` trong `Flutter`, `Flutter` sẽ gửi yêu cầu đến mã `Native` thông qua `MethodChannel` và nhận kết quả trả về.

#### Ví dụ gọi hàm từ Flutter (Dart):
```dart
void getWifiName() async {
  String? wifiName = await WifiService.getWifiName();
  if (wifiName != null) {
    print('Tên Wi-Fi: $wifiName');
  } else {
    print('Không thể lấy tên Wi-Fi');
  }
}
```

### Quyền truy cập Wi-Fi

Để lấy tên `Wi-Fi`, ứng dụng cần quyền truy cập `Wi-Fi` và vị trí. Đảm bảo bạn đã thêm quyền vào `AndroidManifest.xml`:

```kotlin
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

Bên cạnh đó, trên `Android 6.0 (API 23)` trở lên, bạn cần yêu cầu quyền `runtime` để có thể truy cập vị trí và lấy tên `Wi-Fi`.

### Tóm tắt quy trình
 1. `Flutter` gọi `MethodChannel`: `Flutter` gửi yêu cầu đến `Native` thông qua `MethodChannel`, ví dụ gọi phương thức `getWifiName`.
 2. `Native` xử lý yêu cầu: Mã `Native` (`Kotlin`) nhận yêu cầu và thực hiện các thao tác như lấy tên `Wi-Fi`.
 3. Native trả kết quả về `Flutter`: Sau khi xử lý xong, mã `Native` trả kết quả về `Flutter` (ví dụ: tên `Wi-Fi` hoặc lỗi nếu không lấy được `Wi-Fi`).
 4. `Flutter` nhận kết quả: `Flutter` nhận kết quả trả về từ `Native` và thực hiện các thao tác tiếp theo.

 ### Giải thích:

1. **Flutter Side**: Trong phần `Flutter`, `MethodChannel` được khai báo để gọi phương thức từ `Native` và nhận kết quả trả về.
2. **Native Side**: Trong mã `Native` (`Kotlin`), `MethodChannel` nhận các yêu cầu từ `Flutter` và trả kết quả về thông qua `success()` hoặc `error()`.
3. **Quyền truy cập**: Bạn cần phải thêm quyền vào `AndroidManifest.xml` và yêu cầu quyền `runtime` để có thể lấy tên `Wi-Fi`.
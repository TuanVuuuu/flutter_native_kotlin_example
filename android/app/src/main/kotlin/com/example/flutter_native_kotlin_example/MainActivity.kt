package com.example.flutter_native_kotlin_example

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import android.content.pm.PackageManager
import android.widget.Toast
import com.example.flutter_native_kotlin_example.utils.WifiManagerHelper
import com.example.flutter_native_kotlin_example.utils.MethodChannelManager

class MainActivity : FlutterActivity() {

    private lateinit var methodChannelManager: MethodChannelManager

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        methodChannelManager = MethodChannelManager(flutterEngine, this)
        methodChannelManager.setUpMethodChannel() // Cấu hình MethodChannel
    }

    // Xử lý kết quả yêu cầu quyền
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WifiManagerHelper.REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, lấy tên Wi-Fi ngay lập tức
                methodChannelManager.getWifiName { wifiName ->
                    if (wifiName != null) {
                        // Gửi tên Wi-Fi về Flutter
                        methodChannelManager.sendWifiNameToFlutter(wifiName)
                    } else {
                        // Nếu wifiName là null, gửi thông báo lỗi về Flutter
                        methodChannelManager.sendWifiNameToFlutter("Unable to get WiFi name")
                    }
                }
            } else {
                // Người dùng từ chối quyền
                methodChannelManager.sendWifiNameToFlutter("Permission denied. Cannot access WiFi SSID")
            }
        }
    }
    
}



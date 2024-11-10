package com.example.flutter_native_kotlin_example.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class WifiManagerHelper(private val context: Context) {

    companion object {
        const val REQUEST_CODE = 1001
    }

    // Kiểm tra quyền và lấy SSID
    fun getWifiName(callback: (String?) -> Unit) {
        val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Kiểm tra quyền ACCESS_FINE_LOCATION trước khi truy cập SSID
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Nếu chưa có quyền, yêu cầu quyền truy cập
                requestPermission()
            }
        }

        // Nếu là Android 10 trở lên, cần xử lý quyền đặc biệt
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val currentSSID = wifiManager.connectionInfo.ssid
            if (currentSSID != null && currentSSID != "<unknown ssid>") {

                callback.invoke(currentSSID.replace("\"", ""))
            } else {
                callback.invoke("N/A (Restricted on Android 10+ without permission)")
            }
        } else {
            // Đối với các phiên bản Android dưới 10, lấy SSID như bình thường
            callback.invoke(wifiManager.connectionInfo.ssid?.replace("\"", ""))
        }
    }

    // Yêu cầu quyền truy cập ACCESS_FINE_LOCATION
    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
        ) {
            // Nếu cần, hiển thị giải thích cho người dùng về lý do yêu cầu quyền
            Toast.makeText(context, "Permission required to access WiFi SSID", Toast.LENGTH_LONG)
                    .show()
        }
        ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
        )
    }
}

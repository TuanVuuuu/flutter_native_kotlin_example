package com.example.flutter_native_kotlin_example.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.FlutterEngine

class MethodChannelManager(
    flutterEngine: FlutterEngine,
    context: Context
) {
    private val methodChannelBattery = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, MethodChannelMethods.GET_BATTERY_LEVEL.methodName)
    val methodChannelWifi = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, MethodChannelMethods.GET_WIFI_NAME.methodName)
    
    private val batteryManagerHelper = BatteryManagerHelper(context)
    private val wifiManagerHelper = WifiManagerHelper(context)

    fun setUpMethodChannel() {
        methodChannelBattery.setMethodCallHandler { call, result ->
            when (call.method) {
                MethodChannelMethods.GET_BATTERY_LEVEL.methodName -> handleGetBatteryLevel(result)
                else -> result.notImplemented()
            }
        }

        methodChannelWifi.setMethodCallHandler { call, result ->
            when (call.method) {
                MethodChannelMethods.GET_WIFI_NAME.methodName -> handleGetWifiName(result)
                else -> result.notImplemented()
            }
        }
    }

    // Lấy tên Wi-Fi
    fun getWifiName(callback: (String?) -> Unit) {
        wifiManagerHelper.getWifiName(callback)
    }

    // Gửi tên Wi-Fi về Flutter
    fun sendWifiNameToFlutter(wifiName: String) {
        methodChannelWifi.invokeMethod(MethodChannelMethods.GET_WIFI_NAME.methodName, wifiName)
    }

    private fun handleGetBatteryLevel(result: MethodChannel.Result) {
        val batteryLevel = batteryManagerHelper.getBatteryLevel()
        if (batteryLevel != -1) {
            result.success(batteryLevel)
        } else {
            result.error("UNAVAILABLE", "Battery level not available.", null)
        }
    }

    private fun handleGetWifiName(result: MethodChannel.Result) {
        wifiManagerHelper.getWifiName { wifiName ->
            if (wifiName != null) {
                result.success(wifiName)
            } else {
                result.error("UNAVAILABLE", "WiFi name not available.", null)
            }
        }
    }
}

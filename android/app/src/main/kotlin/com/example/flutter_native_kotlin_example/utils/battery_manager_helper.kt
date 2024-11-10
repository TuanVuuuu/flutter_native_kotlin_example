
package com.example.flutter_native_kotlin_example.utils

import android.content.Context
import android.os.BatteryManager
import android.os.Build
// Helper class để lấy thông tin về PIN
class BatteryManagerHelper(private val context: Context) {
    fun getBatteryLevel(): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            -1
        }
    }
}
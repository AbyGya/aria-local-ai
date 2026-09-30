package app.knotwork.android.data.tools.local

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemTool @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun openApp(packageName: String): Result<String, String> {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                Result.Success("Opened app: $packageName")
            } else {
                Result.Error("App not found: $packageName")
            }
        } catch (e: Exception) {
            Result.Error("Failed to open app: ${e.message}")
        }
    }

    fun openSettings(settingsAction: String = Settings.ACTION_SETTINGS): Result<String, String> {
        return try {
            val intent = Intent(settingsAction).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Result.Success("Opened settings")
        } catch (e: Exception) {
            Result.Error("Failed to open settings: ${e.message}")
        }
    }

    fun openUrl(url: String): Result<String, String> {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Result.Success("Opened URL: $url")
        } catch (e: Exception) {
            Result.Error("Failed to open URL: ${e.message}")
        }
    }

    fun setAlarm(hour: Int, minute: Int, message: String): Result<String, String> {
        return try {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_HOUR, hour)
                putExtra(AlarmClock.EXTRA_MINUTES, minute)
                putExtra(AlarmClock.EXTRA_MESSAGE, message)
                putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Result.Success("Alarm set for $hour:$minute")
        } catch (e: Exception) {
            Result.Error("Failed to set alarm: ${e.message}")
        }
    }

    fun getWifiStatus(): Result<WifiStatus, String> {
        return try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val enabled = wifiManager.isWifiEnabled
            val connectionInfo = wifiManager.connectionInfo
            Result.Success(
                WifiStatus(
                    enabled = enabled,
                    ssid = connectionInfo.ssid?.removeSurrounding("\"") ?: "",
                    signalLevel = connectionInfo.rssi,
                    linkSpeed = connectionInfo.linkSpeed,
                )
            )
        } catch (e: Exception) {
            Result.Error("Failed to get WiFi status: ${e.message}")
        }
    }

    fun toggleWifi(enable: Boolean): Result<String, String> {
        return try {
            @Suppress("DEPRECATION")
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            @Suppress("DEPRECATION")
            val success = wifiManager.setWifiEnabled(enable)
            if (success) {
                Result.Success("WiFi ${if (enable) "enabled" else "disabled"}")
            } else {
                Result.Error("Failed to toggle WiFi")
            }
        } catch (e: Exception) {
            Result.Error("Failed to toggle WiFi: ${e.message}")
        }
    }
}

data class WifiStatus(
    val enabled: Boolean,
    val ssid: String,
    val signalLevel: Int,
    val linkSpeed: Int,
)

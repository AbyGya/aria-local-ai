package app.knotwork.android.data.tools.local

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.AlarmClock
import android.provider.Settings
import app.knotwork.android.domain.models.AppError
import app.knotwork.android.domain.models.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private object SystemToolError : AppError.System

/**
 * System tool: launches apps, opens settings and URLs, sets alarms, and
 * inspects or toggles Wi-Fi.
 *
 * Every method here dispatches a platform `Intent` rather than reaching into
 * system services directly, so the platform's own permission and user-consent
 * rules still apply on top of the agent's confirmation gate. Toggling Wi-Fi is
 * the exception: it is a deprecated API that only a device owner or a
 * system-signed app may call, so on a normal install it returns a refusal
 * instead of silently doing nothing.
 */
@Singleton
class SystemTool @Inject constructor(@ApplicationContext private val context: Context) {
    fun openApp(packageName: String): Result<String, AppError> = try {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            Result.Success("Opened app: $packageName")
        } else {
            Result.Error(error = SystemToolError, message = "App not found: $packageName")
        }
    } catch (e: Exception) {
        Result.Error(error = SystemToolError, message = "Failed to open app: ${e.message}", throwable = e)
    }

    fun openSettings(settingsAction: String = Settings.ACTION_SETTINGS): Result<String, AppError> = try {
        val intent = Intent(settingsAction).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
        Result.Success("Opened settings")
    } catch (e: Exception) {
        Result.Error(error = SystemToolError, message = "Failed to open settings: ${e.message}", throwable = e)
    }

    fun openUrl(url: String): Result<String, AppError> = try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
        Result.Success("Opened URL: $url")
    } catch (e: Exception) {
        Result.Error(error = SystemToolError, message = "Failed to open URL: ${e.message}", throwable = e)
    }

    fun setAlarm(hour: Int, minute: Int, message: String): Result<String, AppError> = try {
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
        Result.Error(error = SystemToolError, message = "Failed to set alarm: ${e.message}", throwable = e)
    }

    fun getWifiStatus(): Result<WifiStatus, AppError> = try {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        @Suppress("DEPRECATION")
        val enabled = wifiManager.isWifiEnabled

        @Suppress("DEPRECATION")
        val connectionInfo = wifiManager.connectionInfo
        Result.Success(
            WifiStatus(
                enabled = enabled,
                ssid = connectionInfo.ssid?.removeSurrounding("\"") ?: "",
                signalLevel = connectionInfo.rssi,
                linkSpeed = connectionInfo.linkSpeed,
            ),
        )
    } catch (e: Exception) {
        Result.Error(error = SystemToolError, message = "Failed to get WiFi status: ${e.message}", throwable = e)
    }

    fun toggleWifi(enable: Boolean): Result<String, AppError> = try {
        @Suppress("DEPRECATION")
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        @Suppress("DEPRECATION")
        val success = wifiManager.setWifiEnabled(enable)
        if (success) {
            Result.Success("WiFi ${if (enable) "enabled" else "disabled"}")
        } else {
            Result.Error(error = SystemToolError, message = "Failed to toggle WiFi")
        }
    } catch (e: Exception) {
        Result.Error(error = SystemToolError, message = "Failed to toggle WiFi: ${e.message}", throwable = e)
    }
}

data class WifiStatus(val enabled: Boolean, val ssid: String, val signalLevel: Int, val linkSpeed: Int)

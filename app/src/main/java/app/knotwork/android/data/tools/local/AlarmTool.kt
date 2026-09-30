package app.knotwork.android.data.tools.local

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmTool @Inject constructor(
    @ApplicationContext private val context: Context,
) {
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
            Result.Success("Alarm set for ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")
        } catch (e: Exception) {
            Result.Error("Failed to set alarm: ${e.message}")
        }
    }

    fun showAlarms(): Result<String, String> {
        return try {
            val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Result.Success("Showing alarms")
        } catch (e: Exception) {
            Result.Error("Failed to show alarms: ${e.message}")
        }
    }
}

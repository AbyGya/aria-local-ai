package app.knotwork.android.data.tools.local

import android.app.NotificationManager
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationTool @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getActiveNotifications(): Result<List<ActiveNotification>, String> {
        return try {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifications = nm.activeNotifications.map { sbn ->
                ActiveNotification(
                    packageName = sbn.packageName,
                    title = sbn.notification.extras.getString("android.title") ?: "",
                    text = sbn.notification.extras.getString("android.text") ?: "",
                    postTime = sbn.postTime,
                )
            }
            Result.Success(notifications)
        } catch (e: Exception) {
            Result.Error("Failed to get notifications: ${e.message}")
        }
    }

    fun dismissNotification(key: String): Result<String, String> {
        return try {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.cancel(key)
            Result.Success("Notification dismissed")
        } catch (e: Exception) {
            Result.Error("Failed to dismiss notification: ${e.message}")
        }
    }
}

data class ActiveNotification(
    val packageName: String,
    val title: String,
    val text: String,
    val postTime: Long,
)

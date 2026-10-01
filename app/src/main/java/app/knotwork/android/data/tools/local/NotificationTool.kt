package app.knotwork.android.data.tools.local

import android.app.NotificationManager
import android.content.Context
import app.knotwork.android.domain.models.AppError
import app.knotwork.android.domain.models.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private object NotificationToolError : AppError.System

@Singleton
class NotificationTool @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getActiveNotifications(): Result<List<ActiveNotification>, AppError> {
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
            Result.Error(error = NotificationToolError, message = "Failed to get notifications: ${e.message}", throwable = e)
        }
    }

    fun dismissNotification(key: String): Result<String, AppError> {
        return try {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                nm.cancel(key.hashCode())
            }
            Result.Success("Notification dismissed")
        } catch (e: Exception) {
            Result.Error(error = NotificationToolError, message = "Failed to dismiss notification: ${e.message}", throwable = e)
        }
    }
}

data class ActiveNotification(
    val packageName: String,
    val title: String,
    val text: String,
    val postTime: Long,
)

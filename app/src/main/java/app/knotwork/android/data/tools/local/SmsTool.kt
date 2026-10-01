package app.knotwork.android.data.tools.local

import android.content.Context
import android.telephony.SmsManager
import app.knotwork.android.domain.models.AppError
import app.knotwork.android.domain.models.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private object SmsToolError : AppError.System

/**
 * SMS tool: reads the inbox and sends a message.
 *
 * Both entry points are reached only through the agent's tool-calling path, so
 * a send is gated by the human-in-the-loop confirmation like any other tool
 * that leaves the device. The underlying `SmsManager` call still needs the
 * runtime SEND_SMS / READ_SMS grants; a `SecurityException` surfaces as a
 * `Result.Error` rather than propagating, so a refused permission reads as a
 * tool failure the agent can explain instead of a crashed run.
 */
@Singleton
class SmsTool @Inject constructor(@ApplicationContext private val context: Context) {
    fun sendSms(phoneNumber: String, message: String): Result<String, AppError> = try {
        val smsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            @Suppress("DEPRECATION")
            SmsManager.getDefault()
        }
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Result.Success("SMS sent to $phoneNumber")
    } catch (e: Exception) {
        Result.Error(error = SmsToolError, message = "Failed to send SMS: ${e.message}", throwable = e)
    }

    fun readSms(): Result<List<SmsMessage>, AppError> = try {
        val cursor = context.contentResolver.query(
            android.net.Uri.parse("content://sms/inbox"),
            null,
            null,
            null,
            "date DESC LIMIT 20",
        )
        val messages = mutableListOf<SmsMessage>()
        cursor?.use {
            val addressIdx = it.getColumnIndex("address")
            val bodyIdx = it.getColumnIndex("body")
            val dateIdx = it.getColumnIndex("date")
            while (it.moveToNext()) {
                messages.add(
                    SmsMessage(
                        address = if (addressIdx >= 0) it.getString(addressIdx) else "",
                        body = if (bodyIdx >= 0) it.getString(bodyIdx) else "",
                        date = if (dateIdx >= 0) it.getLong(dateIdx) else 0L,
                    ),
                )
            }
        }
        Result.Success(messages)
    } catch (e: Exception) {
        Result.Error(error = SmsToolError, message = "Failed to read SMS: ${e.message}", throwable = e)
    }
}

data class SmsMessage(val address: String, val body: String, val date: Long)

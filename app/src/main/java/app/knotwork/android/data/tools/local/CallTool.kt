package app.knotwork.android.data.tools.local

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallTool @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun makeCall(phoneNumber: String): Result<String, String> {
        return try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Result.Success("Calling $phoneNumber")
        } catch (e: Exception) {
            Result.Error("Failed to make call: ${e.message}")
        }
    }

    fun readCallLog(): Result<List<CallLogEntry>, String> {
        return try {
            val cursor = context.contentResolver.query(
                android.provider.CallLog.Calls.CONTENT_URI,
                null, null, null, "${android.provider.CallLog.Calls.DATE} DESC LIMIT 20"
            )
            val entries = mutableListOf<CallLogEntry>()
            cursor?.use {
                val numberIdx = it.getColumnIndex(android.provider.CallLog.Calls.NUMBER)
                val typeIdx = it.getColumnIndex(android.provider.CallLog.Calls.TYPE)
                val dateIdx = it.getColumnIndex(android.provider.CallLog.Calls.DATE)
                val durationIdx = it.getColumnIndex(android.provider.CallLog.Calls.DURATION)
                while (it.moveToNext()) {
                    entries.add(
                        CallLogEntry(
                            number = if (numberIdx >= 0) it.getString(numberIdx) else "",
                            type = if (typeIdx >= 0) it.getInt(typeIdx) else 0,
                            date = if (dateIdx >= 0) it.getLong(dateIdx) else 0L,
                            duration = if (durationIdx >= 0) it.getLong(durationIdx) else 0L,
                        )
                    )
                }
            }
            Result.Success(entries)
        } catch (e: Exception) {
            Result.Error("Failed to read call log: ${e.message}")
        }
    }
}

data class CallLogEntry(
    val number: String,
    val type: Int,
    val date: Long,
    val duration: Long,
)

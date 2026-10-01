package app.knotwork.android.data.tools.local

import app.knotwork.android.domain.models.AppError
import app.knotwork.android.domain.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private object SmartHomeError : AppError.Network

@Singleton
class SmartHomeTool @Inject constructor() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getEntities(baseUrl: String, token: String): Result<List<HomeEntity>, AppError> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/api/states")
                .addHeader("Authorization", "Bearer $token")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.Error(error = SmartHomeError, message = "HTTP ${response.code}")
                }
                val body = response.body?.string()
                    ?: return@withContext Result.Error(error = SmartHomeError, message = "Empty response")
                val entities = json.parseToJsonElement(body).jsonArray.map { element ->
                    val obj = element.jsonObject
                    HomeEntity(
                        entityId = obj["entity_id"]?.jsonPrimitive?.content ?: "",
                        state = obj["state"]?.jsonPrimitive?.content ?: "",
                        friendlyName = obj["attributes"]?.jsonObject?.get("friendly_name")?.jsonPrimitive?.content ?: "",
                    )
                }
                Result.Success(entities)
            }
        } catch (e: Exception) {
            Result.Error(error = SmartHomeError, message = "Failed to get entities: ${e.message}", throwable = e)
        }
    }

    suspend fun callService(
        baseUrl: String,
        token: String,
        domain: String,
        service: String,
        entityId: String,
    ): Result<String, AppError> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JsonObject(buildMap {
                put("entity_id", JsonPrimitive(entityId))
            }).toString().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("$baseUrl/api/services/$domain/$service")
                .addHeader("Authorization", "Bearer $token")
                .post(requestBody)
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.Error(error = SmartHomeError, message = "HTTP ${response.code}")
                }
                Result.Success("Service called: $domain.$service on $entityId")
            }
        } catch (e: Exception) {
            Result.Error(error = SmartHomeError, message = "Failed to call service: ${e.message}", throwable = e)
        }
    }
}

data class HomeEntity(
    val entityId: String,
    val state: String,
    val friendlyName: String,
)

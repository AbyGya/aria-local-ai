package app.knotwork.android.bridge.localai

import app.knotwork.android.domain.models.AppError
import app.knotwork.android.domain.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
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

private object LocalAIBridgeError : AppError.Network

@Singleton
class LocalAIBridge @Inject constructor() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    private fun chatPayload(
        model: String,
        prompt: String,
        systemPrompt: String?,
        temperature: Float,
        maxTokens: Int,
        stream: Boolean,
    ): String {
        val messages = buildList {
            if (systemPrompt != null) {
                add(JsonObject(mapOf("role" to JsonPrimitive("system"), "content" to JsonPrimitive(systemPrompt))))
            }
            add(JsonObject(mapOf("role" to JsonPrimitive("user"), "content" to JsonPrimitive(prompt))))
        }
        return json.encodeToString(
            JsonObject.serializer(),
            JsonObject(
                mapOf(
                    "model" to JsonPrimitive(model),
                    "messages" to JsonArray(messages),
                    "temperature" to JsonPrimitive(temperature),
                    "max_tokens" to JsonPrimitive(maxTokens),
                    "stream" to JsonPrimitive(stream),
                )
            )
        )
    }

    suspend fun isAvailable(baseUrl: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/v1/models")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun listModels(baseUrl: String): Result<List<String>, AppError> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/v1/models")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.Error(error = LocalAIBridgeError, message = "HTTP ${response.code}")
                }
                val body = response.body?.string()
                    ?: return@withContext Result.Error(error = LocalAIBridgeError, message = "Empty response")
                val data = json.parseToJsonElement(body).jsonObject["data"]?.jsonArray
                    ?: return@withContext Result.Success(emptyList())
                val models = data.mapNotNull { element ->
                    element.jsonObject["id"]?.jsonPrimitive?.content
                }
                Result.Success(models)
            }
        } catch (e: Exception) {
            Result.Error(error = LocalAIBridgeError, message = "Failed to list models: ${e.message}", throwable = e)
        }
    }

    suspend fun generate(
        baseUrl: String,
        model: String,
        prompt: String,
        systemPrompt: String? = null,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048,
    ): Result<String, AppError> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/v1/chat/completions")
                .post(chatPayload(model, prompt, systemPrompt, temperature, maxTokens, stream = false).toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.Error(
                        error = LocalAIBridgeError,
                        message = "HTTP ${response.code}: ${response.body?.string()}",
                    )
                }
                val body = response.body?.string()
                    ?: return@withContext Result.Error(error = LocalAIBridgeError, message = "Empty response")
                val message = json.parseToJsonElement(body)
                    .jsonObject["choices"]?.jsonArray
                    ?.firstOrNull()?.jsonObject
                    ?.get("message")?.jsonObject
                val content = message?.get("content")?.jsonPrimitive?.content
                if (content != null) {
                    Result.Success(content)
                } else {
                    Result.Error(error = LocalAIBridgeError, message = "No content in response")
                }
            }
        } catch (e: Exception) {
            Result.Error(error = LocalAIBridgeError, message = "Failed to generate: ${e.message}", throwable = e)
        }
    }
}

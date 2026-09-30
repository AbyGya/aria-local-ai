package app.knotwork.android.bridge.localai

import app.knotwork.android.domain.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAIBridge @Inject constructor() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

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

    suspend fun listModels(baseUrl: String): Result<List<String>, String> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/v1/models")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.Error("HTTP ${response.code}")
                }
                val body = response.body?.string() ?: return@withContext Result.Error("Empty response")
                val json = Json.parseToJsonElement(body).jsonObject
                val data = json["data"]?.jsonArray ?: return@withContext Result.Success(emptyList())
                val models = data.mapNotNull { element ->
                    element.jsonObject["id"]?.jsonPrimitive?.content
                }
                Result.Success(models)
            }
        } catch (e: Exception) {
            Result.Error("Failed to list models: ${e.message}")
        }
    }

    suspend fun generate(
        baseUrl: String,
        model: String,
        prompt: String,
        systemPrompt: String? = null,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048,
    ): Result<String, String> = withContext(Dispatchers.IO) {
        try {
            val messages = buildList {
                if (systemPrompt != null) {
                    add(mapOf("role" to "system", "content" to systemPrompt))
                }
                add(mapOf("role" to "user", "content" to prompt))
            }

            val requestBody = buildMap<String, Any>(
                "model" to model,
                "messages" to messages,
                "temperature" to temperature,
                "max_tokens" to maxTokens,
                "stream" to false,
            )

            val request = Request.Builder()
                .url("$baseUrl/v1/chat/completions")
                .post(Json.encodeToString(JsonObject.serializer(), JsonObject(buildMap {
                    put("model", kotlinx.serialization.json.JsonPrimitive(model))
                    put("messages", kotlinx.serialization.json.JsonArray(messages.map { msg ->
                        JsonObject(buildMap {
                            put("role", kotlinx.serialization.json.JsonPrimitive(msg["role"] as String))
                            put("content", kotlinx.serialization.json.JsonPrimitive(msg["content"] as String))
                        })
                    }))
                    put("temperature", kotlinx.serialization.json.JsonPrimitive(temperature))
                    put("max_tokens", kotlinx.serialization.json.JsonPrimitive(maxTokens))
                    put("stream", kotlinx.serialization.json.JsonPrimitive(false))
                })).toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.Error("HTTP ${response.code}: ${response.body?.string()}")
                }
                val body = response.body?.string() ?: return@withContext Result.Error("Empty response")
                val json = Json.parseToJsonElement(body).jsonObject
                val choices = json["choices"]?.jsonArray
                val message = choices?.firstOrNull()?.jsonObject?.get("message")?.jsonObject
                val content = message?.get("content")?.jsonPrimitive?.content
                if (content != null) {
                    Result.Success(content)
                } else {
                    Result.Error("No content in response")
                }
            }
        } catch (e: Exception) {
            Result.Error("Failed to generate: ${e.message}")
        }
    }

    suspend fun generateStream(
        baseUrl: String,
        model: String,
        prompt: String,
        systemPrompt: String? = null,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048,
    ): Result<okhttp3.Response, String> = withContext(Dispatchers.IO) {
        try {
            val messages = buildList {
                if (systemPrompt != null) {
                    add(mapOf("role" to "system", "content" to systemPrompt))
                }
                add(mapOf("role" to "user", "content" to prompt))
            }

            val requestBody = buildMap<String, Any>(
                "model" to model,
                "messages" to messages,
                "temperature" to temperature,
                "max_tokens" to maxTokens,
                "stream" to true,
            )

            val request = Request.Builder()
                .url("$baseUrl/v1/chat/completions")
                .post(Json.encodeToString(JsonObject.serializer(), JsonObject(buildMap {
                    put("model", kotlinx.serialization.json.JsonPrimitive(model))
                    put("messages", kotlinx.serialization.json.JsonArray(messages.map { msg ->
                        JsonObject(buildMap {
                            put("role", kotlinx.serialization.json.JsonPrimitive(msg["role"] as String))
                            put("content", kotlinx.serialization.json.JsonPrimitive(msg["content"] as String))
                        })
                    }))
                    put("temperature", kotlinx.serialization.json.JsonPrimitive(temperature))
                    put("max_tokens", kotlinx.serialization.json.JsonPrimitive(maxTokens))
                    put("stream", kotlinx.serialization.json.JsonPrimitive(true))
                })).toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.Error("HTTP ${response.code}")
            }
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error("Failed to connect: ${e.message}")
        }
    }
}

package app.knotwork.android.data.engine

import android.content.ComponentCallbacks2
import android.content.Context
import app.knotwork.android.di.ApplicationScope
import app.knotwork.android.di.IoDispatcher
import app.knotwork.android.domain.engine.LlmInferenceEngine
import app.knotwork.android.domain.models.AppError
import app.knotwork.android.domain.models.LocalBackend
import app.knotwork.android.domain.models.Result
import app.knotwork.android.domain.repositories.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LlamaCppEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    @ApplicationScope private val appScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : LlmInferenceEngine, ComponentCallbacks2 {

    private var nativeHandle: Long = 0L
    private var _currentModelPath: String? = null
    private var _isVisionEnabled: Boolean = false
    private var _isAudioEnabled: Boolean = false

    @Volatile
    private var _activeBackend: LocalBackend? = null

    @Volatile
    private var activeGenerationJob: Job? = null

    @Volatile
    private var loadGeneration: Long = 0L

    private val generationMutex = Mutex()

    override val isInitialized: Boolean get() = nativeHandle != 0L

    override val currentModelPath: String? get() = _currentModelPath

    override val isVisionEnabled: Boolean get() = _isVisionEnabled

    override val isAudioEnabled: Boolean get() = _isAudioEnabled

    override val activeBackend: LocalBackend? get() = _activeBackend

    init {
        context.registerComponentCallbacks(this)
        System.loadLibrary("llama_jni")
    }

    private object LlmSystemError : AppError.System

    external fun nativeInit(modelPath: String, nThreads: Int, nCtx: Int, useGpu: Boolean): Long
    external fun nativeGenerate(handle: Long, prompt: String, temperature: Float, topK: Int, topP: Float, maxTokens: Int): String
    external fun nativeClose(handle: Long)
    external fun nativeTokenize(handle: Long, text: String): IntArray
    external fun nativeDetokenize(handle: Long, tokens: IntArray): String

    override suspend fun initialize(
        modelPath: String,
        enableVision: Boolean,
        enableAudio: Boolean,
    ): Result<Unit, AppError> = withContext(ioDispatcher) {
        try {
            generationMutex.withLock {
                initializeInternal(modelPath, enableVision, enableAudio).also { outcome ->
                    if (outcome is Result.Success) {
                        loadGeneration += 1
                    }
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Timber.e(e, "Failed to initialize LlamaCppEngine")
            _currentModelPath = null
            _isVisionEnabled = false
            _isAudioEnabled = false
            Result.Error(
                error = LlmSystemError,
                message = e.localizedMessage ?: "Unknown initialization error",
                throwable = e,
            )
        }
    }

    private suspend fun initializeInternal(
        modelPath: String,
        enableVision: Boolean,
        enableAudio: Boolean,
    ): Result<Unit, AppError> {
        val file = File(modelPath)
        if (!file.exists()) {
            val errorMsg = "Model file does not exist at path: $modelPath"
            Timber.e("Model file does not exist at path: %s", modelPath)
            _currentModelPath = null
            _isVisionEnabled = false
            _isAudioEnabled = false
            return Result.Error(
                error = LlmSystemError,
                message = errorMsg,
            )
        }

        if (nativeHandle != 0L && _currentModelPath == modelPath && _isVisionEnabled == enableVision && _isAudioEnabled == enableAudio) {
            return Result.Success(Unit)
        }

        unloadInternal()

        val configuredKey = settingsRepository.localModelBackend.first()
        val configured = LocalBackend.fromKey(configuredKey) ?: LocalBackend.CPU

        val previousAttempt = settingsRepository.lastInitBackendAttempt.first()
        val crashedLastTime = configured != LocalBackend.CPU && previousAttempt == configured.key
        val resolved = if (crashedLastTime) {
            val streak = settingsRepository.localBackendFailureStreak.first() + 1
            settingsRepository.setLocalBackendFailureStreak(streak)
            if (streak >= BACKEND_FAILURE_STREAK_LIMIT) {
                Timber.w(
                    "Backend '%s' failed to initialise %d starts in a row — switching to CPU for good.",
                    configured.key,
                    streak,
                )
                settingsRepository.setLocalModelBackend(LocalBackend.CPU.key)
                settingsRepository.setLocalBackendFailureStreak(0)
            }
            settingsRepository.setLastInitBackendAttempt(null)
            LocalBackend.CPU
        } else {
            configured
        }

        if (resolved != LocalBackend.CPU) {
            settingsRepository.setLastInitBackendAttempt(resolved.key)
        } else {
            settingsRepository.setLastInitBackendAttempt(null)
        }

        val useGpu = resolved == LocalBackend.GPU
        val nThreads = Runtime.getRuntime().availableProcessors()
        val maxTokens = settingsRepository.maxContextLength.first()

        nativeHandle = nativeInit(modelPath, nThreads, maxTokens, useGpu)
        if (nativeHandle == 0L) {
            throw RuntimeException("Failed to initialize llama.cpp native engine")
        }

        _currentModelPath = modelPath
        _isVisionEnabled = enableVision
        _isAudioEnabled = enableAudio
        settingsRepository.setLastInitBackendAttempt(null)
        settingsRepository.setLocalBackendFailureStreak(0)
        _activeBackend = resolved
        Timber.i(
            "LlamaCpp Engine successfully initialized with $modelPath (vision=$enableVision, audio=$enableAudio, backend=$resolved)",
        )

        return Result.Success(Unit)
    }

    override fun generateResponseStream(prompt: String, imagePath: String?, temperature: Float?): Flow<String> =
        flow {
            generationMutex.withLock {
                val handle = nativeHandle
                if (handle == 0L) {
                    Timber.e("Engine is not initialized")
                    throw IllegalStateException("LLM Engine not initialized")
                }

                val generationJob = currentCoroutineContext()[Job]
                activeGenerationJob = generationJob

                try {
                    val temp = temperature ?: settingsRepository.temperature.first()
                    val topK = settingsRepository.topK.first()
                    val topP = settingsRepository.topP.first()
                    val maxTokens = settingsRepository.maxContextLength.first()

                    val response = nativeGenerate(handle, prompt, temp, topK, topP, maxTokens)
                    emit(response)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Timber.e(e, "Error during generation")
                    throw e
                } finally {
                    if (activeGenerationJob === generationJob) {
                        activeGenerationJob = null
                    }
                }
            }
        }.flowOn(ioDispatcher)

    override fun transcribe(audioPath: String, prompt: String): Flow<String> {
        throw UnsupportedOperationException("Audio transcription not supported by llama.cpp engine")
    }

    override suspend fun unload() = generationMutex.withLock { unloadInternal() }

    private fun unloadInternal() {
        try {
            if (nativeHandle != 0L) {
                nativeClose(nativeHandle)
                Timber.i("LlamaCpp engine unloaded successfully")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error unloading LlamaCpp engine")
        } finally {
            nativeHandle = 0L
            _currentModelPath = null
            _isVisionEnabled = false
            _isAudioEnabled = false
            _activeBackend = null
        }
    }

    override fun close() {
        appScope.launch { unload() }
        context.unregisterComponentCallbacks(this)
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {}

    override fun onLowMemory() {
        Timber.w("onLowMemory called, unloading engine")
        cancelActiveGeneration()
        appScope.launch { unload() }
    }

    private fun cancelActiveGeneration() {
        activeGenerationJob?.cancel(CancellationException("Engine unload requested under memory pressure"))
    }

    override fun onTrimMemory(level: Int) {
        if (level < ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) return

        val underRealPressure = level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE
        if (!underRealPressure && activeGenerationJob != null) {
            Timber.i("onTrimMemory level %d ignored: a generation is in flight", level)
            return
        }

        Timber.w("onTrimMemory called with level %d, unloading engine", level)
        cancelActiveGeneration()
        val target = loadGeneration
        appScope.launch { unloadGeneration(target) }
    }

    private suspend fun unloadGeneration(target: Long) = generationMutex.withLock {
        if (loadGeneration != target) {
            Timber.i("Stale unload for engine generation %d skipped; %d is live", target, loadGeneration)
            return@withLock
        }
        unloadInternal()
    }

    private companion object {
        const val BACKEND_FAILURE_STREAK_LIMIT: Int = 2
    }
}

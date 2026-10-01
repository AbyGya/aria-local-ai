package app.knotwork.android.data.tools.local

import android.content.Context
import android.os.Environment
import app.knotwork.android.domain.models.AppError
import app.knotwork.android.domain.models.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private object FileToolError : AppError.System

@Singleton
class FileTool @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun readFile(path: String): Result<String, AppError> {
        return try {
            val file = File(path)
            if (!file.exists()) return Result.Error(error = FileToolError, message = "File not found: $path")
            if (!file.canRead()) return Result.Error(error = FileToolError, message = "Cannot read file: $path")
            Result.Success(file.readText())
        } catch (e: Exception) {
            Result.Error(error = FileToolError, message = "Failed to read file: ${e.message}", throwable = e)
        }
    }

    fun writeFile(path: String, content: String): Result<String, AppError> {
        return try {
            val file = File(path)
            file.parentFile?.mkdirs()
            file.writeText(content)
            Result.Success("File written: $path")
        } catch (e: Exception) {
            Result.Error(error = FileToolError, message = "Failed to write file: ${e.message}", throwable = e)
        }
    }

    fun listFiles(path: String): Result<List<FileInfo>, AppError> {
        return try {
            val dir = File(path)
            if (!dir.exists()) return Result.Error(error = FileToolError, message = "Directory not found: $path")
            if (!dir.isDirectory) return Result.Error(error = FileToolError, message = "Not a directory: $path")
            val files = dir.listFiles()?.map { f ->
                FileInfo(
                    name = f.name,
                    path = f.absolutePath,
                    isDirectory = f.isDirectory,
                    size = if (f.isFile) f.length() else 0L,
                    lastModified = f.lastModified(),
                )
            }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) ?: emptyList()
            Result.Success(files)
        } catch (e: Exception) {
            Result.Error(error = FileToolError, message = "Failed to list files: ${e.message}", throwable = e)
        }
    }

    fun deleteFile(path: String): Result<String, AppError> {
        return try {
            val file = File(path)
            if (!file.exists()) return Result.Error(error = FileToolError, message = "File not found: $path")
            if (file.delete()) {
                Result.Success("Deleted: $path")
            } else {
                Result.Error(error = FileToolError, message = "Failed to delete: $path")
            }
        } catch (e: Exception) {
            Result.Error(error = FileToolError, message = "Failed to delete file: ${e.message}", throwable = e)
        }
    }

    fun getStorageInfo(): Result<StorageInfo, AppError> {
        return try {
            val internal = Environment.getDataDirectory()
            @Suppress("DEPRECATION")
            val external = Environment.getExternalStorageDirectory()
            Result.Success(
                StorageInfo(
                    internalTotal = internal.totalSpace,
                    internalFree = internal.freeSpace,
                    externalTotal = external.totalSpace,
                    externalFree = external.freeSpace,
                )
            )
        } catch (e: Exception) {
            Result.Error(error = FileToolError, message = "Failed to get storage info: ${e.message}", throwable = e)
        }
    }
}

data class FileInfo(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long,
    val lastModified: Long,
)

data class StorageInfo(
    val internalTotal: Long,
    val internalFree: Long,
    val externalTotal: Long,
    val externalFree: Long,
)

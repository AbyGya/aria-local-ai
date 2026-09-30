package app.knotwork.android.data.tools.local

import android.content.Context
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileTool @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun readFile(path: String): Result<String, String> {
        return try {
            val file = File(path)
            if (!file.exists()) return Result.Error("File not found: $path")
            if (!file.canRead()) return Result.Error("Cannot read file: $path")
            Result.Success(file.readText())
        } catch (e: Exception) {
            Result.Error("Failed to read file: ${e.message}")
        }
    }

    fun writeFile(path: String, content: String): Result<String, String> {
        return try {
            val file = File(path)
            file.parentFile?.mkdirs()
            file.writeText(content)
            Result.Success("File written: $path")
        } catch (e: Exception) {
            Result.Error("Failed to write file: ${e.message}")
        }
    }

    fun listFiles(path: String): Result<List<FileInfo>, String> {
        return try {
            val dir = File(path)
            if (!dir.exists()) return Result.Error("Directory not found: $path")
            if (!dir.isDirectory) return Result.Error("Not a directory: $path")
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
            Result.Error("Failed to list files: ${e.message}")
        }
    }

    fun deleteFile(path: String): Result<String, String> {
        return try {
            val file = File(path)
            if (!file.exists()) return Result.Error("File not found: $path")
            if (file.delete()) {
                Result.Success("Deleted: $path")
            } else {
                Result.Error("Failed to delete: $path")
            }
        } catch (e: Exception) {
            Result.Error("Failed to delete file: ${e.message}")
        }
    }

    fun getStorageInfo(): Result<StorageInfo, String> {
        return try {
            val internal = Environment.getDataDirectory()
            val external = Environment.getExternalStorageDirectory()
            Result.Success(
                StorageInfo(
                    internalTotal = internal.totalSpace,
                    internalFree = internal.freeSpace,
                    externalTotal = if (external != null) external.totalSpace else 0L,
                    externalFree = if (external != null) external.freeSpace else 0L,
                )
            )
        } catch (e: Exception) {
            Result.Error("Failed to get storage info: ${e.message}")
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

package de.miraculixx.mlog.utils

import kotlinx.serialization.encodeToString
import java.io.File

inline fun <reified T> File.readJson(arrayRoot: Boolean = false): T {
    if (!exists()) {
        if (!parentFile.exists()) parentFile.mkdirs()
        writeText(json.encodeToString(if (arrayRoot) "[]" else "{}"))
    }
    return json.decodeFromString<T>(readText())
}

inline fun <reified T : Enum<T>> enumOf(type: String?): T? {
    if (type == null) return null
    return try {
        java.lang.Enum.valueOf(T::class.java, type)
    } catch (e: IllegalArgumentException) {
        null
    }
}

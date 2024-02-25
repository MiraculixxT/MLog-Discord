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
package de.miraculixx.mlog.sql

import de.miraculixx.mlog.utils.enumOf
import java.sql.ResultSet
import java.sql.SQLException

fun <T> ResultSet.map(consumer: (ResultSet) -> T): List<T>
    = buildList { while (next()) add(consumer.invoke(this@map)) }

fun <T> ResultSet.mapFirst(consumer: (ResultSet) -> T): T? {
    return if (next()) consumer.invoke(this)
    else null
}

fun ResultSet.getAllLongs(name: String) = buildList {
    while (next()) getLongSave(name)?.let { add(it) }
}

fun ResultSet.getAllStrings(name: String) = buildList {
    while (next()) getStringSave(name)?.let { add(it) }
}

fun ResultSet.getStringSave(name: String) = try {
    getString(name)
} catch (_: SQLException) {
    null
}

fun ResultSet.getIntSave(name: String) = try {
    getInt(name)
} catch (_: SQLException) {
    null
}

fun ResultSet.getLongSave(name: String) = try {
    getLong(name)
} catch (_: SQLException) {
    null
}

inline fun <reified T : Enum<T>> ResultSet.getEnumSave(name: String) = try {
    enumOf<T>(getStringSave(name))
} catch (_: SQLException) {
    null
}

fun ResultSet.getFirstString(name: String): String? {
    return if (next()) getStringSave(name)
    else null
}

fun ResultSet.getFirstInt(name: String): Int? {
    return if (next()) getIntSave(name)
    else null
}

fun ResultSet.getFirstLong(name: String): Long? {
    return if (next()) getLongSave(name)
    else null
}
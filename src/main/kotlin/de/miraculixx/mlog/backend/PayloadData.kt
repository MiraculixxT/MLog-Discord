package de.miraculixx.mlog.backend

import kotlinx.serialization.Serializable

@Serializable
data class LogPayloadData(
    val code: String,
    val timestamp: Long,
    val mWebVersion: String,
    val mod: LogPayloadModData,
    val server: LogPayloadServerData
)

@Serializable
data class LogPayloadModData(
    val id: String,
    val version: String
)

@Serializable
data class LogPayloadServerData(
    val version: String,
    val loader: String,
    val system: String
)
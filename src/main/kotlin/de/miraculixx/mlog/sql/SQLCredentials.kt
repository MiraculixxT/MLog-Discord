package de.miraculixx.mlog.sql

import kotlinx.serialization.Serializable

@Serializable
data class SQLCredentials(
    val url: String,
    val user: String,
    val password: String
)
package de.miraculixx.mlog.utils.data

import kotlinx.serialization.Serializable

@Serializable
data class BotConfig(
    val token: String = "<token>",
    val serverPort: Int = 8080,
)

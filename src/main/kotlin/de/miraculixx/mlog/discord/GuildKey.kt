package de.miraculixx.mlog.discord

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class GuildKey(
    val guildId: String,
    val modId: String
)

@Serializable
data class GuildConfiguration(
    val codeTimeout: Duration
)
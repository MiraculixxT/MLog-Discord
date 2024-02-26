package de.miraculixx.mlog.discord

import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.entities.Guild
import kotlin.time.Duration

@Serializable
data class GuildKey(
    val guildId: String,
    val modId: String
)

fun Guild.toKey(modId: String) = GuildKey(id, modId)

@Serializable
data class GuildConfiguration(
    val codeTimeout: Duration
)
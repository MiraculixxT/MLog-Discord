package de.miraculixx.mlog.discord

import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.entities.Guild
import kotlin.time.Duration

@Serializable
data class GuildKey(
    val guildId: Long,
    val modId: String
)

fun Guild.toKey(modId: String) = GuildKey(idLong, modId)

data class GuildMetrics(
    val maxProjects: Int,
    val maxTraffic: Long,
    val trafficIncome: Long,
    val trafficProcessed: Long
)

data class GuildProject(
    val modId: String,
    val type: GuildProjectType,
    val timeout: Duration
)

data class ProjectRequest(
    val code: String,
    val channel: Long,
    val timeout: Long
)

enum class GuildProjectType {
    CLIENT, SERVER, HYBRID;

    fun getCommand(modId: String, code: String) = when (this) {
        CLIENT -> "> Command: `/mlog $modId $code`\n"
        SERVER -> "> Command: `/mlog-server $modId $code`\n"
        HYBRID -> "> Client: `/mlog $modId $code`\n> Server: `/mlog-server $modId $code`\n"
    }

    companion object {
        fun fromID(id: Int) = when (id) {
            0 -> CLIENT
            1 -> SERVER
            else -> HYBRID
        }
    }
}
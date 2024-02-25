package de.miraculixx.mlog.discord

import de.miraculixx.mlog.MLogBot
import de.miraculixx.mlog.backend.LogPayloadData
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.utils.FileUpload

object GuildManager {
    private val codeChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val badContentRegex = Regex("[`@#]")

    private val guildConfigurations: MutableMap<GuildKey, GuildConfiguration> = mutableMapOf() // <key, config>
    private val guildRequests: MutableMap<GuildKey, MutableMap<String, MessageChannel>> = mutableMapOf() // <key, <code, channel>>

    fun requestNewCode(guildKey: GuildKey, channel: MessageChannel): String? {
        val config = guildConfigurations[guildKey] ?: return null
        val timeout = config.codeTimeout

        val code = (1..8).map { codeChars.random() }.joinToString("")
        val guildRequests = guildRequests.getOrPut(guildKey) { mutableMapOf() }
        if (guildRequests.containsKey(code)) {
            return requestNewCode(guildKey, channel)
        } else guildRequests[code] = channel

        CoroutineScope(Dispatchers.Default).launch {
            delay(timeout)
            guildRequests.remove(code)
        }
        return code
    }

    fun validateCode(guildKey: GuildKey, code: String): Boolean {
        val codes = guildRequests[guildKey] ?: return false
        return codes.contains(code)
    }

    fun handleRequest(guildKey: GuildKey, data: LogPayloadData, files: List<Pair<ByteArray, String>>): Boolean {
        val guild = MLogBot.JDA.getGuildById(guildKey.guildId)
        if (guild == null) { // Remove guild data if bot is not in it
            removeGuild(guildKey)
            return false
        }

        val channel = guildRequests[guildKey]?.remove(data.code) ?: return false
        channel.send(embeds = listOf(Embed {
            title = ":file_folder: || User Logs"
            field {
                val modData = data.mod
                name = "\uD83D\uDD27 Mod Info"
                value = " ID: `${modData.id.stripContent()}`\nVersion: `${modData.version.stripContent()}`"
            }
            field {
                val serverData = data.server
                val serverLoader = serverData.loader
                val serverEmote = when {
                    serverLoader.contains("paper", true) -> "<:paper:972959254740881418>"
                    serverLoader.contains("fabric", true) -> "<:fabric:1062282724410413136>"
                    serverLoader.contains("spigot", true) -> "<:spigot:972959254740881418>"
                    else -> "<:minecraft:973316627166806086>"
                }
                name = "$serverEmote Server Info"
                value = "Version: `${serverData.version.stripContent()}`\nLoader: `${serverData.loader.stripContent()}`\nSystem: `${serverData.system.stripContent()}`"
            }
        }), files = files.map { FileUpload.fromData(it.first, it.second) }).queue()
        return true
    }

    private fun removeGuild(guildKey: GuildKey) {
        guildConfigurations.remove(guildKey)
        guildRequests.remove(guildKey)
    }

    private fun String.stripContent(): String {
        return replace(badContentRegex, "").take(30)
    }
}
package de.miraculixx.mlog.discord

import de.miraculixx.mlog.MLogBot
import de.miraculixx.mlog.backend.LogPayloadData
import de.miraculixx.mlog.sql.*
import dev.minn.jda.ktx.interactions.components.button
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import net.dv8tion.jda.api.utils.FileUpload
import java.time.Instant

object RequestManager {
    private val codeChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val badContentRegex = Regex("[`@#]")
    private val emojiDelete = Emoji.fromUnicode("\uD83D\uDDD1\uFE0F")

//    private val guildConfigurations: MutableMap<String, GuildConfiguration> = mutableMapOf() // <guildID, config>
//    private val guildRequests: MutableMap<GuildKey, MutableMap<String, MessageChannel>> = mutableMapOf() // <key, <code, channel>>
    private val activeKeys = mutableSetOf<String>()

    suspend fun initialize() {
        activeKeys.addAll(SQL.call("SELECT Code FROM Requests").getAllStrings("Code"))
    }

    suspend fun requestNewCode(guildKey: GuildKey, channel: MessageChannel): Pair<String, GuildProject>? {
        val projectConfig = GuildData.getProjectConfiguration(guildKey) ?: return null
        var code: String
        do {
            code = (1..8).map { codeChars.random() }.joinToString("")
        } while (code in activeKeys)
        activeKeys.add(code)

        SQL.call("INSERT INTO Requests VALUES ((SELECT PKey FROM Projects WHERE GKey=? AND ModID=?), ?, ?, ?)") {
            setLong(1, guildKey.guildId)
            setString(2, guildKey.modId)
            setString(3, code)
            setLong(4, channel.idLong)
            setLong(5, projectConfig.timeout.inWholeSeconds + Instant.now().epochSecond)
        }

        CoroutineScope(Dispatchers.Default).launch {
            delay(projectConfig.timeout)
            removeRequest(code)
        }
        return code to projectConfig
    }

    suspend fun validateCode(guildKey: GuildKey, code: String): Boolean {
        if (code !in activeKeys) return false
        val key = SQL.call("SELECT GKey FROM Requests JOIN Projects ON Requests.PKey = Projects.PKey WHERE ModID=? AND Code=?") {
            setString(2, guildKey.modId)
            setString(3, code)
        }.getFirstString("GKey") ?: return false
        return SQL.call("SELECT ID FROM Guilds WHERE GKey=?") {
            setString(1, key)
        }.getFirstString("ID") != null
    }

    suspend fun handleRequest(guildKey: GuildKey, data: LogPayloadData, files: List<Pair<ByteArray, String>>): Boolean {
        val guild = MLogBot.JDA.getGuildById(guildKey.guildId)
        if (guild == null) { // Remove guild data if bot is not in it
            GuildData.deleteGuildData(guildKey.guildId)
            return false
        }

        val channelID = SQL.call("SELECT Channel FROM Requests WHERE Code=?") {
            setString(1, data.code)
        }.getFirstString("Channel") ?: return false
        removeRequest(data.code)

        val channel = guild.getTextChannelById(channelID) ?: return false

        channel.send(embeds = listOf(Embed {
            title = "<:mweb:1117472967350308907> || User Logs"
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
            color = 0xa0712a
        }), files = files.map { FileUpload.fromData(it.first, it.second) },
            components = listOf(ActionRow.of(button("MLOG:DELETE", "Delete Logs", emojiDelete, ButtonStyle.DANGER)))).queue()
        return true
    }

    suspend fun removeRequest(code: String) {
        SQL.call("DELETE FROM Requests WHERE Code=?") {
            setString(1, code)
        }
        activeKeys.remove(code)
    }

    suspend fun getAllRequests(guildKey: GuildKey): List<ProjectRequest> {
        return SQL.call("SELECT Code, Channel, Requests.Timeout FROM Requests JOIN Projects ON Requests.PKey = Projects.PKey WHERE GKey=(SELECT GKey FROM Guilds WHERE ID=?) AND ModID=?") {
            setLong(1, guildKey.guildId)
            setString(2, guildKey.modId)
        }.map {
            ProjectRequest(
                it.getStringSave("Code") ?: "Unknown",
                it.getLongSave("Channel") ?: -1,
                it.getLongSave("Timeout") ?: -1
            )
        }
    }

    private fun String.stripContent(): String {
        return replace(badContentRegex, "").take(30)
    }
}
package de.miraculixx.mlog.discord.events

import de.miraculixx.mlog.discord.GuildKey
import de.miraculixx.mlog.sql.SQL
import de.miraculixx.mlog.sql.getAllStrings
import de.miraculixx.mlog.sql.getFirstString
import dev.minn.jda.ktx.events.listener
import kotlinx.coroutines.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import kotlin.time.Duration.Companion.minutes

object TabComplete {
    private val projectCache = mutableMapOf<Long, Set<String>>()
    private val codeCache = mutableMapOf<GuildKey, Set<String>>()
    private val dummySet = setOf("<none>")

    fun startListen(jda: JDA) = jda.listener<CommandAutoCompleteInteractionEvent> {
        val guildID = it.guild?.idLong ?: return@listener
        val focused = it.focusedOption

        when (focused.name) {
            "project" -> {
                val projects = projectCache[guildID] ?: refreshProjectCache(guildID)
                it.replyChoiceStrings(projects).queue()
            }

            "code" -> {
                val modID = it.getOption("project")?.asString
                if (modID == null) {
                    it.replyChoiceStrings(dummySet).queue()
                    return@listener
                }

                val guildKey = GuildKey(guildID, modID)
                val codes = codeCache[guildKey] ?: refreshCodeCache(guildKey)
                it.replyChoiceStrings(codes).queue()
            }
        }
    }

    private suspend fun refreshProjectCache(guildID: Long): Set<String> {
        val new = SQL.call("SELECT ModID FROM Projects JOIN Guilds ON Guilds.GKey = Projects.GKey WHERE ID=?") {
            setLong(1, guildID)
        }.getAllStrings("ModID").toSet().ifEmpty { dummySet }
        projectCache[guildID] = new

        CoroutineScope(Dispatchers.Default).launch {
            delay(1.minutes)
            projectCache.remove(guildID)
        }

        return new
    }

    private suspend fun refreshCodeCache(guildKey: GuildKey): Set<String> {
        val key = SQL.call("SELECT PKey FROM Projects JOIN Guilds ON Guilds.GKey = Projects.GKey WHERE ID=? AND ModID=?") {
            setLong(1, guildKey.guildId)
            setString(2, guildKey.modId)
        }.getFirstString("PKey")

        val new = if (key == null) emptySet()
        else {
            SQL.call("SELECT Code FROM Requests WHERE PKey=?") {
                setString(1, key)
            }.getAllStrings("Code").toSet().ifEmpty { dummySet }
        }
        codeCache[guildKey] = new

        CoroutineScope(Dispatchers.Default).launch {
            delay(1.minutes)
            codeCache.remove(guildKey)
        }

        return new
    }
}
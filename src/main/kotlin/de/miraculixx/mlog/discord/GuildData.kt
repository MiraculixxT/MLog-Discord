package de.miraculixx.mlog.discord

import de.miraculixx.mlog.sql.*
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent
import java.sql.ResultSet
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

object GuildData {
    private val registeredGuilds: MutableSet<Long> = mutableSetOf()

    suspend fun initialize(jda: JDA) {
        registeredGuilds.clear()
        registeredGuilds.addAll(SQL.call("SELECT ID FROM Guilds").getAllLongs("ID"))

        jda.listener<GuildLeaveEvent> {
            val id = it.guild.idLong
            if (!registeredGuilds.contains(id)) return@listener // Guild was never registered
            deleteGuildData(id)
        }

        jda.listener<UnavailableGuildLeaveEvent> {
            val id = it.guildIdLong
            if (!registeredGuilds.contains(id)) return@listener
            deleteGuildData(id)
        }
    }

    suspend fun checkGuildPresence(guildID: Long, guildName: String): Boolean {
        // Check if the guild is already registered
        if (registeredGuilds.contains(guildID)) return true

        // Register the guild to be present
        val key = SQL.call("INSERT INTO Guilds (ID, Name) VALUES (?, ?) RETURNING GKey") {
            setLong(1, guildID)
            setString(2, guildName)
        }.getFirstString("GKey") ?: return false
        SQL.call("INSERT INTO GuildMetrics (GKey) VALUES (?)") { setString(1, key) }
        registeredGuilds.add(guildID)
        return true
    }

    suspend fun getGuildMetrics(guildID: Long): GuildMetrics? {
        return SQL.call("SELECT MaxProjects, MaxTraffic, IncomeTraffic, ProcessedTraffic FROM Guilds JOIN GuildMetrics ON Guilds.GKey = GuildMetrics.GKey WHERE ID=?") {
            setLong(1, guildID)
        }.mapFirst {
            GuildMetrics(
                it.getIntSave("MaxProjects") ?: -1,
                it.getLongSave("MaxTraffic") ?: -1,
                it.getLongSave("IncomeTraffic") ?: -1,
                it.getLongSave("ProcessedTraffic") ?: -1
            )
        }
    }

    suspend fun deleteGuildData(guildID: Long): Boolean {
        // Receive GKey for further processing
        val gKey = SQL.call("SELECT GKey FROM Guilds WHERE ID=?") { setLong(1, guildID) }.getFirstInt("GKey") ?: return false

        // Delete all open requests
        SQL.call("DELETE FROM Requests WHERE PKey IN (SELECT PKey FROM Projects WHERE GKey=?)") { setInt(1, gKey) }

        // Delete all configured projects
        SQL.call("DELETE FROM Projects WHERE GKey=?") { setInt(1, gKey) }

        // Keep guild metrics to avoid bypassing the bandwidth limit
        return true
    }

    suspend fun registerNewProject(guildKey: GuildKey, timeout: Duration, type: GuildProjectType): Boolean {
        // Receive GKey for further processing and presence check
        val gKey = SQL.call("SELECT GKey FROM Guilds WHERE ID=?") {
            setLong(1, guildKey.guildId)
        }.getFirstInt("GKey") ?: return false

        // Register the new project
        SQL.call("INSERT INTO Projects (GKey, ModID, Type, Timeout) VALUES (?, ?, ?, ?)") {
            setInt(1, gKey)
            setString(2, guildKey.modId)
            setString(3, type.name)
            setString(4, timeout.toIsoString())
        }
        return true
    }

    suspend fun removeProject(guildKey: GuildKey): Boolean {
        return SQL.update("DELETE FROM Projects WHERE GKey=(SELECT GKey FROM Guilds WHERE ID=?) AND ModID=?") {
            setLong(1, guildKey.guildId)
            setString(2, guildKey.modId)
        } > 0
    }

    suspend fun getGuildProjects(guildID: Long): List<GuildProject> {
        return SQL.call("SELECT ModID, Type, Timeout FROM Guilds JOIN Projects ON Guilds.GKey = Projects.GKey WHERE ID = ?") {
            setLong(1, guildID)
        }.map {
            it.getGuildProject()
        }
    }

    suspend fun getProjectConfiguration(key: GuildKey): GuildProject? {
        return SQL.call("SELECT ModID, Type, Timeout FROM Guilds JOIN Projects ON Guilds.GKey = Projects.GKey WHERE ID = ? AND ModID = ?") {
            setLong(1, key.guildId)
            setString(2, key.modId)
        }.mapFirst { it.getGuildProject() }
    }

    private fun ResultSet.getGuildProject(): GuildProject {
        return GuildProject(
            getStringSave("ModID") ?: "Unknown",
            getEnumSave<GuildProjectType>("Type") ?: GuildProjectType.HYBRID,
            Duration.parseIsoStringOrNull(getStringSave("Timeout") ?: "") ?: 10.hours
        )
    }
}
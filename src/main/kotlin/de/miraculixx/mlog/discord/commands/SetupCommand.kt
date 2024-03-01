package de.miraculixx.mlog.discord.commands

import de.miraculixx.mlog.discord.*
import de.miraculixx.mlog.sql.SQL
import de.miraculixx.mlog.utils.entities.SlashCommandEvent
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import kotlin.time.Duration

class SetupCommand : SlashCommandEvent {
    override suspend fun trigger(it: SlashCommandInteractionEvent) {
        val guild = it.guild ?: return
        if (!GuildData.checkGuildPresence(guild.idLong, guild.name)) {
            it.databaseError(100)
            return
        }

        when (it.subcommandName) {
            "register-mod" -> {
                val modID = it.getOption("project")?.asString ?: return
                val typeID = it.getOption("type")?.asInt ?: return
                val timeout = it.getOption("timeout")?.asString ?: return

                val rows = SQL.call("SELECT ModID FROM Projects JOIN Guilds ON Guilds.GKey = Projects.GKey WHERE ID=?") {
                    setLong(1, guild.idLong)
                }.fetchSize
                val guildMetrics = GuildData.getGuildMetrics(guild.idLong)
                if (guildMetrics == null) {
                    it.databaseError(101)
                    return
                }

                if (rows >= guildMetrics.maxProjects) {
                    it.reply_(embeds = listOf(Embed {
                        title = "<:no:1212465201509568543>  || Project Not Added"
                        description = "The project `mod` could not be added. Your guild already reached the maximum of **3** concurrent projects!\n" +
                                "\n" +
                                "We currently have limited resources, the limit might expand in the future for supporter. You can [contact us](https://dc.mutils.net) via ticket for manual expanding if required."
                        colorError()
                        mlogFooter()
                    })).queue()
                }

                val type = GuildProjectType.fromID(typeID)
                val timeoutDuration = Duration.parseOrNull(timeout)
                if (timeoutDuration == null) {
                    it.durationError()
                    return
                }

                if (!GuildData.registerNewProject(guild.toKey(modID), timeoutDuration, type)) {
                    it.databaseError(102)
                    return
                }

                it.reply_(embeds = listOf(Embed {
                    title = "<:yes:1212465222175031316>  || Project Added"
                    description = "The project `$modID` has been added to your guild!\n" +
                            "You can now generate a code for your users to request their logs.\n" +
                            colorSuccess()
                    mlogFooter()
                })).queue()
            }

            "unregister-mod" -> {
                val modID = it.getOption("project")?.asString ?: return
                if (!GuildData.removeProject(guild.toKey(modID))) {
                    it.noProjectError(modID)
                    return
                }

                it.reply_(embeds = listOf(Embed {
                    title = "<:yes:1212465222175031316>  || Project Removed"
                    description = "The project `$modID` has been removed from your guild!\n" +
                            "You can no longer generate a code for your users to request their logs and all current codes are removed."
                    colorSuccess()
                    mlogFooter()
                })).queue()
            }

            "change-timeout" -> {
                val modID = it.getOption("project")?.asString ?: return
                val timeout = it.getOption("timeout")?.asString ?: return

                val timeoutDuration = Duration.parseOrNull(timeout)
                if (timeoutDuration == null) {
                    it.durationError()
                    return
                }

                val rows = SQL.update("UPDATE Projects SET Timeout=? WHERE GKey=(SELECT GKey FROM Guilds WHERE ID=?) AND ModID=?") {
                    setString(1, timeoutDuration.toIsoString())
                    setLong(2, guild.idLong)
                    setString(3, modID)
                }
                when {
                    rows == 0 -> it.noProjectError(modID)
                    rows > 0 -> {
                        it.reply_(embeds = listOf(Embed {
                            title = "<:yes:1212465222175031316>  || Timeout Changed"
                            description = "The timeout for the project `$modID` has been changed to `$timeoutDuration`!"
                            colorSuccess()
                            mlogFooter()
                        })).queue()
                    }

                    else -> it.databaseError(104)
                }
            }
        }
    }

    private fun SlashCommandInteractionEvent.databaseError(code: Int) {
        reply_(embeds = listOf(Embed {
            title = "<:no:1212465201509568543>  || Something Went Wrong"
            description = "Oops, that shouldn't have happened.\nPlease try again later or [contact us](https://dc.mutils.net) if the issue persists.\n" +
                    "Error Position: `$code`"
            colorError()
            mlogFooter()
        })).queue()
    }

    private fun SlashCommandInteractionEvent.noProjectError(modID: String) {
        reply_(embeds = listOf(Embed {
            title = "<:no:1212465201509568543>  || Project Not Removed"
            description = "The project `$modID` could not be removed.\nIt is not configured for this guild!"
            colorError()
            mlogFooter()
        })).queue()
    }

    private fun SlashCommandInteractionEvent.durationError() {
        reply_(embeds = listOf(Embed {
            title = "<:no:1212465201509568543>  || Invalid Timeout Duration"
            description = "The timeout provided is invalid.\nPlease provide a valid duration in the format of `1h`, `30m`, `1h30m`, `1.5d` etc."
            colorError()
            mlogFooter()
        })).queue()
    }
}
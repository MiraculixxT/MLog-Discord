package de.miraculixx.mlog.discord.commands

import de.miraculixx.mlog.discord.*
import de.miraculixx.mlog.sql.SQL
import de.miraculixx.mlog.utils.entities.SlashCommandEvent
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.time.LocalDateTime
import java.time.ZoneOffset

class ManageCommand: SlashCommandEvent {
    private val currentTimeZone = ZoneOffset.of(ZoneOffset.systemDefault().id)

    override suspend fun trigger(it: SlashCommandInteractionEvent) {
        val guildID = it.guild?.idLong ?: return

        when (it.subcommandName) {
            "info" -> {
                val metrics = GuildData.getGuildMetrics(guildID)
                if (metrics == null) {
                    it.guildNotRegistered()
                    return
                }
                val projects = SQL.call("SELECT ModID FROM Projects JOIN Guilds ON Guilds.GKey = Projects.GKey WHERE ID=?") {
                    setLong(1, guildID)
                }.fetchSize
                val resetTime = LocalDateTime.now().plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
                    .toEpochSecond(currentTimeZone)

                it.reply_(embeds = listOf(Embed {
                    title = "<:mweb:1117472967350308907>  || Guild Information"
                    description = "All data we know and save about your guild"
                    field {
                        name = "<:minecraft:973316627166806086> Projects"
                        value = "> • Max: `${metrics.maxProjects}`\n" +
                                "> • Current: `$projects`"
                        inline = true
                    }
                    field {
                        name = "<:command_block:931889815392227328> Traffic"
                        value = "> • Max: `${metrics.maxTraffic}`\n" +
                                "> • Current: `${metrics.trafficProcessed}`\n" +
                                "> • Raw Received: `${metrics.trafficIncome}`\n" +
                                "> *Monthly reset <t:$resetTime:R> *"
                        inline = true
                    }
                    thumbnail = it.guild?.iconUrl
                    colorSuccess()
                    mlogFooter()
                })).queue()
            }
            "projects" -> {
                val projects = GuildData.getGuildProjects(guildID)
                it.reply_(embeds = listOf(Embed {
                    title = "<:mweb:1117472967350308907>  || Project Information"
                    description = "All currently registered projects for this guild"

                    if (projects.isNotEmpty()) {
                        projects.forEach { project ->
                            field {
                                name = "<:minecraft:973316627166806086> ${project.modId}"
                                value = "> • Type: `${project.type.name}`\n" +
                                        "> • Timeout: `${project.timeout}`"
                                inline = true
                            }
                        }
                    } else {
                        field {
                            name = "<:no:1212465201509568543> No Projects"
                            value = "```diff\n- This guild has no registered projects yet!```"
                        }
                    }
                    colorSuccess()
                    mlogFooter()
                })).queue()
            }

            // code group
            "list" -> {
                val project = it.getOption("project")?.asString ?: return
                val key = GuildKey(guildID, project)
                val openRequests = RequestManager.getAllRequests(key)
                it.reply_(embeds = listOf(Embed {
                    title = "<:mweb:1117472967350308907>  || Open Requests"
                    description = "All currently open requests for the project `$project`"
                    field {
                        name = "<:slash:983086645505065020> Open Requests"
                        value = buildString {
                            if (openRequests.isNotEmpty()) {
                                openRequests.forEach { request ->
                                    append("> • `$request` - Invalid <t:$request:R>\n")
                                }
                            } else {
                                field {
                                    name = "<:no:1212465201509568543> No Requests"
                                    value = "```diff\n- This project has no open requests yet!```"
                                }
                            }
                        }
                    }
                    colorSuccess()
                    mlogFooter()
                })).queue()
            }
            "remove" -> {

            }
        }
    }

    private fun SlashCommandInteractionEvent.guildNotRegistered() {
        reply_(embeds = listOf(Embed {
            title = "<:no:1212465201509568543>  || Guild Not Registered"
            description = "This guild (`${guild?.name}`) is not registered in our database!\n" +
                    "Please register any project with the `/setup` command before using the manage command."
            colorError()
            mlogFooter()
        })).queue()
    }
}
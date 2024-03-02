package de.miraculixx.mlog.utils.manager

import de.miraculixx.mlog.LOGGER
import de.miraculixx.mlog.MLogBot
import de.miraculixx.mlog.discord.commands.CodeCommand
import de.miraculixx.mlog.discord.commands.ManageCommand
import de.miraculixx.mlog.discord.commands.SetupCommand
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.interactions.commands.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions

object SlashCommandManager {
    private val commands = mapOf(
        "setup" to SetupCommand(),
        "code" to CodeCommand(),
        "manage" to ManageCommand()
    )

    fun startListen(jda: JDA) {
        jda.listener<SlashCommandInteractionEvent> {
            val commandClass = commands[it.name] ?: return@listener
            val subCommandGroup = it.subcommandGroup
            LOGGER.info("${it.user.name}: /${it.name}${if (subCommandGroup == null) "" else " $subCommandGroup"} ${it.subcommandName}")
            try {
                commandClass.trigger(it)
            } catch (e: Exception) {
                LOGGER.warn("Error while executing command: ${e.message}")
                it.reply("```diff\n- A critical error occurred while executing the command!\n" +
                        "- Please notify us to get this fixed quickly.\n" +
                        "- https://dc.mutils.net```").setEphemeral(true).queue()
            }
        }
    }

    init {
        MLogBot.JDA.updateCommands {
            slash("setup", "Setup MLog for your Server") {
                defaultPermissions = DefaultMemberPermissions.DISABLED
                subcommand("register-mod", "Register a new mod for your server") {
                    option<String>("project", "The ID of your mod/plugin", true)
                    option<Long>("type", "Client side, server side or both?", true) {
                        choice("client", 0)
                        choice("server", 0)
                        choice("both", 0)
                    }
                    option<String>("timeout", "The time after each code becomes unusable (e.g. 1d 6h)", true)
                }
                subcommand("unregister-mod", "Unregister a mod from your server") {
                    option<String>("project", "The ID of your mod/plugin", true, true)
                }
                subcommand("change-timeout", "Set the timeout for new generated codes") {
                    option<String>("project", "The ID of your mod/plugin", true, true)
                    option<String>("timeout", "The time after each code becomes unusable (e.g. 1d 6h)", true)
                }
            }

            slash("manage", "Manage your MLog settings") {
                defaultPermissions = DefaultMemberPermissions.DISABLED
                subcommand("info", "Get information about your server")
                subcommand("projects", "List all registered projects for your server")
                addSubcommandGroups(SubcommandGroup("codes", "Manage your codes for your server") {
                    subcommand("list", "List all active codes for a project") {
                        option<String>("project", "The ID of your mod/plugin", true, true)
                    }
                    subcommand("remove", "Remove a code from a project") {
                        option<String>("project", "The ID of your mod/plugin", true, true)
                        option<String>("code", "The code to remove", true, true)
                    }
                })
            }

            slash("code", "Get a new code to register a new mod") {
                defaultPermissions = DefaultMemberPermissions.DISABLED
                option<String>("project", "The target project for receiving logs", true, true)
                option<MessageChannel>("channel", "The channel to send the logs to", false)
                option<Boolean>("announce", "Announce generated code public in current channel?", false)
            }
        }.queue()
    }
}
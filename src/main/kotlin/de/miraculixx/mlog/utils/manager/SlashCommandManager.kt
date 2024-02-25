package de.miraculixx.mlog.utils.manager

import de.miraculixx.mlog.MLogBot
import de.miraculixx.mlog.discord.commands.CodeCommand
import de.miraculixx.mlog.discord.commands.SetupCommand
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.interactions.commands.option
import dev.minn.jda.ktx.interactions.commands.slash
import dev.minn.jda.ktx.interactions.commands.subcommand
import dev.minn.jda.ktx.interactions.commands.updateCommands
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType

object SlashCommandManager {
    private val commands = mapOf(
        "setup" to SetupCommand(),
        "code" to CodeCommand()
        )

    fun startListen(jda: JDA) {
        jda.listener<SlashCommandInteractionEvent> {
            val commandClass = commands[it.name] ?: return@listener
            commandClass.trigger(it)
        }
        jda.listener<CommandAutoCompleteInteractionEvent> {
            val commandClass = commands[it.name] ?: return@listener
            commandClass.tabComplete(it)
        }
    }

    init {
        MLogBot.JDA.updateCommands {
            slash("setup", "Setup MLog for your Server") {
                defaultPermissions = DefaultMemberPermissions.DISABLED
                subcommand("register-mod", "Register a new mod for your server") {
                    option<String>("mod-id", "The ID of your mod/plugin", true)
                }
                subcommand("unregister-mod", "Unregister a mod from your server") {
                    addOption(OptionType.STRING, "mod-id", "The ID of your mod/plugin", true)
                    option<String>("mod-id", "The ID of your mod/plugin", true)
                }
                subcommand("code-timeout", "Set the timeout for new generated codes") {
                    option<String>("timeout", "1d | 6h 30m 10s | 1.5d | ...", true)
                }
            }

            slash("code", "Get a new code to register a new mod") {
                defaultPermissions = DefaultMemberPermissions.DISABLED
                option<String>("mod", "The target mod for receiving logs", true, true)
                option<MessageChannel>("channel", "The channel to send the logs to", false)
                option<Boolean>("announce", "Announce generated code public in current channel?", false)
            }
        }.queue()
    }
}
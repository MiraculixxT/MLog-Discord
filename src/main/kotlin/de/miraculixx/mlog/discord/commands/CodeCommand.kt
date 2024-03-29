package de.miraculixx.mlog.discord.commands

import de.miraculixx.mlog.discord.RequestManager
import de.miraculixx.mlog.discord.colorSuccess
import de.miraculixx.mlog.discord.mlogFooter
import de.miraculixx.mlog.discord.toKey
import de.miraculixx.mlog.utils.entities.SlashCommandEvent
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.time.Instant

class CodeCommand : SlashCommandEvent {
    override suspend fun trigger(it: SlashCommandInteractionEvent) {
        val guild = it.guild ?: return
        val mod = it.getOption("project")?.asString ?: return
        val channel = it.getOption("channel")?.asChannel ?: it.channel
        if (channel !is MessageChannel) {
            it.reply_("```diff\n- Please provide a valid message channel!```", ephemeral = true).queue()
            return
        }
        val announce = it.getOption("announce")?.asBoolean ?: true
        val newRequest = RequestManager.requestNewCode(guild.toKey(mod), channel)
        if (newRequest == null) {
            it.reply_("```diff\n- Failed to generate a new code!\n- The mod/plugin $mod is not configured for this server yet! Use /setup before```", ephemeral = true).queue()
            return
        }
        val config = newRequest.second
        val code = newRequest.first

        if (announce) {
            it.replyEmbeds(Embed {
                title = "<:mweb:1117472967350308907>  || New File Request"
                description = "${it.user.asMention} requested to share your logs into this channel.\n" +
                        "Enter following command in your Minecraft chat or the console:\n" +
                        config.type.getCommand(mod, code) +
                        "The command will expire <t:${Instant.now().epochSecond + config.timeout.inWholeSeconds}:R>\n" +
                        "\n" +
                        "\uD83D\uDD3B **Command not working?**\n" +
                        "Install [MLog](https://modrinth.com/project/mlog) on your server or as mod to enable easy support for all plugins/mods using MLog."
                colorSuccess()
                mlogFooter()
            }).queue()
        } else {
            it.replyEmbeds(Embed {
                title = "<:mweb:1117472967350308907>  || New File Request"
                description = "You generated a new code for $mod! \n" +
                        "Give your user the code or command to receive all logs and/or configurations.\n" +
                        "> Code: `$code`\n" +
                        config.type.getCommand(mod, code) +
                        "The code will expire <t:${Instant.now().epochSecond + config.timeout.inWholeSeconds}:R>\n" +
                        "\n" +
                        "\uD83D\uDD3B **Command not working?**\n" +
                        "MLog can be downloaded on Modrinth via following link:\n" +
                        "> https://modrinth.com/project/mlog"
                colorSuccess()
                mlogFooter()
            }).queue()
        }
    }
}
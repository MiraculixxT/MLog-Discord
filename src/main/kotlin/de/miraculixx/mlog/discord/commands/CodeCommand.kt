package de.miraculixx.mlog.discord.commands

import de.miraculixx.mlog.utils.entities.SlashCommandEvent
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CodeCommand: SlashCommandEvent {
    override suspend fun trigger(it: SlashCommandInteractionEvent) {
        val channel = it.getOption("channel")?.asChannel ?: it.channel
        if (channel !is MessageChannel) {
            it.reply("```diff\n- Please provide a valid message channel!```").queue()
            return
        }
        val announce = it.getOption("announce")?.asBoolean ?: true

    }
}
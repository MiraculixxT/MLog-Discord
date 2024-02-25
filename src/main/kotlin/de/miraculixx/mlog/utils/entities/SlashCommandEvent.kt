package de.miraculixx.mlog.utils.entities

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

interface SlashCommandEvent {
    suspend fun trigger(it: SlashCommandInteractionEvent)

    suspend fun tabComplete(it: CommandAutoCompleteInteractionEvent) {}
}
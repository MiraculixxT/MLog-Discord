package de.miraculixx.mlog.utils.entities

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

interface ModalEvent {
    suspend fun trigger(it: ModalInteractionEvent) {}
}
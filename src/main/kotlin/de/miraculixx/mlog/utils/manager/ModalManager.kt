package de.miraculixx.mlog.utils.manager

import de.miraculixx.mlog.utils.entities.ModalEvent
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

object ModalManager {
    private val modals = mapOf<String, ModalEvent>(
    )

    fun startListen(jda: JDA) = jda.listener<ModalInteractionEvent> {
        val id = it.modalId
        val commandClass = when {

            else -> modals[id]
        }
        commandClass?.trigger(it)
    }
}
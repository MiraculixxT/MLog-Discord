package de.miraculixx.mlog.utils.manager

import de.miraculixx.mlog.discord.events.DeleteButton
import de.miraculixx.mlog.utils.entities.ButtonEvent
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

object ButtonManager {
    private val buttons = mapOf<String, ButtonEvent>(
        "MLOG:DELETE" to DeleteButton(),
    )

    fun startListen(jda: JDA) = jda.listener<ButtonInteractionEvent> {
        val id = it.button.id ?: return@listener
        val commandClass = when {
            else -> buttons[id]
        }
        commandClass?.trigger(it)
    }
}
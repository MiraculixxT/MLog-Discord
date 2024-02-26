package de.miraculixx.mlog.utils.manager

import de.miraculixx.mlog.utils.entities.DropDownEvent
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent

object DropDownManager {
    private val dropdowns = mapOf<String, DropDownEvent>(
    )

    fun startListen(jda: JDA) = jda.listener<StringSelectInteractionEvent> {
        val id = it.selectMenu.id ?: return@listener
        val commandClass = when {

            else -> dropdowns[id]
        }
        commandClass?.trigger(it)
    }
}
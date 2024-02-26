package de.miraculixx.mlog.discord.events

import de.miraculixx.mlog.utils.entities.ButtonEvent
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.reply_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import kotlin.time.Duration.Companion.seconds

class DeleteButton : ButtonEvent {
    private val confirms = mutableMapOf<Long, Long>() // <userID, messageID>

    override suspend fun trigger(it: ButtonInteractionEvent) {
        it.guild ?: return
        val userID = it.user.idLong
        val messageID = it.messageIdLong

        val isConfirmed = confirms[userID] == messageID
        if (!isConfirmed) {
            confirms[userID] = messageID
            it.reply_("Are you sure you want to delete all logs from Discord?\nClick the button again to confirm.", ephemeral = true).queue()
            delayRemove(userID)
        } else {
            it.channel.sendMessageEmbeds(Embed {
                title = "\uD83D\uDDD1\uFE0F  || Files Removed"
                description = "All shared files and configurations were deleted by ${it.user.asMention}!"
                color = 0xa02a2a
                footer("MLog - Provide fast & easy support", "https://cdn.discordapp.com/emojis/975780449903341579.webp?quality=lossless")
            }).queue()
            it.reply_("All logs have been permanently deleted from Discord!\nLogs can be requested and send again at any time.").queue()
            confirms.remove(userID)
            it.message.delete().queue()
        }
    }

    private fun delayRemove(userID: Long) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(30.seconds)
            confirms.remove(userID)
        }
    }
}
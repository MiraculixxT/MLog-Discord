package de.miraculixx.mlog

import de.miraculixx.mlog.backend.WebServer
import de.miraculixx.mlog.discord.GuildData
import de.miraculixx.mlog.discord.RequestManager
import de.miraculixx.mlog.discord.events.TabComplete
import de.miraculixx.mlog.utils.data.BotConfig
import de.miraculixx.mlog.utils.manager.ButtonManager
import de.miraculixx.mlog.utils.manager.SlashCommandManager
import de.miraculixx.mlog.utils.readJson
import dev.minn.jda.ktx.jdabuilder.default
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

fun main() {
    MLogBot
}

val LOGGER: Logger = LoggerFactory.getLogger(MLogBot::class.java)

object MLogBot {
    var JDA: JDA
    val botConfig = File("config/bot.json").readJson<BotConfig>()

    init {
        runBlocking {
            JDA = default(botConfig.token) {
                setActivity(Activity.watching("for logs"))
                setStatus(OnlineStatus.DO_NOT_DISTURB)
                setMemberCachePolicy(MemberCachePolicy.NONE)
            }
            JDA.awaitReady()

            ButtonManager.startListen(JDA)
//        DropDownManager.startListen(JDA)
//        ModalManager.startListen(JDA)
            SlashCommandManager.startListen(JDA)

            WebServer
            RequestManager.initialize()
            GuildData.initialize(JDA)
            TabComplete.startListen(JDA)

            LOGGER.info("MLog is now online!")

            keepAlive()
        }
    }

    private fun keepAlive() {
        runBlocking {
            var online = true
            while (online) {
                val scanner = Scanner(System.`in`)
                when (val out = scanner.nextLine()) {
                    "exit" -> {
                        JDA.shardManager?.setStatus(OnlineStatus.OFFLINE)
                        JDA.shutdown()
                        LOGGER.info("MLog is now offline!")
                        online = false
                    }

                    else -> {
                        LOGGER.warn("Command $out not found!")
                        LOGGER.warn("Current Commands -> 'exit'")
                    }
                }
            }
        }
    }
}
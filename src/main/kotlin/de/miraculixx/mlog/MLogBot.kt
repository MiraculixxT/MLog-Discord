package de.miraculixx.mlog

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
import java.io.File
import java.util.*

fun main() {
    MLogBot
}

lateinit var INSTANCE: MLogBot

object MLogBot {
    var JDA: JDA
    val botConfig = File("config/bot.json").readJson<BotConfig>()

    init {
        INSTANCE = this

        JDA = default(botConfig.token) {
            setActivity(Activity.watching("MLog sharing"))
            setStatus(OnlineStatus.DO_NOT_DISTURB)
            setMemberCachePolicy(MemberCachePolicy.NONE)
        }
        JDA.awaitReady()

        ButtonManager.startListen(JDA)
//        DropDownManager.startListen(JDA)
//        ModalManager.startListen(JDA)
        SlashCommandManager.startListen(JDA)

        println("MLog is now online!")

        keepAlive()
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
                        println("MLog is now offline!")
                        online = false
                    }

                    else -> {
                        println("Command $out not found!\nCurrent Commands -> 'exit'")
                    }
                }
            }
        }
    }
}
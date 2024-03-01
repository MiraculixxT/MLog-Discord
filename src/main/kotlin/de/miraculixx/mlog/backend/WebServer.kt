package de.miraculixx.mlog.backend

import de.miraculixx.mlog.MLogBot
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.ratelimit.*
import kotlin.time.Duration.Companion.seconds

object WebServer {
    init {
        embeddedServer(Netty, port = MLogBot.botConfig.serverPort, host = "0.0.0.0", module = Application::module).start(wait = false)
    }
}

private fun Application.module() {
    install(RateLimit) {
        global {
            rateLimiter(3, 60.seconds)
        }
    }
    configureWebhooks()
}
package de.miraculixx.mlog.backend

import de.miraculixx.mlog.discord.GuildKey
import de.miraculixx.mlog.discord.RequestManager
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureWebhooks() {
    routing {
        // Using global rate limit for all requests
        rateLimit {
            /**
             * Example Ktor Request:
             * post("https://mlog.mutils.net/webhook/mweb/908621996009619477") {
             *   headers {
             *      append("ModID", "mweb")
             *      append("GuildID", "908621996009619477")
             *      append("Code", "123abcABC")
             *   }
             *   body = MultiPartFormDataContent(formData {
             *      append("data", json.encodeToString(LogPayloadData(...))),
             *      append("file[0]", file)
             *      append("file[1]", file)
             *      ...
             *      }
             * }
             */
            post("webhook/{ModID}/{GuildID}") {
                val modID = call.request.header("ModID")
                val guildID = call.request.header("GuildID")
                val code = call.request.header("Code")
                val contentLength = call.request.header(HttpHeaders.ContentLength)

                if ((contentLength?.toLongOrNull() ?: 0) > 3_000_000) {
                    call.respondText("File size exceeds limit!", status = HttpStatusCode.BadRequest)
                    return@post
                }

                if (modID == null || guildID == null || code == null) {
                    call.respondText("Missing request information!", status = HttpStatusCode.BadRequest)
                    return@post
                }

                val guildKey = GuildKey(guildID.toLongOrNull() ?: -1, modID)
                if (guildKey.guildId == -1L || !RequestManager.validateCode(guildKey, code)) {
                    call.respondText("Invalid code or credentials!", status = HttpStatusCode.Forbidden)
                }

                var data: LogPayloadData? = null
                val files = mutableListOf<Pair<ByteArray, String>>()
                call.receiveMultipart().forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "data") {
                                data = call.receive<LogPayloadData>()
                            }
                        }

                        is PartData.FileItem -> {
                            if (files.size > 6) return@forEachPart // Stop parsing after illegal size
                            val fileBites = part.streamProvider().readAllBytes()
                            val fileName = part.originalFileName ?: "${UUID.randomUUID()}.txt" // Let discord decide to display the file as text or not
                            files.add(fileBites to fileName)
                        }

                        else -> Unit
                    }
                    part.dispose()
                }

                if (data == null || files.isEmpty()) {
                    call.respondText("Missing multipart data!", status = HttpStatusCode.BadRequest)
                    return@post
                }
                if (files.size > 5) {
                    call.respondText("Too many files! Please zip before sending", status = HttpStatusCode.BadRequest)
                    return@post
                }

                RequestManager.handleRequest(guildKey, data!!, files)
            }
        }
    }
}

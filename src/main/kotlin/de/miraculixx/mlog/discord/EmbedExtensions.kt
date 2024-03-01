package de.miraculixx.mlog.discord

import dev.minn.jda.ktx.messages.InlineEmbed

fun InlineEmbed.mlogFooter() {
    footer("MLog - Provide fast & easy support", "https://cdn.discordapp.com/emojis/975780449903341579.webp?quality=lossless")
}

fun InlineEmbed.colorError() {
    color = 0xa02a2a
}

fun InlineEmbed.colorSuccess() {
    color = 0x2aa046
}
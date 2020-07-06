package com.iurysza.twittertelegrambot

import com.iurysza.twittertelegrambot.streaming.FilterParams
import com.iurysza.twittertelegrambot.streaming.FilteredStatusStream
import com.iurysza.twittertelegrambot.utils.AuthLoader

fun main() {
    val filterParams = FilterParams(listOf(1278835179910504448L), listOf("@MandaProZap"))
    val auth = AuthLoader.createAuthFrom("twitter_auth.json")

    FilteredStatusStream(auth, filterParams) { newStatus ->
        println(newStatus)
    }.start()
}

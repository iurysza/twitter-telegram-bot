package com.iurysza.twittertelegrambot

import com.iurysza.twittertelegrambot.streaming.FilterParams
import com.iurysza.twittertelegrambot.streaming.FilteredStatusStream
import com.iurysza.twittertelegrambot.utils.AuthLoader

fun main() {
    val filterParams = FilterParams(listOf(1278835179910504448L), listOf("@MandaProZap"))
    val authData = AuthLoader.getAuthDataFrom("twitter_auth.json")

    val twitterClient = TwitterClient(authData)

    FilteredStatusStream(authData, filterParams) { newStatus ->
        twitterClient
            .getStatus(newStatus!!.inReplyToStatusId)
            ?.mediaEntities
            ?.forEach { println(it) }
    }.start()
}

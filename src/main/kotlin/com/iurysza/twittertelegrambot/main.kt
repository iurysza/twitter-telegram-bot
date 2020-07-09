package com.iurysza.twittertelegrambot

import com.github.kotlintelegrambot.bot
import com.iurysza.twittertelegrambot.twitter.TwitterClient
import com.iurysza.twittertelegrambot.twitter.streaming.FilterParams
import com.iurysza.twittertelegrambot.twitter.streaming.FilteredStatusStream
import com.iurysza.twittertelegrambot.utils.AuthLoader
import com.iurysza.twittertelegrambot.utils.FileDownloader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


fun main() {
    val authData = AuthLoader.getAuthDataFrom("auth_data.json")
    val filterParams = FilterParams(listOf(authData.twitterUserId), listOf("@MandaProZap"))

    val twitterClient = TwitterClient(authData)
    val telegramBot = bot { token = authData.telegramToken }

    FilteredStatusStream(authData, filterParams) { newStatus ->
        twitterClient.getStatusById(newStatus.inReplyToStatusId)
            ?.mediaEntities
            ?.mapNotNull { it.videoVariants.firstOrNull()?.url }
            ?.forEach { url ->
                GlobalScope.launch {
                    FileDownloader.downloadAndWriteToFile(url)?.let { videoFile ->
                        telegramBot.sendVideo(authData.telegramUserId, videoFile)
                    }
                }
            }
    }.start()
}

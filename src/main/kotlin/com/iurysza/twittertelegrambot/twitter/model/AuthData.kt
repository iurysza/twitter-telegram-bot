package com.iurysza.twittertelegrambot.twitter.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthData(
    val consumerKey: String,
    val consumerSecret: String,
    val token: String,
    val tokenSecret: String,
    val telegramToken: String,
    val twitterUserId: Long,
    val telegramUserId: Long
)

package com.iurysza.twittertelegrambot.utils

import com.iurysza.twittertelegrambot.model.AuthData
import com.twitter.hbc.httpclient.auth.OAuth1
import java.io.File

object AuthLoader {
    fun createAuthFrom(fileName: String): OAuth1 {
        val fileContent = getFileFromResources("/$fileName")
        val (consumerKey, consumerSecret, token, tokenSecret) = MoshiWrapper.fromJson(
            AuthData::class,
            fileContent
        )!!
        return OAuth1(consumerKey, consumerSecret, token, tokenSecret)
    }

    private fun getFileFromResources(fileName: String): String {
        val resource = this::class.java.getResource(fileName)
        return if (resource == null) {
            throw IllegalArgumentException("file not found!")
        } else {
            File(resource.file).readText()
        }
    }
}
package com.iurysza.twittertelegrambot.utils

import com.iurysza.twittertelegrambot.model.AuthData
import com.twitter.hbc.httpclient.auth.OAuth1
import java.io.File

object AuthLoader {

    fun createAuthFrom(fileName: String): OAuth1 {
        val (consumerKey, consumerSecret, token, tokenSecret) = getAuthDataFrom(fileName)
        return OAuth1(consumerKey, consumerSecret, token, tokenSecret)
    }

    fun toOAuth(authData: AuthData): OAuth1 {
        val (consumerKey, consumerSecret, token, tokenSecret) = authData
        return OAuth1(consumerKey, consumerSecret, token, tokenSecret)
    }

    fun getAuthDataFrom(fileName: String): AuthData {
        val fileContent = getFileFromResources("/$fileName")
        return MoshiWrapper.fromJson(
            AuthData::class,
            fileContent
        )!!
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
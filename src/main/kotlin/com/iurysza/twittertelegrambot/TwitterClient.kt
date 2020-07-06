package com.iurysza.twittertelegrambot

import com.iurysza.twittertelegrambot.model.AuthData
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken


class TwitterClient(authData: AuthData) {

    private val twitter: Twitter = TwitterFactory().instance

    init {
        twitter.setOAuthConsumer(authData.consumerKey, authData.consumerSecret)
        val accessToken = AccessToken(authData.token, authData.tokenSecret)
        twitter.oAuthAccessToken = accessToken
    }

    fun getStatus(statusId: Long): Status? {
        return runCatching { twitter.showStatus(statusId) }.getOrNull()
    }
}
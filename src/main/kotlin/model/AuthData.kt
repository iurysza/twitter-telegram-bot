package com.iurysza.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthData(
    val consumerKey: String,
    val consumerSecret: String,
    val token: String,
    val tokenSecret: String
)

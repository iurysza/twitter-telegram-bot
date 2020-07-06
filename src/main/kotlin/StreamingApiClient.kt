package com.iurysza

import com.iurysza.streaming.FilterParams
import com.iurysza.streaming.FilteredStatusStream
import com.iurysza.utils.AuthLoader

fun main() {
    val filterParams = FilterParams(listOf(1278835179910504448L), listOf("@MandaProZap"))
    val auth = AuthLoader.createAuthFrom("twitter_auth.json")

    FilteredStatusStream(auth, filterParams) { newStatus ->
        println(newStatus)
    }.start()
}

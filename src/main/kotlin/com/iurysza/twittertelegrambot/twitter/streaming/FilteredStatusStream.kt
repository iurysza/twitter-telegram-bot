package com.iurysza.twittertelegrambot.twitter.streaming

import com.iurysza.twittertelegrambot.twitter.model.AuthData
import com.iurysza.twittertelegrambot.utils.AuthLoader
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.HttpHosts
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.twitter4j.Twitter4jStatusClient
import twitter4j.Status
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class FilteredStatusStream(
    private val authData: AuthData,
    private val filterParams: FilterParams,
    private val statusListener: (Status) -> Unit
) {

    companion object {
        private const val CAPACITY = 1_000
        private const val THREAD_POOL_SIZE = 4
    }

    var statusClient: Twitter4jStatusClient? = null

    fun start() {
        val (followings, terms) = filterParams

        val filters = createStatusFilters(followings, terms)
        val msgQueue = LinkedBlockingQueue<String>(100 * CAPACITY)
        val client = createBasicClient(filters, msgQueue, CAPACITY)

        statusClient = createStatusClient(client, msgQueue).apply {
            connect()
            process()
        }
    }

    private fun createStatusFilters(followings: List<Long>, terms: List<String>): StatusesFilterEndpoint {
        return StatusesFilterEndpoint().apply {
            followings(followings)
            trackTerms(terms)
        }
    }

    private fun createStatusClient(
        client: BasicClient?,
        msgQueue: LinkedBlockingQueue<String>,
        poolSize: Int = THREAD_POOL_SIZE
    ) = Twitter4jStatusClient(
        client,
        msgQueue,
        listOf(DefaultStatusListener(statusListener)),
        Executors.newFixedThreadPool(poolSize)
    )

    private fun createBasicClient(
        endpoint: StatusesFilterEndpoint,
        msgQueue: LinkedBlockingQueue<String>,
        eventQueueSize: Int
    ) = ClientBuilder()
        .hosts(HttpHosts(Constants.STREAM_HOST))
        .authentication(AuthLoader.toOAuth(authData))
        .endpoint(endpoint)
        .processor(StringDelimitedProcessor(msgQueue))
        .eventMessageQueue(LinkedBlockingQueue(eventQueueSize))
        .build()

}

data class FilterParams(val followings: List<Long>, val terms: List<String>)

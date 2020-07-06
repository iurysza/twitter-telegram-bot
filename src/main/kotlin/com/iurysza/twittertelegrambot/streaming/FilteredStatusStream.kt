package com.iurysza.twittertelegrambot.streaming

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.HttpHosts
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.httpclient.auth.OAuth1
import com.twitter.hbc.twitter4j.Twitter4jStatusClient
import twitter4j.Status
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class FilteredStatusStream(
    private val auth: OAuth1,
    private val filterParams: FilterParams,
    private val statusListener: (Status?) -> Unit
) {

    companion object {
        private const val capacity = 1_000
    }

    var statusClient: Twitter4jStatusClient? = null

    fun start() {
        val (followings, terms) = filterParams

        val filters = createStatusFilters(followings, terms)
        val msgQueue = LinkedBlockingQueue<String>(100 * capacity)
        val client = createBasicClient(filters, msgQueue, capacity)

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
        poolSize: Int = 4
    ) = Twitter4jStatusClient(
        client,
        msgQueue,
        listOf(DefaultStatusListener(statusListener)),
        Executors.newFixedThreadPool(poolSize)
    )

    private fun createBasicClient(
        endpoint: StatusesFilterEndpoint,
        msgQueue: LinkedBlockingQueue<String>,
        capacity: Int
    ): BasicClient? {
        return ClientBuilder()
            .hosts(HttpHosts(Constants.STREAM_HOST))
            .authentication(auth)
            .endpoint(endpoint)
            .processor(StringDelimitedProcessor(msgQueue))
            .eventMessageQueue(LinkedBlockingQueue(capacity))
            .build()
    }
}


data class FilterParams(val followings: List<Long>, val terms: List<String>)
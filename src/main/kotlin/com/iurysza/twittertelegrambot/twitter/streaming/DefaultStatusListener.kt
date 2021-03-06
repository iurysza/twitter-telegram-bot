package com.iurysza.twittertelegrambot.twitter.streaming

import twitter4j.StallWarning
import twitter4j.Status
import twitter4j.StatusDeletionNotice
import twitter4j.StatusListener

class DefaultStatusListener(private val statusListener: (Status) -> Unit) : StatusListener {
    override fun onStatus(status: Status?) {
        println("newStatus Received: $status")
        status?.let(statusListener)
    }

    override fun onTrackLimitationNotice(numberOfLimitedStatuses: Int) {
        println("numberOfLimitedStatuses = $numberOfLimitedStatuses")
    }

    override fun onStallWarning(warning: StallWarning?) {
        print("warning= $warning")
    }

    override fun onException(ex: Exception?) {
        ex?.printStackTrace()
    }

    override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice?) {
        println("statusDeletionNotice: $statusDeletionNotice ")
    }

    override fun onScrubGeo(userId: Long, upToStatusId: Long) {
        println("onScrubGeo $userId")
    }
}
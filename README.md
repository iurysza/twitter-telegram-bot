## Twitter video downloader bot

A simple bot to download videos from twitter and send them to telegram
```kotlin
fun main() {
    val authData = AuthLoader.getAuthDataFrom("auth_data.json")
    val filterParams = FilterParams(listOf(authData.twitterUserId),listOf("@MandaProZap"))

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
```

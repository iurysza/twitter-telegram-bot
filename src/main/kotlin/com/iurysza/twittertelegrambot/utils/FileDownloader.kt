package com.iurysza.twittertelegrambot.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.Okio
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.File

object FileDownloader {

    private val service = Retrofit
        .Builder()
        .baseUrl("http://localhost/")
        .build()
        .create(FileDownloadService::class.java)

    suspend fun downloadAndWriteToFile(url: String): File? = withContext(Dispatchers.IO) {
        val body = service.downloadFileFrom(url).await()
        writeBodyToFile(body)
    }

    private fun writeBodyToFile(responseBody: ResponseBody?): File? = runCatching {
        File("${System.currentTimeMillis()}.mp4").apply {
            createNewFile()
            Okio.buffer(Okio.sink(this)).write(responseBody!!.bytes()!!)
        }
    }.getOrNull()
}

interface FileDownloadService {
    @GET
    fun downloadFileFrom(@Url fileUrl: String): Call<ResponseBody?>
}
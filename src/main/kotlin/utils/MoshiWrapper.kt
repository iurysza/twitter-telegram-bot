package com.iurysza.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.reflect.KClass

object MoshiWrapper {
    val instance: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    inline fun <reified T : Any> fromJson(type: KClass<T>, json: String): T? {
        return instance.adapter(type.java).fromJson(json)
    }
}
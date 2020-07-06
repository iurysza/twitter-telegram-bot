@file:Suppress("SpellCheckingInspection")

import java.net.URI

plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    maven {
        url = URI.create("https://jitpack.io")
    }
}

dependencies {

    //region Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    //endregion

    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:5.0.0")

    //region HBC
    implementation("com.twitter:hbc-core:2.2.0")
    implementation("com.twitter:hbc-twitter4j:2.2.0")
    implementation("org.twitter4j:twitter4j-core:4.0.7")
    implementation("org.slf4j:slf4j-log4j12:1.6.0")
    //endregion

    //region Mosh
    implementation("com.squareup.moshi:moshi:1.9.3")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.3")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.3")
    //endregion

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

afterEvaluate {
    this.tasks.named("test", Test::class) {
        useJUnitPlatform()
    }
}
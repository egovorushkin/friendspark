package com.friendspark.data.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

val httpClient = HttpClient {
    install(ContentNegotiation) { json() }
    defaultRequest {
        url("http://10.0.2.2:8080/") // Android emulator
        // iOS/Desktop use localhost:8080
    }
}
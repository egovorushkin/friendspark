package com.friendspark.di

import com.friendspark.data.api.httpClient
import com.friendspark.data.repository.RegisterRequest
import com.friendspark.data.repository.RegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

interface FriendSparkApi {
    suspend fun register(request: RegisterRequest): RegisterResponse
}

class FriendSparkApiImpl(private val client: HttpClient = httpClient) : FriendSparkApi {
    override suspend fun register(request: RegisterRequest): RegisterResponse {
        val token = request.token // Add token to RegisterRequest if needed
        return client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
            if (token != null) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }.body()
    }
}

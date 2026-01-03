package com.friendspark.di

import com.friendspark.data.api.httpClient
import com.friendspark.data.repository.RegisterRequest
import com.friendspark.data.repository.RegisterResponse
import com.friendspark.util.AppLogger
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
        val url = "http://10.0.2.2:8090/api/v1/auth/register"
        val token = request.token

        AppLogger.logApi("Making POST request to: $url")
        AppLogger.logApi("Request has token: ${if (token != null) "Yes (length: ${token.length})" else "No"}")

        return try {
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                // TODO: hardcoded for tests
                setBody("{\"name\": \"User5 Friendspark\"}")
                if (token != null) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                    AppLogger.logNetwork("Authorization header set with Bearer token")
                }
            }.body<RegisterResponse>()

            AppLogger.logApi("Registration API response: success=${response.success}, userId=${response.userId}")
            response
        } catch (e: Exception) {
            AppLogger.logApiError("API call failed: ${e.message}", e)
            throw e
        }
    }
}

package io.github.kdroidfilter.netfreetools.network

import io.github.kdroidfilter.netfreetools.core.NetFreeUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

suspend fun fetchNetFreeUser(): NetFreeUser {
    // Configure the HTTP client with JSON support
    val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // ignore extra JSON fields
            })
        }
    }

    return try {
        // Perform GET and deserialize body into NetFreeUser
        client.get("https://api.internal.netfree.link/user/0").body()
    } catch (e: Exception) {
        // On any failure, return an "empty" user with isNetFree=false
        NetFreeUser(
            isNetFree  = false,
            userKey    = "",
            signature  = "",
            userId     = 0,
            serverName = "",
            plan       = 0,
            ip         = ""
        )
    } finally {
        client.close()
    }
}
package io.github.kdroidfilter.netfreetools.network

import io.github.kdroidfilter.netfreetools.core.NetFreeUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FetcherTest {

    @Test
    fun testFetchNetFreeUserSuccess() = runTest {
        // Prepare mock response
        val mockEngine = MockEngine { request ->
            // Verify request URL
            assertEquals("https://api.internal.netfree.link/user/0", request.url.toString())

            // Return a successful response with a sample NetFreeUser JSON
            respond(
                content = """
                    {
                        "isNetFree": true,
                        "userKey": "test-key",
                        "signature": "test-signature",
                        "userId": 123,
                        "servername": "test-server",
                        "plan": 456,
                        "ip": "192.168.1.1"
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        // Create a test implementation of fetchNetFreeUser that uses our mock engine
        val user = fetchNetFreeUserWithClient(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        })

        // Verify the response was correctly parsed
        assertTrue(user.isNetFree)
        assertEquals("test-key", user.userKey)
        assertEquals("test-signature", user.signature)
        assertEquals(123, user.userId)
        assertEquals("test-server", user.serverName)
        assertEquals(456, user.plan)
        assertEquals("192.168.1.1", user.ip)
    }

    @Test
    fun testFetchNetFreeUserError() = runTest {
        // Prepare mock engine that returns an error
        val mockEngine = MockEngine { request ->
            respond(
                content = "Error",
                status = HttpStatusCode.InternalServerError
            )
        }

        // Create a test implementation of fetchNetFreeUser that uses our mock engine
        val user = fetchNetFreeUserWithClient(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        })

        // Verify that on error, we get a default "empty" user with isNetFree=false
        assertFalse(user.isNetFree)
        assertEquals("", user.userKey)
        assertEquals("", user.signature)
        assertEquals(0, user.userId)
        assertEquals("", user.serverName)
        assertEquals(0, user.plan)
        assertEquals("", user.ip)
    }

    @Test
    fun testFetchNetFreeUserNetworkError() = runTest {
        // Prepare mock engine that throws an exception
        val mockEngine = MockEngine { _ ->
            throw Exception("Network error")
        }

        // Create a test implementation of fetchNetFreeUser that uses our mock engine
        val user = fetchNetFreeUserWithClient(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        })

        // Verify that on exception, we get a default "empty" user with isNetFree=false
        assertFalse(user.isNetFree)
        assertEquals("", user.userKey)
        assertEquals("", user.signature)
        assertEquals(0, user.userId)
        assertEquals("", user.serverName)
        assertEquals(0, user.plan)
        assertEquals("", user.ip)
    }

    @Test
    fun testFetchNetFreeUserMalformedJson() = runTest {
        // Prepare mock engine that returns malformed JSON
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "isNetFree": true,
                        "userKey": "test-key",
                        "signature": "test-signature",
                        "userId": "not-a-number", // This should cause a SerializationException
                        "servername": "test-server",
                        "plan": 456,
                        "ip": "192.168.1.1"
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        // Create a test implementation of fetchNetFreeUser that uses our mock engine
        val user = fetchNetFreeUserWithClient(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        })

        // Verify that on serialization error, we get a default "empty" user with isNetFree=false
        assertFalse(user.isNetFree)
        assertEquals("", user.userKey)
        assertEquals("", user.signature)
        assertEquals(0, user.userId)
        assertEquals("", user.serverName)
        assertEquals(0, user.plan)
        assertEquals("", user.ip)
    }

    @Test
    fun testFetchNetFreeUserEmptyResponse() = runTest {
        // Prepare mock engine that returns an empty response
        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        // Create a test implementation of fetchNetFreeUser that uses our mock engine
        val user = fetchNetFreeUserWithClient(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        })

        // Verify that on empty response, we get a default "empty" user with isNetFree=false
        assertFalse(user.isNetFree)
        assertEquals("", user.userKey)
        assertEquals("", user.signature)
        assertEquals(0, user.userId)
        assertEquals("", user.serverName)
        assertEquals(0, user.plan)
        assertEquals("", user.ip)
    }

    @Test
    fun testFetchNetFreeUserNotFound() = runTest {
        // Prepare mock engine that returns a 404 Not Found
        val mockEngine = MockEngine { request ->
            respond(
                content = "Not Found",
                status = HttpStatusCode.NotFound
            )
        }

        // Create a test implementation of fetchNetFreeUser that uses our mock engine
        val user = fetchNetFreeUserWithClient(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        })

        // Verify that on 404, we get a default "empty" user with isNetFree=false
        assertFalse(user.isNetFree)
        assertEquals("", user.userKey)
        assertEquals("", user.signature)
        assertEquals(0, user.userId)
        assertEquals("", user.serverName)
        assertEquals(0, user.plan)
        assertEquals("", user.ip)
    }

    @Test
    fun testFetchNetFreeUserNonNetFreeResponse() = runTest {
        // Prepare mock response with isNetFree=false
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "isNetFree": false,
                        "userKey": "non-netfree-key",
                        "signature": "non-netfree-signature",
                        "userId": 789,
                        "servername": "non-netfree-server",
                        "plan": 0,
                        "ip": "10.0.0.1"
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        // Create a test implementation of fetchNetFreeUser that uses our mock engine
        val user = fetchNetFreeUserWithClient(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        })

        // Verify the response was correctly parsed with isNetFree=false
        assertFalse(user.isNetFree)
        assertEquals("non-netfree-key", user.userKey)
        assertEquals("non-netfree-signature", user.signature)
        assertEquals(789, user.userId)
        assertEquals("non-netfree-server", user.serverName)
        assertEquals(0, user.plan)
        assertEquals("10.0.0.1", user.ip)
    }
}

// Helper function to allow testing with a custom HttpClient
suspend fun fetchNetFreeUserWithClient(client: HttpClient): NetFreeUser {
    return try {
        client.get("https://api.internal.netfree.link/user/0").body()
    } catch (e: Exception) {
        NetFreeUser(
            isNetFree = false,
            userKey = "",
            signature = "",
            userId = 0,
            serverName = "",
            plan = 0,
            ip = ""
        )
    } finally {
        client.close()
    }
}

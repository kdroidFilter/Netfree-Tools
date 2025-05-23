package io.github.kdroidfilter.netfreetools.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetFreeUser(
    @SerialName("isNetFree")
    val isNetFree: Boolean,            // Indicates if the user is on NetFree plan

    @SerialName("userKey")
    val userKey: String,               // Unique user key

    @SerialName("signature")
    val signature: String,             // Authentication signature

    @SerialName("userId")
    val userId: Int,                   // Numeric user ID

    @SerialName("servername")
    val serverName: String,            // Server name hosting the user

    @SerialName("plan")
    val plan: Int,                     // Plan identifier

    @SerialName("ip")
    val ip: String                     // User IP address
)
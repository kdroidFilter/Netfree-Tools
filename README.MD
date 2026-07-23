# Netfree Tools 🔧🌐

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kdroidfilter/netfreetools.core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.kdroidfilter%22%20AND%20a:%22netfreetools.core%22)

Netfree Tools is a Kotlin Multiplatform library designed to simplify integration with the NetFree service. It provides core data models, network fetchers, Android certificate bundles, and sample applications demonstrating usage across multiple targets (JVM, Android, iOS, JavaScript, and native). 🚀

---

## 📦 Modules

1. **core**: Defines the `NetFreeUser` data model using Kotlinx Serialization. 📄
2. **network**: Implements `fetchNetFreeUser()` using Ktor to retrieve user status from `https://api.internal.netfree.link/user/0`, with graceful fallback on failure. 🔄
3. **android-certificates**: Bundles all NetFree CA certificates for Android network security configurations. 🔒

---

## ✨ Features

* **Kotlin Multiplatform** support: JVM, Android, JavaScript (browser & Wasm), macOS, Linux, Windows, and iOS. 🔄
* **Serialization**: Uses Kotlinx Serialization to parse NetFree API responses. 📦
* **Networking**: Leverages Ktor with Content Negotiation for robust HTTP requests. 🌐
* **Certificate Management**: Pre-packaged NetFree certificates for Android network-security configurations. 🔐
* **Sample Apps**: Ready-to-run samples demonstrating integration and UI using Jetpack Compose and Compose Multiplatform. 🎨

---

## 🚀 Getting Started

### Prerequisites

* JDK 17+ ☕
* Android SDK (if building Android targets) 🤖
* Xcode (for iOS targets) 🍎

### 🔧 Installation

Add the library modules as dependencies in your Gradle project:

```kotlin
implementation("io.github.kdroidfilter:netfreetools.core:<version>")
implementation("io.github.kdroidfilter:netfreetools.network:<version>")
implementation("io.github.kdroidfilter:netfreetools.certificates:<version>")
```

> **Note**: The network module uses Ktor dependencies as `compileOnly` to avoid compatibility issues with different Ktor versions. You need to include your own Ktor dependencies in your project:
> ```kotlin
> implementation("io.ktor:ktor-client-core:YOUR_KTOR_VERSION")
> implementation("io.ktor:ktor-client-content-negotiation:YOUR_KTOR_VERSION")
> implementation("io.ktor:ktor-serialization-kotlinx-json:YOUR_KTOR_VERSION")
> // Add other Ktor dependencies as needed
> ```

---

## 📚 Usage

### Core Module

```kotlin
val user = Json.decodeFromString<NetFreeUser>(jsonString)
println("User is on NetFree: ${user.isNetFree}")
```

### Network Module

```kotlin
suspend fun checkNetFree() {
    val netFreeUser = fetchNetFreeUser()
    if (netFreeUser.isNetFree) {
        println("✅ NetFree user: ${netFreeUser.userKey}")
    } else {
        println("❌ Not a NetFree user or network error.")
    }
}
```

### Android Certificates Module

Simply add the **android-certificates** dependency to your Android project, no further setup is required beyond the import. All NetFree CA certificates are automatically bundled and applied via the provided `network_security_config.xml`, ensuring your app trusts NetFree's custom CAs without any additional configuration. 🔒

The bundle contains:

* The current unified **NetFree Root CA X2** roots (RSA, RSA 2037, ECC256, ECC384, ED25519), which are the ones NetFree signs with today.
* The **legacy unified roots**, still used by accounts that have not been migrated to X2.
* The remaining **per ISP `NetFree Sign` certificates** from the previous generation, for older deployments.

Two limitations are worth keeping in mind:

* `network-security-config` trust anchors only apply from API 24. On API 21 to 23 the certificates are ignored.
* The configuration only affects the Android TLS stack (`HttpsURLConnection`, OkHttp, WebView). Components shipping their own trust store, such as Flutter, Cronet or native code linked against its own TLS library, are not covered.

---

---

## 🤝 Contributing

Contributions are welcome! Please open issues or pull requests on the [GitHub repository](https://github.com/kdroidFilter/Netfree-Tools). 🛠️

---

## 📄 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details. 📝

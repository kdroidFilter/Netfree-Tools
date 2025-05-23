# Netfree Tools 🔧🌐

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
implementation("io.github.kdroidfilter.netfreetools:core:VERSION")
implementation("io.github.kdroidfilter.netfreetools:network:VERSION")
implementation("io.github.kdroidfilter.netfreetools.certificates:VERSION")
```

Replace `VERSION` with the current `libVersion` (see [gradle.properties](gradle.properties)). 🔖

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

Simply add the **android-certificates** dependency to your Android project—no further setup is required beyond the import. All NetFree CA certificates are automatically bundled and applied via the provided `network_security_config.xml`, ensuring your app trusts NetFree’s custom CAs without any additional configuration. 🔒---

---

## 🤝 Contributing

Contributions are welcome! Please open issues or pull requests on the [GitHub repository](https://github.com/kdroidFilter/Netfree-Tools). 🛠️

---

## 📄 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details. 📝

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
}

group = "io.github.kdroidfilter.netfreetools.certificates"
version = "1.0.0"

kotlin {
    jvmToolchain(17)

    androidTarget { publishLibraryVariants("release") }


    sourceSets {
        commonMain.dependencies {

        }

        commonTest.dependencies {
        }

    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compileTaskProvider.configure {
            compilerOptions {
                freeCompilerArgs.add("-Xexport-kdoc")
            }
        }
    }

}

android {
    namespace = "io.github.kdroidfilter.netfreetools.certificates"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}

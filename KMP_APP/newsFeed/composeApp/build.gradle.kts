import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinCocoapods)
}

// Add repositories for dependency resolution
//repositories {
//    mavenCentral()
//    google()
//}

kotlin {
    // --- ANDROID ---
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // --- iOS ---
    // These define the compilation targets for Kotlin/Native for iOS.
    // Even without CocoaPods, you might want to compile Kotlin code for these
    // architectures if you plan to use it in an iOS app through other means
    // (e.g., manual integration, or if another tool consumes these artifacts).
    // If you are *only* building the Android app and WasmJS app from this KMP module,
    // and have no plans to run any Kotlin code on an iOS device/simulator *at all*
    // from this module, you could remove these. But typically, you'd keep them
    // if "building for iOS" still means compiling Kotlin for iOS.
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // --- Cocoapods ---
    cocoapods {
        name = "ComposeApp"
        version = "1.0.0"
        summary = "KMP shared module with Compose Multiplatform"
        homepage = "https://github.com/your-org/your-repo"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // --- WASM / JS ---
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            commonWebpackConfig {
                sourceMaps = true
                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    val staticPaths: ListProperty<String> = project.objects.listProperty(String::class.java)
//                    staticPaths.set(listOf(project.rootDir.path, project.projectDir.path))
//                    static = staticPaths.get()
//                }
            }
            testTask {
                useKarma {
                    useSafari()
                }
            }
        }
        binaries.executable()
    }

    // --- SOURCE SETS ---
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Compose Multiplatform
                implementation(libs.jetbrains.compose.ui)
                implementation(libs.jetbrains.compose.material)
                implementation(libs.jetbrains.compose.material3)
                implementation(libs.compose.materialIconsExtended)
                implementation(libs.jetbrains.compose.componentsResources)

                // AndroidX Compose (platform-agnostic)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                // Navigation (Decompose for all platforms)
                implementation(libs.decompose)
                implementation(libs.decompose.compose)

                // Networking
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                // Serialization
                implementation(libs.kotlinx.serialization.json)

                // Date/Time
                implementation(libs.kotlinx.datetime)

                // Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // Dependency Injection
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.koin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.common)
                implementation(libs.ktor.client.android)
                implementation(libs.koin.android)
            }
        }

        val iosMain by creating {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(libs.koin.core.wasm.js)
            }
        }
    }
}

android {
    namespace = "org.hacorp.newsfeed"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.hacorp.newsfeed"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

// --- Extra Tasks ---
//tasks.register<Sync>("packageForXcode") {
//    group = "build"
//    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
//    dependsOn("assembleXCFramework")
//    from(layout.buildDirectory.dir("XCFrameworks/$mode/ComposeApp.xcframework"))
//    into(layout.buildDirectory.dir("xcode-frameworks/$mode"))
//}

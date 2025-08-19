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
    id("co.touchlab.faktory.kmmbridge") version "0.3.7"
    kotlin("native.cocoapods")
    id("maven-publish")
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
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // --- WASM / JS ---
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            commonWebpackConfig {
                sourceMaps = true
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(project.rootDir.path)
                        add(project.projectDir.path)
                    }
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
//                implementation(libs.jetbrains.androidx.core)
                implementation(libs.jetbrains.androidx.core.bundle)

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
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        // Link iOS targets to iosMain
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        configure(listOf(iosX64Main, iosArm64Main, iosSimulatorArm64Main)) {
            dependsOn(iosMain)
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(libs.koin.core.wasm.js)
            }
        }
    }

    // --- Cocoapods ---
    cocoapods {
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
tasks.register<Sync>("packageForXcode") {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    dependsOn("assembleXCFramework")
    from(layout.buildDirectory.dir("bin"))
    into(layout.buildDirectory.dir("xcode-frameworks/$mode"))
}
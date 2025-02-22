import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.material.icons.core) // Íconos básicos
            implementation(libs.androidx.material.icons.extended) // Íconos extendidos
            implementation(libs.kotlinx.datetime) // Asegúrate de usar la última versión
            implementation(libs.ktor.client.core) // Ktor core
            implementation(libs.ktor.client.cio) // Ktor client for Android/Multiplatform
            implementation(libs.ktor.client.json) // Ktor JSON serialization
            implementation(libs.ktor.client.logging) // Logging for HTTP requests
            implementation(libs.ktor.client.content.negotiation) // Content negotiation
            implementation(libs.ktor.serialization.kotlinx.json) // Kotlinx JSON serialization
            implementation(libs.kotlinx.datetime)

        }
    }
}

android {
    namespace = "org.dqmdz.consumer"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.dqmdz.consumer"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
        implementation(libs.ktor.client.auth)
        implementation(libs.ktor.client.cio.v234) // O el motor que prefieras
        implementation(libs.androidx.navigation.compose) // Versión de la navegación de Compose
        implementation(libs.androidx.ui) // Versión de Compose
        implementation(libs.material) // Versión de Material para Compose
    }
}

dependencies {
    implementation(kotlin("script-runtime"))
}
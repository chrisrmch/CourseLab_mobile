import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.navigationSafeArgs)
    idea
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.compose.material3)
            implementation(libs.androidx.compose.material3.wsc)
            implementation(libs.androidx.compose.material3.adaptative)
            implementation(libs.androidx.compose.material3.adaptative.layout)
            implementation(libs.androidx.compose.material3.adaptative.navigation)
            implementation(libs.okhttp.client)
            implementation(libs.navigation.compose.android)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.maps.locationcomponent)
            implementation(libs.androidx.appcompat)
            implementation(libs.mapbox.sdk.compose.extention)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.mapbox.android)
            implementation(libs.okhttp)
            implementation(libs.androidx.constraintlayout)
            runtimeOnly(libs.androidx.ui)
            implementation(libs.coil.network.okhttp)
            implementation(libs.accompanist.permissions)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor)
            implementation(libs.coil.compose)
            implementation(libs.ktor.negotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.navigation.compose.common)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.compose.m3.kmp)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3.adaptative)
            implementation(libs.compose.material3.adaptative.layout)
            implementation(libs.compose.material3.adaptative.navigation)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(libs.compose.window.sizeClass)
            implementation(libs.ktor.client.logging)
            implementation(libs.preferences.datastore)
            implementation(libs.datastore)
            implementation(libs.compose.foundation)
            implementation(libs.icons)
            implementation(libs.calendar)
            implementation(compose.material3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.common)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.okhttp.client)
        }
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

android {
    namespace = "org.courselab.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.courselab.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk .get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    androidResources {
        generateLocaleConfig = true
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.play.services.location)
    debugImplementation(compose.uiTooling)
}


compose.desktop {
    application {
        mainClass = "org.courselab.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.courselab.app"
            packageVersion = "1.0.0"
        }
    }
}
plugins {
    id("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id ("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.capstone.wastetotaste"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true

    }

    defaultConfig {
        applicationId = "com.capstone.wastetotaste"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"${project.findProperty("API_KEY")}\"")
    }

    buildTypes {
        debug{
            buildConfigField("String", "API_URL", "\"https://wastetotaste-lficrzkhvq-et.a.run.app/wastetotaste/\"")
            buildConfigField("String", "API_PREDICT_URL", "\"${project.findProperty("API_PREDICT_URL")}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_URL", "\"https://wastetotaste-lficrzkhvq-et.a.run.app/wastetotaste/\"")
            buildConfigField("String", "API_PREDICT_URL", "\"${project.findProperty("API_PREDICT_URL")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.android.async.http)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.room.paging)

    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)

    implementation (libs.glide)
    implementation (libs.compressor)

    // api
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)

    // core
    implementation (libs.androidx.datastore.preferences.v100)
    implementation (libs.androidx.lifecycle.livedata.ktx.v261)
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v261)

    implementation (libs.androidx.core.ktx.v1100)
    implementation (libs.androidx.appcompat.v170)
    implementation (libs.material.v180)
    implementation (libs.androidx.constraintlayout)
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.androidx.espresso.core)
    implementation (libs.androidx.swiperefreshlayout)
    implementation (libs.datastore.preferences)
    implementation (libs.datastore.preferences)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
}
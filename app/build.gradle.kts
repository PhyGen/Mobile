plugins {
    id("com.android.application") version "8.4.0"
    //id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android") version "1.9.23"
}

kotlin {
    jvmToolchain(11)
}

android {
    namespace = "com.example.phygen_java_1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.phygen_java_1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
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
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-auth:21.0.0")

//    // Firebase Messaging
//    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
//    implementation("com.google.firebase:firebase-messaging")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.12.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // JSON
    implementation("org.json:json:20231013")
    testImplementation("junit:junit:4.13.2")
}

//apply(plugin = "com.google.gms.google-services")

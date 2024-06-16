plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "pt.ipt.dama2024.betterday"
    compileSdk = 34

    defaultConfig {
        applicationId = "pt.ipt.dama2024.betterday"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.3"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // allow the access to objects of the interface, from code
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    // Add Retrofit Dependencies
    // https://square.github.io/retrofit/
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.gson)

    //https://github.com/Kotlin/kotlinx.coroutines
    implementation(libs.kotlinx.coroutines.android)

    //https://github.com/hdodenhof/CircleImageView
    implementation (libs.circleimageview)

    //https://github.com/square/picasso
    implementation(libs.squareup.picasso)

    // CameraX core library using the camera2 implementation
    //all the following camera implementation calls are retrieved from the learning class on cameraX
    //https://github.com/IPT-MEI-DAMA-2023-2024/Camera-X
    //https://developer.android.com/media/camera/camerax
    implementation (libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    // If you want to additionally use the CameraX Lifecycle library
    implementation(libs.androidx.camera.lifecycle)
    // CameraX View class
    implementation(libs.androidx.camera.view)
    // CameraX Extensions library
    implementation(libs.androidx.camera.extensions)

    // StreetMaps Libs
    implementation (libs.osmdroid.android)
    implementation(libs.play.services.maps)

    //
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
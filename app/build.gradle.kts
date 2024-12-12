plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.sargaev.aiedu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sargaev.aiedu"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "META-INF/native-image/**"
        }
    }
    buildFeatures {
        viewBinding = true
    }

}


dependencies {
    //implementation(libs.tensorflow.core.platform)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.eazegraph)
    implementation(libs.github.anychart.android)
    implementation(libs.smile.kotlin)
    implementation(libs.javacv)
    implementation(libs.javacpp)
    implementation(libs.openblas)
    implementation(libs.kotlin.stdlib)
    implementation(libs.openblas.platform)
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha04")
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    //implementation(libs.kotlin.deeplearning.tensorflow)
    //implementation(libs.tensorflow.core.native)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
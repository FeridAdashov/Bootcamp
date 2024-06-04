plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hiltProjectLevel)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.firebase.root)
}

android {
    namespace = "com.example.bootcamp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bootcamp"
        minSdk = 24
        targetSdk = 34
        versionName = "1.0.0"
        versionCode = 1

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
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

    kapt {
        correctErrorTypes = true
    }

    hilt {
        enableAggregatingTask = false
    }


    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.dynamicFeatures.fragment)

    implementation(libs.gson)
    implementation(libs.fragment.ktx)


    implementation(libs.hiltAndroid)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.swiperefreshlayout)

    //Calendar view
//    implementation(libs.calendar.view.airsaid)
    implementation(libs.calendar.view.material)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.common.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
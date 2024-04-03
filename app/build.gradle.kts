plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
}
android {

    packagingOptions {
        resources.excludes.add("META-INF/gradle/incremental.annotation.processors")
    }
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "HH_ACCESS_TOKEN", value = "\"${developProperties.hhAccessToken}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    // Koin
    implementation (libs.koin.android)

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)

    // UI layer libraries
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)

    // region Unit tests
    testImplementation(libs.unitTests.junit)
    // endregion

    // region UI tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
    // endregion

    implementation(libs.koin)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.viewpager2)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.coroutines)
    implementation(libs.paging)
    implementation(libs.javax.inject)
    implementation(libs.fragments.navigationFragment)
    implementation(libs.fragments.navigationUi)
    implementation(libs.fragments.fragmentKtx)
}
kapt {
    correctErrorTypes = true
}

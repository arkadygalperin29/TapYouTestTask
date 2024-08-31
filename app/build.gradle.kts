plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.navigation.fragment.safeargs)
}

android {
    namespace = "com.dev.agalperin"
    compileSdk = 34

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.dev.agalperin"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "TAP_YOU_TEST_API_BASE_URL",
            "\"https://hr-challenge.dev.tapyou.com/\""
        )

        testInstrumentationRunner = "androidx.test.runner.MockitoJUnitRunner"
    }

    hilt {
        enableAggregatingTask = true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Prepared XML solution for Chart
    implementation(libs.mpandroidchart)

    //Glide
    implementation(libs.github.glide)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.dagger.hilt.android)
    ksp(libs.hilt.android.compiler)

    //tests
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.mockito.core) {
        exclude("net.bytebuddy")
    }
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.test.mockito.android)
    androidTestImplementation(libs.test.android.hilt)
    androidTestImplementation(libs.test.androidx.junit)



    implementation(project(":tapyouapi"))
    testImplementation (project(":tapyouapi"))
    implementation(project(":core"))
}

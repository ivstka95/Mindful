plugins {
    alias(libs.plugins.mindful.android.application)
    alias(libs.plugins.mindful.android.library.compose)
    alias(libs.plugins.mindful.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ivan.karpiuk.mindful"

    defaultConfig {
        applicationId = "ivan.karpiuk.mindful"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.feature.onboarding)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.hilt.navigation.compose)
}

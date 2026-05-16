plugins {
    alias(libs.plugins.mindful.android.library)
    alias(libs.plugins.mindful.android.hilt)
    alias(libs.plugins.mindful.android.room)
}

android {
    namespace = "ivan.karpiuk.mindful.database"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.room.testing)
}

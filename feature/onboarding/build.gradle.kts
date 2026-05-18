plugins {
    alias(libs.plugins.mindful.android.feature)
    alias(libs.plugins.mindful.android.library.compose)
}

android {
    namespace = "ivan.karpiuk.mindful.feature.onboarding"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(projects.core.testing)
}

plugins {
    alias(libs.plugins.mindful.android.library)
    alias(libs.plugins.mindful.android.hilt)
}

android {
    namespace = "ivan.karpiuk.mindful.datastore"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.datastore.preferences)
}

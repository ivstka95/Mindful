plugins {
    alias(libs.plugins.mindful.android.library)
    alias(libs.plugins.mindful.android.hilt)
}

android {
    namespace = "ivan.karpiuk.mindful.database"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.datetime)
    ksp(libs.androidx.room.compiler)
}

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
    sourceSets {
        // Expose exported Room schemas as assets for MigrationTestHelper in JVM (Robolectric) tests
        getByName("test").assets.srcDir(project.files("schemas"))
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.room.testing)
}

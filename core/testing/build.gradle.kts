plugins {
    alias(libs.plugins.mindful.android.library)
}

android {
    namespace = "ivan.karpiuk.mindful.testing"
}

dependencies {
    api(libs.junit4)
    api(libs.mockk)
    api(libs.turbine)
    api(libs.kotlinx.coroutines.test)
}

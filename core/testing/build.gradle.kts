// This module must only be consumed via testImplementation — never implementation.
// MainDispatcherRule and fakes live in src/main so they are accessible from test
// source sets of consumer modules, but they must never reach a production APK.
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

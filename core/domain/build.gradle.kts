plugins {
    alias(libs.plugins.mindful.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.android)
}

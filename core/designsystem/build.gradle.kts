plugins {
    alias(libs.plugins.mindful.android.library)
    alias(libs.plugins.mindful.android.library.compose)
    alias(libs.plugins.compose.stability.analyzer)
}

composeStabilityAnalyzer {
    stabilityValidation {
        enabled.set(true)
        outputDir.set(layout.projectDirectory.dir("stability"))
        failOnStabilityChange.set(
            providers.gradleProperty("composeStabilityStrict").orNull?.toBoolean() ?: true,
        )
    }
}

android {
    namespace = "ivan.karpiuk.mindful.designsystem"
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.ui.text.google.fonts)
}

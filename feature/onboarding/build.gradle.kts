plugins {
    alias(libs.plugins.mindful.android.feature)
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
    namespace = "ivan.karpiuk.mindful.feature.onboarding"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
}

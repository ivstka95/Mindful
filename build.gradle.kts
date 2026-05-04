// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    // Apply ktlint + detekt only after a Kotlin/Android plugin is on this project,
    // so source-set discovery (especially for Android variants) succeeds.
    val applyCodeQuality = {
        if (!plugins.hasPlugin(rootProject.libs.plugins.ktlint.get().pluginId)) {
            apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)
            apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)

            extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
                config.setFrom(rootProject.files("config/detekt/detekt.yml"))
                buildUponDefaultConfig = true
                parallel = true
                autoCorrect = false
                basePath = rootProject.projectDir.absolutePath
            }

            extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
                version.set("1.3.1")
                android.set(true)
                ignoreFailures.set(false)
                reporters {
                    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
                    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
                }
                filter {
                    exclude { it.file.path.contains("/build/") }
                }
            }

            tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
                jvmTarget = "17"
                reports {
                    html.required.set(true)
                    xml.required.set(true)
                    txt.required.set(false)
                    sarif.required.set(false)
                    md.required.set(false)
                }
            }
            tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
                jvmTarget = "17"
            }
        }
    }

    plugins.withId("com.android.application") { applyCodeQuality() }
    plugins.withId("com.android.library") { applyCodeQuality() }
    plugins.withId("org.jetbrains.kotlin.jvm") { applyCodeQuality() }
}

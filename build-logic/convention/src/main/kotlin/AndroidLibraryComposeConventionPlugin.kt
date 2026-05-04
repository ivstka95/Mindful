import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Adds Compose support to any Android module (application or library).
 * Does NOT apply com.android.library — assumes the appropriate Android plugin
 * is already applied by another convention plugin.
 */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Wait for an Android plugin to be applied, then configure Compose.
            pluginManager.withPlugin("com.android.application") {
                val extension = extensions.getByType<com.android.build.api.dsl.ApplicationExtension>()
                configureAndroidCompose(extension)
            }
            pluginManager.withPlugin("com.android.library") {
                val extension = extensions.getByType<com.android.build.api.dsl.LibraryExtension>()
                configureAndroidCompose(extension)
            }
        }
    }
}

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    val libs = libs()
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("androidTestImplementation", platform(bom))

        add("implementation", libs.findLibrary("androidx-compose-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-compose-foundation").get())
        add("implementation", libs.findLibrary("androidx-compose-material3").get())
        add("implementation", libs.findLibrary("androidx-compose-runtime").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())

        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())

        add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
    }
}

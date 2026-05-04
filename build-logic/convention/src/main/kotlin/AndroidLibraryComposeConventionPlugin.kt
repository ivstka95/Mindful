import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
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
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            pluginManager.withPlugin("com.android.application") {
                extensions.getByType<ApplicationExtension>().buildFeatures.compose = true
                addComposeDependencies()
            }
            pluginManager.withPlugin("com.android.library") {
                extensions.getByType<LibraryExtension>().buildFeatures.compose = true
                addComposeDependencies()
            }
        }
    }
}

private fun Project.addComposeDependencies() {
    val libs = libs()
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

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig {
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            val libs = libs()
            dependencies {
                // :core:testing is excluded to avoid a circular dependency — it applies this plugin.
                if (target.path != ":core:testing") {
                    add("testImplementation", target.project(":core:testing"))
                }
                add("androidTestImplementation", libs.findLibrary("androidx-test-runner").get())
                add("androidTestImplementation", libs.findLibrary("androidx-test-ext-junit").get())
            }
        }
    }
}

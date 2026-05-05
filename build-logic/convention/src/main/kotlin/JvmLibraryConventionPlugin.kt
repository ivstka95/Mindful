import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            val libs = libs()
            val jdkTarget = libs.findVersion("jdkTarget").get().requiredVersion
            val javaVersion = JavaVersion.toVersion(jdkTarget)

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }

            extensions.configure<KotlinJvmExtension> {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(jdkTarget))
                }
            }

            dependencies {
                add("testImplementation", libs.findLibrary("junit-jupiter").get())
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("turbine").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testRuntimeOnly", libs.findLibrary("junit-platform-launcher").get())
            }

            tasks.withType<Test>().configureEach { useJUnitPlatform() }
        }
    }
}

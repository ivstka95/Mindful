import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.libs(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

/** Configure the base Kotlin/Android settings shared by app and library modules. */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension) {
    val libs = libs()
    val jdkTarget = libs.findVersion("jdkTarget").get().requiredVersion
    val javaVersion = JavaVersion.toVersion(jdkTarget)
    commonExtension.apply {
        compileSdk =
            libs
                .findVersion("compileSdk")
                .get()
                .requiredVersion
                .toInt()

        defaultConfig.apply {
            minSdk =
                libs
                    .findVersion("minSdk")
                    .get()
                    .requiredVersion
                    .toInt()
        }

        compileOptions.apply {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
            isCoreLibraryDesugaringEnabled = false
        }
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(jdkTarget))
            allWarningsAsErrors.set(false)
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
        }
    }

    tasks.withType<Test>().configureEach { useJUnitPlatform() }
}

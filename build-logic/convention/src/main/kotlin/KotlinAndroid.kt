import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

internal fun Project.libs(): VersionCatalog =
    extensions.getByType<VersionCatalogsExtension>().named("libs")

/** Configure the base Kotlin/Android settings shared by app and library modules. */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension) {
    val libs = libs()
    commonExtension.apply {
        compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()

        defaultConfig.apply {
            minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
        }

        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            isCoreLibraryDesugaringEnabled = false
        }
    }

    configureKotlin<KotlinAndroidProjectExtension>()
}

internal inline fun <reified T> Project.configureKotlin() where T : Any, T : ExtensionAware {
    extensions.configure<T> {
        val compilerOptions = (this as ExtensionAware).extensions.findByName("compilerOptions")
            ?: error("compilerOptions not found on extension ${T::class}")
        @Suppress("UNCHECKED_CAST")
        (compilerOptions as KotlinJvmCompilerOptions).apply {
            jvmTarget.set(JvmTarget.JVM_17)
            allWarningsAsErrors.set(false)
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
        }
    }
}

/** Type alias for the Kotlin Android project extension class introduced in Kotlin 2.x. */
internal typealias KotlinAndroidProjectExtension = org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

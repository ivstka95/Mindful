plugins {
    `kotlin-dsl`
}

group = "ivan.karpiuk.mindful.buildlogic"

val jdkTarget = libs.versions.jdkTarget.get()

java {
    sourceCompatibility = JavaVersion.toVersion(jdkTarget)
    targetCompatibility = JavaVersion.toVersion(jdkTarget)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(jdkTarget)
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.hilt.gradle.plugin)
    compileOnly(libs.androidx.room.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "mindful.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "mindful.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "mindful.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "mindful.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "mindful.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "mindful.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("jvmLibrary") {
            id = "mindful.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}

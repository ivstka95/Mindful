plugins {
  alias(libs.plugins.mindful.android.library)
  alias(libs.plugins.mindful.android.hilt)
  alias(libs.plugins.mindful.android.room)
}

android {
  namespace = "ivan.karpiuk.mindful.database"
}

dependencies {
  implementation(projects.core.domain)
  implementation(libs.kotlinx.datetime)
}

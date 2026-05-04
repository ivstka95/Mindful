plugins {
  alias(libs.plugins.mindful.android.feature)
  alias(libs.plugins.mindful.android.library.compose)
}

android {
  namespace = "ivan.karpiuk.mindful.feature.onboarding"
}

dependencies {
  implementation(projects.core.designsystem)
  implementation(projects.core.domain)
}

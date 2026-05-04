plugins {
  alias(libs.plugins.mindful.android.library)
  alias(libs.plugins.mindful.android.library.compose)
}

android {
  namespace = "ivan.karpiuk.mindful.designsystem"
}

dependencies {
  api(libs.androidx.compose.material3)
  api(libs.androidx.compose.ui.text.google.fonts)
}

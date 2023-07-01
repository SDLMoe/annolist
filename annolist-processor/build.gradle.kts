plugins {
  kotlin("jvm")
}

dependencies {
  implementation(project(":annolist-annotations"))
  implementation(libs.ksp.api)
}

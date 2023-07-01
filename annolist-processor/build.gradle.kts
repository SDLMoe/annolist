plugins {
  kotlin("jvm")
  publish
}

dependencies {
  implementation(project(":annolist-annotations"))
  implementation(libs.ksp.api)
}

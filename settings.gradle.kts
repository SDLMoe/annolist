pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
  }
}

rootProject.name = "annolist"

include(":annolist-annotations")
include(":annolist-processor")
include(":test-project")

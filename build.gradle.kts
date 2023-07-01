import com.diffplug.gradle.spotless.FormatExtension

plugins {
  id("com.google.devtools.ksp") version "1.8.22-1.0.11" apply false
  kotlin("jvm") apply false
  // NOT AN ERROR, it's a bug, see: https://youtrack.jetbrains.com/issue/KTIJ-19369
  // You can install a plugin to suppress it:
  // https://plugins.jetbrains.com/plugin/18949-gradle-libs-error-suppressor
  alias(libs.plugins.spotless)
}

allprojects {
  repositories {
    mavenCentral()
  }
  group = "moe.sdl.annolist"
  version = "0.1.0"
}

installGitHooks()

spotless {
  fun FormatExtension.excludes() {
    targetExclude("**/build/", "**/generated/", "**/resources/")
  }

  fun FormatExtension.common() {
    trimTrailingWhitespace()
    lineEndings = com.diffplug.spotless.LineEnding.UNIX
    endWithNewline()
  }

  val ktlintConfig = mapOf(
    "ij_kotlin_allow_trailing_comma" to "true",
    "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
    "trailing-comma-on-declaration-site" to "true",
    "trailing-comma-on-call-site" to "true",
    "ktlint_standard_no-wildcard-imports" to "disabled",
    "ktlint_disabled_import-ordering" to "disabled",
  )

  kotlin {
    target("**/*.kt")
    excludes()
    common()
    ktlint(libs.versions.ktlint.get()).editorConfigOverride(ktlintConfig)
  }

  kotlinGradle {
    target("**/*.gradle.kts")
    excludes()
    common()
    ktlint(libs.versions.ktlint.get()).editorConfigOverride(ktlintConfig)
  }
}

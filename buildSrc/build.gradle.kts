plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
  implementation(libs.kotlin.gradle.plugin)
//    implementation(libs.build.kotlinpoet)
//    implementation(libs.build.buildconfig)
//    implementation(libs.build.shadow)
}

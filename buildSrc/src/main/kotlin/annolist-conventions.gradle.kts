import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  java
}

repositories {
  mavenCentral()
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
  maven("https://plugins.gradle.org/m2/")
}

dependencies {
  constraints {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  }

  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  testImplementation(kotlin("test"))
}

sourceSets {
  main {
    java {
      setSrcDirs(setOf("kotlin")) // No Java, and Kotlin Only
    }
  }
  test {
    java {
      setSrcDirs(setOf("kotlin")) // No Java, and Kotlin Only
    }
  }
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.apply {
    jvmTarget = "8"
  }
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}

plugins {
  `java-library`
  `maven-publish`
  signing
}

java {
  withJavadocJar()
  withSourcesJar()
}

val secretPropsFile: File = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
  val props = getRootProjectLocalProps()
  props.forEach { t, u -> ext[t] = u }
} else {
  ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
  ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
  ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
  ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
  ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

publishing {
  publications {
    create<MavenPublication>("mavenKotlin") {
      artifactId = project.name
      from(components["java"])
      versionMapping {
        usage("java-api") {
          fromResolutionOf("runtimeClasspath")
        }
        usage("java-runtime") {
          fromResolutionResult()
        }
      }

      pom {
        name.set("Annolist")
        description.set("Kotlin KSP compile time list generating.")
        url.set("https://github.com/SDLMoe/annolist")

        licenses {
          license {
            name.set("Apache License, Version 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }

        developers {
          developer {
            id.set("Colerar")
          }
        }

        scm {
          connection.set("scm:git:git://github.com/SDLMoe/annolist.git")
          developerConnection.set("scm:git:ssh://github.com/SDLMoe/annolist.git")
          url.set("https://github.com/SDLMoe/annolist")
        }
      }
    }

    repositories {
      maven {
        name = "sonatype"
        val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
        val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
        val url = if (version.toString().contains("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        setUrl(url)
        credentials {
          username = getExtraString("ossrhUsername")
          password = getExtraString("ossrhPassword")
        }
      }
    }
  }
}

signing {
  sign(publishing.publications["mavenKotlin"])
}

tasks.javadoc {
  if (JavaVersion.current().isJava9Compatible) {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
  }
}

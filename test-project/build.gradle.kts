plugins {
  kotlin("jvm")
  id("com.google.devtools.ksp")
  application
}

application {
  mainClass.set("moe.sdl.test.MainKt")
}

// Makes generated code visible to IDE
kotlin.sourceSets.main {
  kotlin.srcDirs(
    file("$buildDir/generated/ksp/main/kotlin"),
  )
}

dependencies {
  implementation(project(":annolist-annotations"))
  ksp(project(":annolist-processor"))
}

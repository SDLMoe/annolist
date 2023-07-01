# Annolist

A KSP plugin for generating compile-time annotated variable list.

## Usage

### Setup Gradle

```kotlin
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
  implementation("moe.sdl.annolist:annolist-annotations:<VERSION>")
  ksp("moe.sdl.annolist:annolist-processor:<VERSION>")
}
```

### Kotlin

1. Create an annotation class for annotating variables

```kotlin
// moe.sdl.test.Main

@AnnoList(
  listName = "TEST_LIST",
  pkg = "moe.sdl.test",
  fileName = "Property",
  isInternal = true,
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY)
annotation class TestList

data class SomeComplexClass<T>(val value: T)
```

2. Annotate variables with the class created before.

```kotlin
// moe.sdl.test.A
@TestList
val test = SomeComplexClass(1)
```

```kotlin
// moe.sdl.test.B
package moe.sdl.test
@TestList
val test1 = SomeComplexClass(123.123)
```

```kotlin
// moe.sdl.test.C
package moe.sdl.test
@TestList
val test2 = SomeComplexClass("Hello")
```

3. Compile and you can see the generated source file:

```kotlin
package moe.sdl.test

internal val TEST_LIST = listOf(
  moe.sdl.test.test,
  moe.sdl.test.test1,
  moe.sdl.test.test2,
)
```

4. Use the list:

```kotlin
fun main() {
  TEST_LIST.forEach {
    println(it.value)
  }
}
```

## Roadmap

- [x] Support top-level varaible
- [ ] Support `object` and `companion object`
- [ ] Better error handling and error message
- [ ] Use something like `kotlinpoet` instead of hand-writing to generating source code

## License

Distributed under the [Apache 2.0 license](/LICENSE).

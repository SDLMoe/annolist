package moe.sdl.test

import moe.sdl.annolist.annotations.AnnoList

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

fun main() {
  TEST_LIST.forEach {
    println(it.value)
  }
}

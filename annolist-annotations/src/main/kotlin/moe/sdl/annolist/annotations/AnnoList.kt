package moe.sdl.annolist.annotations

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AnnoList(
  val listName: String,
  val pkg: String,
  val fileName: String,
  val isInternal: Boolean,
)

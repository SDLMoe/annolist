package moe.sdl.annolist.processor

import com.google.devtools.ksp.symbol.KSAnnotation

val KSAnnotation.qualifiedName
  get() = annotationType.resolve().declaration.qualifiedName

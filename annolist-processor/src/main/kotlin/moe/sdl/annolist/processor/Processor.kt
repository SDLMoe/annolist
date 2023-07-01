@file:Suppress("UnnecessaryVariable")

package moe.sdl.annolist.processor

import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import moe.sdl.annolist.annotations.AnnoList
import java.io.OutputStream

private val ANNO_LIST_CLASS_NAME = AnnoList::class.qualifiedName!!
private val TARGET_CLASS_NAME = Target::class.qualifiedName!!

private val SUPPORTED_TARGET = arrayListOf(AnnotationTarget.PROPERTY).map { it.toString() }

class Processor(
  private val options: Map<String, String>,
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator,
) : SymbolProcessor {

  operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
  }

  @Suppress("UNCHECKED_CAST")
  override fun process(resolver: Resolver): List<KSAnnotated> {
    val markedAnnolist = resolver
      .getSymbolsWithAnnotation(ANNO_LIST_CLASS_NAME)
      .filterIsInstance<KSClassDeclaration>()
      .filter { it.classKind == ClassKind.ANNOTATION_CLASS }

    if (markedAnnolist.none()) return emptyList()

    val failed = mutableListOf<KSAnnotated>()

    markedAnnolist.forEach anno@{ marked ->
      val annoListAnno = marked.annotations.first { it.qualifiedName!!.asString() == ANNO_LIST_CLASS_NAME }
      val targetAnno = marked.annotations
        .firstOrNull { it.qualifiedName!!.asString() == TARGET_CLASS_NAME }
        ?: run {
          logger.error("Annotation class `${marked.qualifiedName!!.asString()}` has no @Target")
          return@anno
        }
      val targetList = targetAnno.arguments.first().value as ArrayList<KSType>
      if (targetList.isEmpty()) return@anno
      targetList.forEach {
        val shortName = it.declaration.simpleName.getShortName()
        if (!SUPPORTED_TARGET.contains(shortName)) {
          logger.error(
            "Annotation class `${marked.qualifiedName!!.asString()}` is annotated with unsupported @Target `$it`",
          )
          return@anno
        }
      }

      val options = AnnoList(
        listName = annoListAnno.arguments[0].value as String,
        pkg = annoListAnno.arguments[1].value as String,
        fileName = annoListAnno.arguments[2].value as String,
        isInternal = annoListAnno.arguments[3].value as Boolean,
      )
      val elements = findElements(resolver, marked.qualifiedName!!.asString(), options)
      failed.addAll(elements)
    }

    return failed
  }

  private fun findElements(resolver: Resolver, annotation: String, options: AnnoList): List<KSAnnotated> {
    val marked = resolver.getSymbolsWithAnnotation(annotation)
      .filterIsInstance<KSPropertyDeclaration>()

    if (marked.none()) return emptyList()
    codeGenerator.createNewFile(
      dependencies = Dependencies(false, *marked.mapNotNull { it.containingFile }.toList().toTypedArray()),
      packageName = options.pkg,
      fileName = options.fileName,
    ).use {
      it += "package ${options.pkg}\n"
      it += "\n"
      if (options.isInternal) it += "internal "
      it += "val ${options.listName} = listOf(\n"
      marked.forEach prop@{ prop ->
        if (!prop.hasBackingField) {
          logger.error("${prop.qualifiedName?.asString()} has no backing field, unsupported.")
          return@prop
        }
        if (!prop.isPublic() && !prop.isInternal()) {
          logger.error("${prop.qualifiedName?.asString()} property must be public or internal, unsupported.")
          return@prop
        }
        it += "  ${prop.qualifiedName!!.asString()},\n"
      }
      it += ")\n"
    }

    return emptyList()
  }
}

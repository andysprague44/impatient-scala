package forthe.impatient.chapter9

import java.io.File
import java.nio.file.FileVisitOption

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.reflect.{ClassTag, _}

/**
  * Utility methods for using files and directories.
  */
object FileFunctions {

  /** Wrapper for scala Source, makes sure we close the source after */
  def withSource[V](file: File)(code: scala.io.Source => V): V = {
    val source = Source.fromFile(file)

    try {
      code(source) //do something
    } finally {
      source.close()
    }
  }

  /** Wrapper for anything that implements the closeable interface.
    * Makes sure we close it after we do something */
  def withCloseable[U <: java.io.Closeable, V](file: File)(code: U => V)(implicit t: ClassTag[U]): V = {
    import java.io._
    val out: U = {
      t match {
        case c: Any if c == classTag[ObjectOutputStream] => new ObjectOutputStream(new FileOutputStream(file))
        case c: Any if c == classTag[PrintWriter] => new PrintWriter(file)
        case c: Any if c == classTag[ObjectInputStream] => new ObjectInputStream(new FileInputStream(file))
        case c: Any => throw new UnsupportedOperationException(s"Type $t not supported")
      }
    }.asInstanceOf[U]

    try {
      code(out) //do something
    } finally {
      out.close()
    }
  }

  /** Walk a directory and return the results of a function ran on each file */
  def withDirectory[T](dirPath: java.nio.file.Path,
                       options: java.util.EnumSet[FileVisitOption] = java.util.EnumSet.noneOf(classOf[FileVisitOption]),
                       maxDepth: Int = Integer.MAX_VALUE)(fileFn: java.nio.file.Path => T): Seq[T] = {
    import java.nio.file._

    val resultCollector: ArrayBuffer[T] = ArrayBuffer[T]()

    def visitFile(f: (Path) => T): FileVisitor[Path] = new SimpleFileVisitor[Path] {
      override def visitFile(p: Path, attrs: attribute.BasicFileAttributes) = {
        resultCollector.append(f(p))
        FileVisitResult.CONTINUE
      }
    }

    Files.walkFileTree(dirPath, options, maxDepth, visitFile(fileFn))
    resultCollector.toList
  }

}

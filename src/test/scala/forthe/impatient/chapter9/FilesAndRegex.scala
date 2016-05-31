package forthe.impatient.chapter9

import java.io.File

import org.scalatest.{FunSuite, Matchers}

import scala.io.Source
import scala.util.Try

/**
  * Chapter 9 exercises
  */
class FilesAndRegex extends FunSuite with Matchers {

  val testResourceDir = new File(ClassLoader.getSystemResource("").toURI)

  /** Source wrapper to make sure we always close the source after we have finished */
  def withSource[T](fileName: String)(f: scala.io.Source => T): Try[T] = {
    val source = Source.fromFile(new File(ClassLoader.getSystemResource(fileName).toURI))
    Try {
      val g = f(source) //do what we want to do with the source
      source.close() //close on success
      g //return result
    } recover {
      case g: Throwable =>
        source.close() //close on error
        throw g
    }
  }

  /** PrintWriter wrapper to make sure we always close the writer after we hae finished */
  def withWriter(fileName: String)(f: java.io.PrintWriter => Unit): Try[Unit] = {
    val out = new java.io.PrintWriter(new File(s"$testResourceDir\\$fileName"))
    Try {
      f(out) //write something
      out.close() //close on success
    } recover {
      case g: Throwable =>
        out.close() //close on error
        throw g
    }
  }

  /** Delete a file */
  def deleteFile(fileName: String): Unit = {
    new File(ClassLoader.getSystemResource(fileName).toURI).delete()
  }

  test("q1: Write a scala code snippet to reverse lines in a file") {
    val reversed = withSource[Array[String]]("count.txt") { source => source.getLines().toArray.reverse }.get

    withWriter("output.txt") { out => reversed.foreach(out.println) }

    val res = withSource[Array[String]]("output.txt") { source => source.getLines().toArray }.get
    res.head should be("ten")
    res.last should be("one")

    deleteFile("output.txt")

  }

  test("q2: Reads a file with tabs and creates rows with n-columns") {
    //What I think this question means is that we are lining up the columns as well as removing tabs:
    //  String\tInt\tGerman\tMusical\n
    //  One\t1\tEin\tSolo\n
    //  Two\t2\tSwei\tDuet\n
    //  Three\t3\tDrei\tTriplet\n
    //  Fifty-five\t55\tFundundfunfzig\tLots\n
    // Becomes =>
    //  String     Int String         Musical
    //  One        1   Ein            Solo
    //  Two        2   Swei           Duet
    //  Three      3   Drei           Triplet
    //  Fifty-five 55  Fundundfunfzig Lots

    /* Write a test file */
    val testFile = s"String\tInt\tGerman\tMusical\nOne\t1\tEin\tSolo\nTwo\t2\tSwei\tDuet\nThree\t3\tDrei\tTriplet\nFiftyfive\t55\tFunfundfunfzig\tLots"
    withWriter("numberTable.txt") { out => out.print(testFile) }

    val alignedTable = withSource[Array[String]]("numberTable.txt") { source =>
      val table = source.getLines().toArray.map(_.split(s"\t"))

      //Get the maximum number of columns
      val numCols = table.map(_.length).max

      //Set all rows to have the same number of columns (use a zip later on that needs lists of equal length)
      val paddedTable = table.map(row => row.padTo[String, Array[String]](numCols, ""))

      //Get the maximum cell width in each column
      val colMaxWidths = (0 until numCols).map(i => paddedTable.map(row => row(i).length).max).toList

      //Set the width of each cell to the maximum cell width in the column
      val alignedTable = paddedTable.map(
        row => (row zip colMaxWidths).map {
          case (cell, width) => cell.padTo[Char, String](width + 1, ' ')
        }
      )

      //Make each row a string
      alignedTable.map(_.mkString)
    }.get

    withWriter("output.txt") { out => alignedTable.foreach(r => {
      println(r); r
    })
    }

    alignedTable.length should be(5)
    alignedTable.head.length should be(37)
    alignedTable.last should be("Fiftyfive 55  Funfundfunfzig Lots    ")

  }



  test("q3: 1 liner to read a file and print all words larger than 12 characters to the console") {
    val file = new File(s"$testResourceDir\\heyJudeLyrics.txt")
    val words = Source.fromFile(file).mkString.split("\\s+").filter(_.length > 8).map(w => {
      println(w); w
    })

    words.length should be(1)
    words.head should be("shoulders")
  }

  test("q4:Write a scala program to read floating point numbers only. Get sum, average, min, max") {
    withWriter("numbers.txt") { out => out.print("1.0 2.5 2.5 4.0") }

    val (sum, average, min, max) = withSource[(Double, Double, Double, Double)]("numbers.txt") { source =>
      val numbers = source.mkString.split("\\s+").map(_.toDouble)
      (numbers.sum, numbers.sum / numbers.length, numbers.min, numbers.max)
    }.get

    sum should be(10.0)
    average should be(2.5)
    min should be(1.0)
    max should be(4.0)

  }

  test("q5: ")


}
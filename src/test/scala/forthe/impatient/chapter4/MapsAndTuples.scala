package forthe.impatient.chapter4

import java.io.File
import java.util.Calendar

import org.scalatest.prop.Checkers
import org.scalatest.{FunSuite, Matchers}

import scala.collection.{SortedMap, mutable}

/**
  * Chapter 4 exercies
  */
class MapsAndTuples extends FunSuite with Matchers with Checkers {

  test("q1: Set up  map of prices, then apply a 10% discount") {
    val prices = Map("simbaLicense" -> 1000.00, "goPro" -> 500.00, "returnFlightsToAustralia" -> 2000.00)
    val discounted = for((k, v) <- prices) yield (k, v * 0.9)

    discounted("goPro") should be (450.00)
  }

  test("q2: read from file and count how often each word appears") {
    //Not worrying about scala file reading just yet
    val in = new java.util.Scanner(new File(ClassLoader.getSystemResource("heyJudeLyrics.txt").toURI))
    val words = collection.mutable.Map[String, Int]()

    /* Iterate through and count each instance of a word */
    while(in.hasNext()) {
      val newWord = in.next().replace(",", "").toLowerCase
      val entry = words.getOrElseUpdate(newWord, 0)
      words(newWord) = entry + 1
    }
    println(words.mkString("|"))

    words("hey") should equal (23)
    words("jude") should equal (23)
    words("nah") should equal (162)
  }

  test("q3: repeat with immutable map") {
    val in = scala.io.Source.fromFile(new File(ClassLoader.getSystemResource("heyJudeLyrics.txt").toURI))
                               .getLines
                               .flatMap(_.split("\\W+")) //gets words from lines

    val words = in.foldLeft(Map.empty[String, Int]){
      (currentMap, word) => {
        val parsedWord = word.replace(",", "").toLowerCase()
        currentMap + (parsedWord -> (currentMap.getOrElse(parsedWord, 0) + 1))
      }
    }

    println(words.mkString("|"))
    words("hey") should equal (23)
    words("jude") should equal (23)
    words("nah") should equal (162)
  }

  test("q4: repeat with a sorted map") {
    val in = scala.io.Source.fromFile(new File(ClassLoader.getSystemResource("heyJudeLyrics.txt").toURI))
      .getLines
      .filter(_ != "") //ignore empty lines
      .flatMap(_.split("\\W+")) //gets words from lines

    //currentMap is the accumulated map, word is the next word to add/update
    val words = in.foldLeft(SortedMap.empty[String, Int]){
      (currentMap, word) => {
        val parsedWord = word.replace(",", "").toLowerCase()
        currentMap + (parsedWord -> (currentMap.getOrElse(parsedWord, 0) + 1))
      }
    }

    println(words.mkString("|"))
    words.firstKey should be ("a")
    words.lastKey should be ("your")
  }

  test("q5: repeat with a util.TreeMap") {
    val in = scala.io.Source.fromFile(new File(ClassLoader.getSystemResource("heyJudeLyrics.txt").toURI))
      .getLines
      .filter(_ != "") //ignore empty lines
      .flatMap(_.split("\\W+")) //gets words from lines

    //currentMap is the accumulated map, word is the next word to add/update
    val words = in.foldLeft(new java.util.TreeMap[String, Int]()){
      (currentMap, word) => {
        val parsedWord = word.replace(",", "").toLowerCase()
        currentMap.put(parsedWord, currentMap.getOrDefault(parsedWord, 0) + 1)
        currentMap
      }
    }

    import scala.collection.JavaConverters._
    println(words.asScala.mkString("|"))
    words.firstKey should be ("a")
    words.lastKey should be ("your")
  }

  test("q6: defined linked hash map for 'Monday' to Calendar.MONDAY etc") {
      val weekDayMap = mutable.LinkedHashMap.empty[String, Int] +
        ("monday" -> Calendar.MONDAY) +
        ("tuesday" -> Calendar.TUESDAY) +
        ("sunday" -> Calendar.SUNDAY) +
        ("wednesday" -> Calendar.WEDNESDAY) +
        ("thursday" -> Calendar.THURSDAY) +
        ("friday" -> Calendar.FRIDAY) +
        ("saturday" -> Calendar.SATURDAY)

      weekDayMap.head._1 should be ("monday")
      weekDayMap.last._1 should be ("saturday")
  }

  test("q7: Print table of java properties formatted") {
    val propertyMap = Map(
      "java.runtime.name" -> System.getProperty("java.runtime.name"),
      "sun.boot.library.path" -> System.getProperty("sun.boot.library.path"))

    val longest = propertyMap.keys.max.length
    val formattedMap = propertyMap.map{
      case (k, v) => k + " "*(longest - k.length + 10) + "| " + v
    }

    for (property <- formattedMap) println(property)
    /* Result:
     * java.runtime.name              | Java(TM) SE Runtime Environment
     * sun.boot.library.path          | C:\Program Files\Java\jdk1.8.0_65\jre\bin
     */
   formattedMap.head.indexOf("|") should equal (formattedMap.last.indexOf("|"))
   }

  test("q8: write function minmax that returns pair of min & max values") {
    def minmax(values: Array[Int]) = {
      (values.min, values.max)
    }
    minmax(Array(3, 4, 6, 5)) should be ((3, 6))
  }

  test("q9: write a function lteqgt(arr, v) that returns a triple containing count below v, equal v, above v") {
    def lteqgt(values: Array[Int], v: Int) = {
      (values.count(_ < v), values.count(_ == v), values.count(_ > v))
    }

    lteqgt(Array(0,0,0,3,5,5), 3) should be ((3,1,2))
  }

  test("q10. What happens when you zip 'Hello' and 'World'?  Is there a plausible use case?") {
    "Hello" zip "World" should be (Vector(('H','W'), ('e','o'),('l','r'),('l','l'),('o','d')))

    //get upper case and lower case pairs
    val lower = "abcdefghijklmnopqrstuvwxyz"
    lower zip lower.toUpperCase

    //compare differences in 2 strings
    "Hello World!" zip "hello World." filter{case (x,y) => x != y} should be (Vector(('H','h'),('!','.')))


  }

}
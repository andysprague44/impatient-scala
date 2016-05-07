package forthe.impatient.chapter3

import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest.prop.Checkers
import org.scalatest.{FunSuite, Matchers}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Chapter 3 exercies
  */
class Arrays extends FunSuite with Matchers with Checkers {

  test("q1: create an array of n random integers between 0 (inclusive) and n(exclusive") {
    def randomArray(n: Int) = {
       val rand = new Random(n)
      (for (i <- 1 to n) yield rand.nextInt(n)).toArray
    }

    //we don't want to take any old int or we'll end up with an
    //array of 2147483647 elements
    val n = Gen.choose(0, 100)
    forAll(n) { n =>
      val arr = randomArray(n)
      n == 0 && arr.length == 0 ||
      (
        arr.length == n && //contains n records
        arr.min >= 0 &&    //inclusive of 0
        arr.max < n        //exclusive of n
      )
    }.check
  }

  test("q2: write a loop that swaps adjacent elements") {
    //Loops are boring, here's a tail rec function instead
    @annotation.tailrec
    def swapAdjacent(arr: Array[Int], res: Array[Int] = Array()): Array[Int] = {
      //At end of string if there are only 0 or 1 elements left
      if (arr.length <= 1) res ++ arr
      else {
        //take off the first 2 elements, swap, add to the result, and run for the tail
        swapAdjacent(arr drop 2, res ++ (arr take 2).reverse)
      }
    }

    swapAdjacent(Array(1,2,3,4,5)) should equal (Array(2,1,4,3,5))
  }

  test("q3: same but produce new array and use for/yield") {
    def swapAdjacent(arr: Array[Int]): (Array[Int], Array[Int]) = {
      val elements = for {
        i <- arr.indices
        if i % 2 == 0
      } yield if (arr.isDefinedAt(i+1)) Array(arr(i+1), arr(i)) else Array(arr(i))
      (arr, elements.toArray.flatten[Int])
    }

    //Checking we also have the original array by returning it from the method
    val (originalArr, newArr) = swapAdjacent(Array(1,2,3,4,5))
    originalArr should equal (Array(1,2,3,4,5))
    newArr should equal (Array(2,1,4,3,5))
  }

  test("q4: move all -ve values in an array to the end (in same order)") {
    def shuffleNegatives(arr: Array[Int]) = {
      arr.filter(_ >= 0) ++ arr.filter(_ < 0)
    }

    shuffleNegatives(Array(0, -4, 3, 2, 1, -2, 6)) should equal (Array(0, 3, 2, 1, 6, -4, -2))
  }

  test("q5: compute average of Array[Double]") {
    def average(arr: Array[Double]) = arr.sum / arr.length
    average(Array(1.0, 2.0, 3.0, 4.0, 5.0)) should be (3.0)
  }

  test("q6: rearrange Array so they are in reverse sorted order?  Same with ArrayBuffer?") {
    def rearrange(arr: Array[Int]) = arr.sorted.reverse
    rearrange(Array(3, 2, 5, 4, 6)) should be (Array(6, 5, 4, 3, 2))

    def rearrangeBuffer(b: ArrayBuffer[Int]) = b.sorted.reverse
    rearrangeBuffer(ArrayBuffer(3, 2, 5, 4, 6)) should be (ArrayBuffer(6, 5, 4, 3, 2))
  }

  test("q7: remove duplicates from an array") {
    def unique(arr: Array[Int]) = arr.toSet.toArray
    unique(Array(1, 1, 1, 1, 2)) should be (Array(1, 2))
  }

  test("q8: what is the most efficient way to remove all negative numbers except the first one in an array?") {
    def firstExample(arr: Array[Int]) = {
      val a = arr.toBuffer
      var first = true
      var n = a.length
      var i = 0
      while (i < n) {
        if (a(i) >= 0) i += 1
        else {
          if (first) { first = false; i += 1}
          else { a.remove(i); n -=1}
        }
      }
      a.toArray
    }

    def secondExample(arr: Array[Int]) = {
      val a = arr.toBuffer
      var first = true
      val indexes = for (i <- 0 until a.length if first || a(i) >=0) yield {
        if (a(i) < 0) first = false; i
      }
      for (j <- 0 until indexes.length) a(j) = a(indexes(j))
      a.trimEnd(a.length - indexes.length)
      a.toArray
    }

    def exerciseSuggestedMethod(arr: Array[Int]) = {
      val a = arr.toBuffer
      val negIndexes = for {
        i <- a.indices
        if a(i) < 0
      } yield i
      val toRemove = negIndexes.reverse.dropRight(1)
      for (i <- toRemove) a.remove(i)
      a.toArray
    }

    def myBetterFunctionalApproach(arr: Array[Int]) = {
      val start = arr.takeWhile(_ > 0)
      val rest = arr.dropWhile(_ > 0)
      start ++ rest.take(1) ++ rest.tail.filter(_ > 0)
    }

    //First check the functions work
    val testArr = Array(1, 2, -5, 3,  -1, -1, -1, 4, -1)
    firstExample(testArr) should be (Array(1,2,-5,3,4))
    secondExample(testArr) should be (Array(1,2,-5,3,4))
    exerciseSuggestedMethod(testArr) should be (Array(1,2,-5,3,4))
    myBetterFunctionalApproach(testArr) should be (Array(1,2,-5,3,4))

    //Lets create a large array and use that to check the timings (100,000 elements)
    val largeTestArray =
      Array.fill(10000)(1) ++
      Array.fill(10000)(-1) ++
      Array.fill(10000)(1) ++
      Array.fill(60000)(-10) ++
      Array.fill(10000)(1)

    /* Timing function */
    def timeMe[A](f: => A): Long = {val s = System.nanoTime(); f; System.nanoTime() - s}

    // The first 3 examples decide whether to be quicker or slower each time
    //Running this on a pretty old laptop, so it's probably that with more of an impact
    //After 10 or so runs the functional way seems to normally be the quickest but can't be sure
    //I don't know enough about O(n) time complexities to describe what should actually be the quickest and why
    timeMe(myBetterFunctionalApproach(largeTestArray)) should be < timeMe(firstExample(largeTestArray)) //winner winner chicken dinner
    timeMe(myBetterFunctionalApproach(largeTestArray)) should be < timeMe(secondExample(largeTestArray)) //winner winner chicken dinner
    timeMe(myBetterFunctionalApproach(largeTestArray)) should be < timeMe(exerciseSuggestedMethod(largeTestArray)) //winner winner chicken dinner
  }

  test("q9: make a sorted collection of USA timezones (from java.util.TimeZone.getAvailableIds)") {
    def sortedUSTimeZones = {
      java.util.TimeZone.getAvailableIDs
        .filter(_.matches("America.*"))
        .map(_.drop(8))
        .sorted
    }
    sortedUSTimeZones should equal(
      Array("Adak", "Anchorage", "Anguilla", "Antigua", "Araguaina",
        "Argentina/Buenos_Aires", "Argentina/Catamarca", "Argentina/ComodRivadavia",
        "Argentina/Cordoba", "Argentina/Jujuy", "Argentina/La_Rioja", "Argentina/Mendoza",
        "Argentina/Rio_Gallegos", "Argentina/Salta", "Argentina/San_Juan", "Argentina/San_Luis",
        "Argentina/Tucuman", "Argentina/Ushuaia", "Aruba", "Asuncion", "Atikokan", "Atka",
        "Bahia", "Bahia_Banderas", "Barbados", "Belem", "Belize", "Blanc-Sablon", "Boa_Vista",
        "Bogota", "Boise", "Buenos_Aires", "Cambridge_Bay", "Campo_Grande", "Cancun", "Caracas",
        "Catamarca", "Cayenne", "Cayman", "Chicago", "Chihuahua", "Coral_Harbour", "Cordoba",
        "Costa_Rica", "Creston", "Cuiaba", "Curacao", "Danmarkshavn", "Dawson", "Dawson_Creek",
        "Denver", "Detroit", "Dominica", "Edmonton", "Eirunepe", "El_Salvador", "Ensenada",
        "Fort_Wayne", "Fortaleza", "Glace_Bay", "Godthab", "Goose_Bay", "Grand_Turk", "Grenada",
        "Guadeloupe", "Guatemala", "Guayaquil", "Guyana", "Halifax", "Havana", "Hermosillo",
        "Indiana/Indianapolis", "Indiana/Knox", "Indiana/Marengo", "Indiana/Petersburg",
        "Indiana/Tell_City", "Indiana/Vevay", "Indiana/Vincennes", "Indiana/Winamac", "Indianapolis",
        "Inuvik", "Iqaluit", "Jamaica", "Jujuy", "Juneau", "Kentucky/Louisville",
        "Kentucky/Monticello", "Knox_IN", "Kralendijk", "La_Paz", "Lima", "Los_Angeles",
        "Louisville", "Lower_Princes", "Maceio", "Managua", "Manaus", "Marigot", "Martinique",
        "Matamoros", "Mazatlan", "Mendoza", "Menominee", "Merida", "Metlakatla", "Mexico_City",
        "Miquelon", "Moncton", "Monterrey", "Montevideo", "Montreal", "Montserrat", "Nassau",
        "New_York", "Nipigon", "Nome", "Noronha", "North_Dakota/Beulah", "North_Dakota/Center",
        "North_Dakota/New_Salem", "Ojinaga", "Panama", "Pangnirtung", "Paramaribo",
        "Phoenix", "Port-au-Prince", "Port_of_Spain", "Porto_Acre", "Porto_Velho",
        "Puerto_Rico", "Rainy_River", "Rankin_Inlet", "Recife", "Regina", "Resolute",
        "Rio_Branco", "Rosario", "Santa_Isabel", "Santarem", "Santiago", "Santo_Domingo",
        "Sao_Paulo", "Scoresbysund", "Shiprock", "Sitka", "St_Barthelemy", "St_Johns",
        "St_Kitts", "St_Lucia", "St_Thomas", "St_Vincent", "Swift_Current", "Tegucigalpa",
        "Thule", "Thunder_Bay", "Tijuana", "Toronto", "Tortola", "Vancouver", "Virgin",
        "Whitehorse", "Winnipeg", "Yakutat", "Yellowknife")
    )
  }
  test("q10: make a SystemFlavourMap") {
    import java.awt.datatransfer._

    import scala.collection.JavaConverters._
    val flavours = SystemFlavorMap.getDefaultFlavorMap.asInstanceOf[SystemFlavorMap]
    val natives = flavours.getNativesForFlavor(DataFlavor.imageFlavor).asScala
    natives should be (List("PNG", "JFIF", "DIB", "ENHMETAFILE", "METAFILEPICT").toBuffer)
    //Used to transform between operating systems and platforms I think,
    //the question is just trying to give a reason for java -> scala converters
  }

}
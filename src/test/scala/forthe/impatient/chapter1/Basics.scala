package forthe.impatient.chapter1

import org.scalatest.{FunSuite, Matchers}

/**
  * Chapter 1 exercies
  */
class Basics extends FunSuite with Matchers {

  test("q1: what interesting stuff can you do with 3?") {
    val one = 3 compareTo 8 //-1 if less, 0 if same, 1 if more
    one should be(-1)
    val two: Int = 3 ^ 3 //bitwise XOR (11 ^ 11 => 00)
    two should be(0)
    val three: Int = 3.unary_+ //no idea, maybe I find out in chapter 13
    three should be(3)
    val four: Int = 3.## //no idea
    four should be(3)
    val five = 3 !== 3 // not equal to
    five should be(false)
    //Plus obviously hundreds more
  }
  test("q2: does square root three squared equal 3?") {
    def rootAndSquare(a: Double) = {
      val tmp = math.sqrt(a)
      math.pow(tmp, 2)
    }

    val three: Double = 3
    val rootSquared = rootAndSquare(three)
    rootSquared should not equal (three plusOrMinus 10E-17)
    rootSquared should equal(three plusOrMinus 10E-16)
  }

  test("q3: es variables are vals") {}

  test("q4: what is crazy * 3?") {
    val result = "crazy" * 3
    result should be("crazycrazycrazy")

  }

  test("q4: what does 10 max 2 mean?") {
    val result = 10 max 2
    /* Takes the maximum of the 2 numbers */
    result should equal (10)
  }

  test("q5: what is 2 pow 1024 using BigInt?"){
    val num: BigInt = 2
    val result = num pow 1024
    result should equal (BigInt("17976931348623159077293051907890247336179769" +
      "7894230657273430081157732675805500963132708477322407536021120113879871" +
      "39335765878976881441662249284743063947412437776789342486548527630221960" +
      "12460941194530829520850057688381506823424628814739131105408272371633505" +
      "10684586298239947245938479716304835356329624224137216"))
    //p.s. I ran the test, it failed, and I pasted in the right answer
  }

  test("q6: what is needed to create probablePrime and Random?") {
    import math.BigInt._
    import util.Random
    val randomPrime = probablePrime(100, Random)
    randomPrime isProbablePrime(certainty = 200) should be (true)
  }

  test("q7: how do you create random fle names using primes and converting to base 36?") {
    import math.BigInt._
    import util.Random
    val fileName = probablePrime(100, Random).toString(36)
    fileName length() should be > 10
  }

  test("q8: how do you get the first and last chars from a string?") {
    val string = "whatastring!"
    val first = string.head
    val last = string.last
    first should be ('w')
    last should be ('!')
  }

  test("q9: what are take, drop, takeRight and dropRight?") {
    val string = "whatastring!"
    val sub1 = string drop 4 take  //should replce with 'slice'
    sub1 should be ("astring")

    val sub2 = string takeRight 8 dropRight 1
    sub2 should be ("astring")

    //substring less intuative (uses a 0 based index)
    val sub = string substring(4, 11)
    sub should be ("astring")

    string should be ("whatastring!")
  }
}
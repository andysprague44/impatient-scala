package impatient.chapter2

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalatest.prop.Checkers
import org.scalatest.{FunSuite, Matchers}


/**
  * Chapter 2 Exercises
  */
class Functions extends FunSuite with Matchers with Checkers {

 test("q1: function that calculates signum of a number?") {
   def signum(i: Double) = i compareTo 0

   signum(-5) should be (-1)
   signum(0) should be (0)
   signum(10E-15) should be (1)
 }

  test("q2: what is value and type of {}?") {
    val `{}` = {}
    //value
    `{}`.getClass.getName should be ("void")

    //type
    `{}` match {
      case x: Unit => x //pass
    }

  }

  test("q3: when is x = y= 1 valid?") {
    var y = 0
    def x = y = 1
    x //x is a function with side effects (so should use x() really)
    y should be (1)
  }

  test("q4: Write a scala equivalent for java's 'for(int i = 10; i >= 0; i--) System.out.println(i)'") {
    def scalaEquiv = {
      (for (i <- 0 to 10) yield i).reverse.toList.map(i => {println(i);i})
    }
    scalaEquiv should equal (List(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0))
  }

  test("q5: write a countdown from n to 0") {
    def countdown(n: Int) = {
      (for (i <- 0 to n) yield i).reverse.toList
    }
    countdown(3) should equal (List(3, 2, 1, 0))
  }

  test("q6: Compute the product of all unicode characters in a string with a for loop") {
      val resLoop = {for(c: Char <- "Hello") yield c.toLong}.product
      resLoop should equal (9415087488L)
  }

  test("q7: Compute the product of all unicode characters in a string") {
    val resMap = "Hello".toCharArray.map(_.toLong).product
    resMap should be (9415087488L)

    val resFold = "Hello".foldLeft(1L)(_ * _.toInt)
    resFold should be (9415087488L)
  }

  test("q8: Write a function to do the same") {
    def unicodeProductFold(in: String) = in.foldLeft(1L)(_ * _.toLong)

    unicodeProductFold("Hello") should be (9415087488L)
  }

  test("q9: Make previous function recursive") {
    @annotation.tailrec //this is a cool annotation, use it!
    def unicodeProductThatIsInNoWayOverkill(in: String, product: Long = 1L): Long = {
        if (in == "") {
          product
        } else {
          unicodeProductThatIsInNoWayOverkill(in.tail.toString, product * in.head.toLong)
        }
    }
    unicodeProductThatIsInNoWayOverkill("Hello") should be (9415087488L)
  }

  test("q10: write a function that computes x^n") {
    /* Use definitions:
     *  x^n = y.y if n is even and +ve, where y = x^(n/2)
     *  x^n = x.x^(n-1) if n is odd and +ve
     *  x^0 = 1
     *  x^n = 1 / x^(-n) if n is -ve
     *
     *  No return statements allowed (ever)
     */

    def computePowers(x: Double, n: Int): Double = {
      //Seems like an awesome place to use the power of pattern matching
      //Could use standard for else blocks but that's boring (and not the Scala way)
      n match {
        case _ if n % 2 == 0 && n > 0 =>
          val y = computePowers(x, n / 2)
          y * y
        case _ if n > 0 =>
          x * computePowers(x, n - 1)
        case _ if n == 0 =>
          1.0
        case _ => //if we get here it must be -ve
          1.0 / computePowers(x, -n)
      }
    }

    //Hey let's use ScalaCheck too!  So we are sure it works for all doubles and all int
    //Well, except -2147483648, as int's are not-symmetric, and +2147483648 is out of range
    check((x: Double, n: Int) => {
      if (n == -2147483648) true //ignore
      else {
        val result = computePowers(x, n)
        result == Math.pow(x, n) //can just check against a library function
      }
    })

    //Side-note: So much cooler than Java!
  }




}
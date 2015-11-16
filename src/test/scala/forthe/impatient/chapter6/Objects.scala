package forthe.impatient.chapter6

import java.awt.Point

import org.scalatest.prop.Checkers
import org.scalatest.{FunSuite, Matchers}

/**
  * Chapter 6 exercies
  */
class Objects extends FunSuite with Matchers with Checkers {

  test("q1: write a Conversions object with inches to cm, gallons to liters, miles to mk") {
    object Conversions {
      def inchesToCentimeters(inches: Double) = inches * 2.54

      def gallonsToLitres(gallons: Double, country: String = "US") = {
        if (country.toUpperCase == "UK") gallons * 4.54609 else gallons * 3.78541
      }

      def milesToKilometers(miles: Double) = miles * 1.6
    }

    Conversions.inchesToCentimeters(2) should be (5.08)
    Conversions.gallonsToLitres(2) should be (7.57082)
    Conversions.gallonsToLitres(2, "UK") should be (9.09218)
    Conversions.milesToKilometers(2) should be (3.2)

  }

  test("q2: Provide super class UnitConversion for previous q") {
    trait UnitConversion {
      def apply(in : Double): Double
    }
    object InchesToCentimeters extends UnitConversion {
      def apply(inches: Double) = inches * 2.54
    }
    object GallonsToLitres extends UnitConversion {
      def apply(gallons: Double) = gallons * 3.78541

      def apply(gallons: Double, country: String): Double = {
        if (country.toUpperCase == "UK") gallons * 4.54609 else apply(gallons)
      }
    }
    object MilesToKilometers extends UnitConversion {
      def apply(miles: Double) = miles * 1.6
    }

    InchesToCentimeters(2) should be (5.08)
    GallonsToLitres(2) should be (7.57082)
    GallonsToLitres(2, "UK") should be (9.09218)
    MilesToKilometers(2) should be (3.2)

  }

  test("q3: Define an origin that extends java.awt.Point") {
    object Origin extends java.awt.Point {
    }

    val origin = Origin
    origin should equal (new Point(0, 0))

    //Why is this not a good idea?  Have to override all the methods that can 'move' it
    origin.move(2, 2)
    origin should equal (new Point(2, 2))
  }

  test("q4: define a Point class with companion object") {
    object Point {
      def apply(x: Int = 0, y: Int = 0) = new Point(x, y)
    }

    val point = Point(3, 1)
    point should equal (new Point(3, 1))
  }


  test("q5: write a scala app that prints the cmd line arguments in reverse order") {
    //object TestApp created in src/main at same directory location
    //To run type the following command at the root directory
    //sbt "run-main forthe.impatient.chapter6.TestApp Hello World"

    //If different arguments are specified it will return error code 1 (success only if World Hello is the result)
  }

  test("q6: Write an enumeration for playing cards with a toString method") {
    object PlayingCard extends Enumeration {
      val Heart, Club, Diamond, Spade = Value
    }

    PlayingCard.Heart.toString should be ("Heart")
  }

  test("q7: implement a function that checks if playing card is red") {
    object PlayingCard extends Enumeration {
      type PlayingCard = Value
      val Heart, Club, Diamond, Spade = Value
    }

    import PlayingCard._
    def isRed(card: PlayingCard) = card == Heart || card == Diamond

    isRed(Heart) should be (true)
  }

  test("q8: Write an enumeration for the 8 corners of an RGB colour cube, with IDs of the colour value") {
    object RBGColourCube extends Enumeration {
      val Black = Value(0x000000)
      val Red = Value(0xFF0000)
      val Lime = Value(0x00FF00)
      val Blue = Value(0x0000FF)
      val Yellow = Value(Red.id + Lime.id)
      val Cyan = Value(Lime.id + Blue.id)
      val Magenta = Value(Red.id + Blue.id)
      val White = Value(Red.id + Lime.id + Blue.id)
    }
    import RBGColourCube._

    White.id should be (0xFFFFFF)
    White.toString should be ("White")
  }

}
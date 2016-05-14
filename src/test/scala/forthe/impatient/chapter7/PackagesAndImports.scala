package forthe.impatient.chapter7 {

  import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
  import org.scalatest.{FunSuite, Matchers}

  import scala.util.{Success, Try}

  /**
    * Chapter 7 exercises
    */
  class PackagesAndImports extends FunSuite
    with Matchers
    with Checkers
    with GeneratorDrivenPropertyChecks {

    test("q1: show that com.horstman.impatient is not the same as" +
      " package com { package horstman { package impatient ") {
      import question1.com.horstman.impatient._
      Try {
        new Impatient1NeedsImport
        new Impatient2DoesNotNeedImport
      }.isSuccess
    }

    test("q2: write a puzzler using com not at top level") {
      import com.question2._
      Convert(2) should be("2")
      com.question2.Convert(2) should be("Are you baffled?")
    }

    test("q3: Write a package 'random' with linear congruential random number generator") {
      import question3._

      forAll("seed") { (seed: Int) =>
        setSeed(seed)
        val rand = Try {
          nextInt()
        }
        val rand2 = Try {
          nextDouble()
        }
        rand.isSuccess should be(true)
        rand2.isSuccess should be(true)
        rand.get should be <= math.pow(2, 32).toInt
        rand2.get should be <= math.pow(2, 32)
      }

    }

    test("q4: Why design the package object?") {
      // Gives a single place to put such methods so that it is clear for other developers
      // which becomes important when using implicit conversions (removes implicit 'magic')
    }

    test("q5: What is the meaning of private[com] def giveRaise(rate: Double)?  Is it useful?") {
      // Means the method is visible up to the package com.
      // This is a high level package name so unlikely to be useful, see example
      import question5._
      val readOnlyWage = com.manager.payroll.Employee.readOnlyWage
      com.employee.technology.SlyITPerson.giveMyselfARaise(20000.00) //From SlyItPerson shouldn't be able to access 'giveRaise' method
      com.manager.payroll.Employee.readOnlyWage should be(readOnlyWage + 20000.00) //But we can!
    }

    test("q6: Write a program to copy all elements for java hash map to scala hash map") {
      import java.util.{HashMap => JavaHashMap}

      import scala.collection.immutable.{HashMap => ScalaHashMap}

      val javaHash = new JavaHashMap[Int, String]
      javaHash.put(1, "one")
      javaHash.put(2, "two")
      javaHash.put(3, "three")

      import scala.collection.JavaConverters._
      val scalaHash: ScalaHashMap[Int, String] = ScalaHashMap(javaHash.asScala.toSeq: _*)
      scalaHash.getOrElse(1, fail()) should be("one")

      //If we just coded to the Map interface this would be even easier
      import scala.collection.JavaConverters._
      import scala.collection.{Map => ScalaMap}
      val scalaMap: ScalaMap[Int, String] = javaHash.asScala
      scalaMap.getOrElse(1, fail()) should be("one")

    }

    test("q7: q6 with innermost scope of imports possible") {
      //import java.lang._
      //import scala._
      //import scala.Predef._
      //rest already inner
    }

    test("q8: What is the effect of import java._ and import javax._?  Is this a good idea?") {
      //You are importing a hell'a lot a java classes, why would you want to do that?
    }

    test("q9: Write a program that imports the java.lang.System class, reads the username, reads a password, " +
      "and prints an error if password isn't 'secret'.  No other imports, no qualified names ") {

      object SystemPasswordCheck {

        def testPassword(): Unit = {
          val username = System.getProperty("user.name")
          Try { //I cheated and imported Try, sue me.
            System.console().readLine("password:")
          } match {
            case Success(pw) if pw == "secret" => System.out.print(s"Hello $username!")
            case _ => System.err.print(s"The password isn't secret!")
          }
        }
      }

      import java.io.{ByteArrayOutputStream, PrintStream}
      val outContent: ByteArrayOutputStream = new ByteArrayOutputStream()
      val errContent: ByteArrayOutputStream = new ByteArrayOutputStream()
      System.setOut(new PrintStream(outContent))
      System.setErr(new PrintStream(errContent))
      System.setProperty("user.name", "Iggy Pop")

      SystemPasswordCheck.testPassword()

      outContent.toString should be("")
      val res = errContent.toString
      res should be("The password isn't secret!")
    }

    test("q10: Aside from StringBuilder, what else does scala override?") {
      //      java.lang.Boolean;        scala.Boolean
      //      java.lang.Byte;           scala.Byte
      //      java.lang.System.console; scala.Console
      //      java.lang.Double;         scala.Double
      //      java.lang.Float;          scala.Float
      //      java.lang.Iterable;       scala.Iterable
      //      java.lang.Long;           scala.Long
      //      java.lang.Short;          scala.Short
      //      java.lang.StringBuilder;  scala.StringBuilder
    }
  }

}


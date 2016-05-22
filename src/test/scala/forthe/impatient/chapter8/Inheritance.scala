package forthe.impatient.chapter8

import org.scalatest.{FunSuite, Matchers}

/**
  * Chapter 8 exercises
  */
class Inheritance extends FunSuite
  with Matchers {

  test("q1: extend BankAccount to a Checking account") {
    val checkingAccount = new CheckingAccount(10.00)
    checkingAccount.deposit(1)
    checkingAccount.withdraw(1)
    checkingAccount.currentBalance should be(8.0)
  }

  test("q2: extend BankAccount to a saving account") {
    val savingAccount = new SavingAccount(3.00)
    (1 to 4).foreach { i => savingAccount.deposit(2) }
    savingAccount.currentBalance should be(10.0) //$8 in plus $1 fee

    savingAccount.earnMonthlyInterest(0.1)
    savingAccount.currentBalance should be(11.0)

    (1 to 4).foreach { i => savingAccount.withdraw(1) }
    savingAccount.currentBalance should be(6.0) //$4 out plus $1 fee
  }

  ignore("q3: implement favourite java/c++ toy inheritance hierarchy in scala") {
    //TODO On hold while travelling
  }

  test("q4: define abstract Item, SimpleItem and a Bundle that contains Items") {
    val (joy1, joy2, joy3) = (new SimpleItem(1, "joy"), new SimpleItem(2, "joy"), new SimpleItem(3, "joy"))
    val aBundleOfJoy: Bundle = new Bundle(joy1, joy2)
    aBundleOfJoy.addToBundle(joy3).addToBundle(joy3).removeFromBundle(joy2)
    aBundleOfJoy.description should be("A Bundle containing: joy,joy,joy")
    aBundleOfJoy.price should be(1 + 3 + 3)
  }

  test("q5: Design Point and LabelledPoint") {
    new LabelledPoint("Black Thursday", 1929, 230.07).label should be("Black Thursday")
  }

  test("q6: Define abstract Shape with centrePoint method, and concrete Rectangle and Circle") {
    new Circle(2, 2, 4).area should be(8 * Math.PI)
    new Rectangle(0, 0, 1, 1).centrePoint should be(new Point(0.5, 0.5))
  }

  test("q7: Create Square that extends Rectangle with 3 constructors") {
    //The use case can be met with one constructor with default values
    val square = new Square(3, new java.awt.Point(1, 2))
    val defaultSquare = new Square()
    val squareFromWidth = new Square(5)
    //We only need to define a new constructor to create a square from a point
    val squareFromCorner = new Square(new java.awt.Point(3, 4))

    (square.x, square.y, square.width, square.height) should be((1, 2, 3, 3))
    (defaultSquare.x, defaultSquare.y, defaultSquare.height, defaultSquare.width) should be((0, 0, 0, 0))
    (squareFromWidth.x, squareFromWidth.y, squareFromWidth.height, squareFromWidth.width) should be((0, 0, 5, 5))
    (squareFromCorner.x, squareFromCorner.y, squareFromCorner.width, squareFromCorner.height) should be((3, 4, 0, 0))
  }

  test("q8: Analyze Person and SecretAgent classes") {
    //    $ javap -p Person.class
    //    Compiled from "Person.scala"
    //    public class forthe.impatient.chapter8.Person {
    //      private final java.lang.String name;
    //      public java.lang.String name();
    //      public java.lang.String toString();
    //      public forthe.impatient.chapter8.Person(java.lang.String);
    //    }
    //
    //    $ javap -p SecretAgent.class
    //    Compiled from "Person.scala"
    //    public class forthe.impatient.chapter8.SecretAgent extends forthe.impatient.chapter8.Person {
    //      private final java.lang.String name;
    //      public java.lang.String name();
    //      public java.lang.String toString();
    //      public forthe.impatient.chapter8.SecretAgent(java.lang.String);
    //    }
    //    With '-c' option gives a lot of rubbish I can't be bothered to interpret.
    //    I'm going to write this off as a bad question.
  }

  test("q9:  Replace val range with def in Creature class, what happens?") {

    val creature = new Creature //range = 10
    val antWithValExtendingVal = new Ant //range = 2
    antWithValExtendingVal.env.length should be(0) //Not 2 or 10, early definition problem!

    val antWithDefExtendingDef = new AntWithDef
    antWithDefExtendingDef.env.length should be(2) //When we call a def it always runs it, whether initialised or not? Is that right?

    val antWithValExtendingDef = new AntWithVal
    antWithValExtendingDef.env.length should be(0) //Setting as val reverts to the same issue
  }

  test("q10: Explain meaning of protected keyword") {
    //    The first 'protected' makes the primary constructor visible for extending classes only
    //    The second 'protected' makes the field 'elems' visible at package level only

    // *** Using Stack directly ***:
    import scala.collection.immutable._

    // Doesn't compile:
    //val stack = new collection.immutable.Stack[String](List("2"))
    // INSTEAD can use default constructor and provided method:
    val stackDefault = new Stack[String]().push("ying").push("yang")

    //Doesn't compile:
    //val elems = stackDefault.elems
    //INSTEAD have to use provided methods e.g. 'head', 'tail'
    val first = stackDefault.head
    val second = stackDefault.tail.head
    first :: second :: Nil should contain only("ying", "yang")

    // *** Using 'SubStack' that extends Stack ***:
    class SubStack[String](val subElems: List[String]) extends Stack[String](subElems /*sub class can access super constructor*/) {
      val superElems = "yang" :: elems /*sub class can access super field*/
    }

    val subStack = new SubStack[String](List("ying"))
    val subElems = subStack.superElems
    subElems should contain only("ying", "yang")
  }
}


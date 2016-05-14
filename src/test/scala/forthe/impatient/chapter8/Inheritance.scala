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

  test("q3: implement favourite java/c++ toy inheritance hierarchy in scala") {
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
}


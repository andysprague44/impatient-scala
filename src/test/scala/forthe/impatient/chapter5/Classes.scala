package forthe.impatient.chapter5

import org.scalatest.prop.Checkers
import org.scalatest.{FunSuite, Matchers}

import scala.beans.BeanProperty

/**
  * Chapter 5 exercies
  */
class Classes extends FunSuite with Matchers with Checkers {

  test("q1: improve the counter in sec 5.1 so that it doesn't turn negative") {
    object Counter {
      //companion object
      def apply(start: Int = 0) = new Counter(start)
    }
    class Counter(private var value: Int) {
      def increment() {
        //If we get to the max value just start from 0 again
        value = if (value == Int.MaxValue) 0 else value + 1
      }

      def current = value
    }

    val counter = Counter()
    counter.current should be(0)
    counter increment()
    counter.current should be(1)

    val largeCounter = Counter(Int.MaxValue)
    largeCounter increment()
    largeCounter.current should be(0)
  }

  test("q2: write a bank account class with deposit, withdraw, and read-only balance") {
    object BankAccount {
      def apply(initial: BigDecimal = BigDecimal("0.0")) = new BankAccount(initial)
    }
    class BankAccount(private var amount: BigDecimal) {
      def deposit(in: BigDecimal) = amount += in

      def withdraw(out: BigDecimal) = amount -= out

      def balance = amount
    }

    val myAccount = BankAccount(5.00)
    myAccount.deposit(2500.00)
    myAccount.withdraw(5.00)
    myAccount.balance should be(BigDecimal("2500.00"))
  }

  test("q3: write a class Time with before method") {
    object Time {
      def apply(hrs: Int, min: Int) = new Time(hrs, min)
    }
    class Time(val hrs: Int, val min: Int) {
      def before(other: Time): Boolean = {
        60 * this.hrs + this.min < 60 * other.hrs + other.min
      }
    }

    val myTime = Time(12, 0)
    val otherTime = Time(12, 1)
    myTime.before(otherTime) should be(true)
  }

  test("q4: reimplement with internal storage of time in mins since midnight") {
    object Time {
      def apply(hrs: Int, min: Int) = new Time(60 * hrs + min)
    }
    class Time(val time: Int) {
      def before(other: Time): Boolean = this.time < other.time
    }

    val myTime = Time(12, 0)
    val otherTime = Time(12, 1)
    myTime.before(otherTime) should be(true)
  }

  test("q5: make a class Student with JavaBean properties. What methods are generated? Use getters/setters?") {
    object Student {
      def apply(name: String = "", id: Long = 0L) = new Student(name, id)
    }
    class Student(@BeanProperty var name: String, @BeanProperty var id: Long) {}

    val student = Student("Andy", 1989L)
    //Java style accessors
    student.setName("Still Andy")
    student.getName() should be("Still Andy")

    //Scala style accessors (in the same class)
    student.name_=("Actually, it's Bob")
    student.name should be("Actually, it's Bob")
  }

  test("q6: Create a Person where primary constructor turns -ve ages to zero") {
    object Person {
      def apply(age: Int) = new Person(if (age < 0) 0 else age)
    }
    class Person(var age: Int) {
      def isNow(newAge: Int) = if (newAge > age) age = newAge
    }

    val bob = Person(-21)
    bob.age should be(0)
    bob isNow 40
    bob.age should be(40)
    bob isNow 17
    bob.age should be(40)
  }

  test("q7: create a person with constructor of a string of 'first last')") {
    object Person {
      def apply(fullName: String) = {
        //A good place to do grouped pattern matching - getting the bits inside (here)
        val Regex =
          """^(.*)[ ](.*)$""".r
        fullName match {
          case Regex(first, last) => new Person(first, last)
          case _ => new Person("Joe", "Bloggs")
        }
      }
    }
    class Person(val firstName: String, val lastName: String)

    val samBurgess = Person("Sam Burgess")
    samBurgess.firstName should be("Sam")
    samBurgess.lastName should be("Burgess")

    val dr = Person("Who")
    dr.firstName should be("Joe")
    dr.lastName should be("Bloggs")

  }

  test("q8: Make a car") {
    object Car {
      def apply(manufacturer: String,
                model: String,
                year: Int = -1,
                license: String = "")
        = new Car(manufacturer, model, year, license)
    }
    class Car(val manufacturer: String,
              val model: String,
              val year: Int,
              var license: String)
    //ps I'm not supplying 4 constructers as th question asked because that's
    // just not neccessary when using companion objects

    val fordTransit = Car("ford", "transit")

    fordTransit.license should be ('empty)
    fordTransit.year should be (-1)
    fordTransit.license = "AS11 5HN"
    fordTransit.license should be ("AS11 5HN")

  }

  test("q9: reimplement in Java"){
      val javaRobinReliant = new JavaCar("Robin", "Reliant")

    javaRobinReliant.getLicense should be ('empty)
    javaRobinReliant.getYear should be (-1)
    javaRobinReliant.setLicense("AS11 5HN")
    javaRobinReliant.getLicense should be ("AS11 5HN")

    //Number of lines of scala, including Companion Object, and style choices:
          // 11
    //Number of lines of scala, including Companion Object, args in one line:
          // 5

    //Number of lines of equivalent Java Class:
          // 36
  }

   test("q10: Consider the class below, and rewrite using primary constructor.  What do you prefer?") {
     class Employee(val name: String, var salary: Double) {
       def this() {
         this("John Q. Public", 0.0)
       }
     }

     class EmployeeNew(val name: String = "John Q. Public",
                       var salary: Double = 0.0){}


     val poorEmployee = new Employee()
     poorEmployee.name should be("John Q. Public")

     val richEmployee = new EmployeeNew()
     richEmployee.salary = 120000.00
     richEmployee.name should be ("John Q. Public")

     //The second is better, 'def this() { this(...' looks more confusing to me
     //Adding a Companion object just remove the need for the 'new' keyword, which I also like
   }
}
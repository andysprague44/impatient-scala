package forthe.impatient.chapter9

import scala.collection.mutable.ArrayBuffer

/**
  * Serializable Person class
  */
@SerialVersionUID(42L) class Person(val name: String) extends Serializable {
  val `friends friends!`: ArrayBuffer[Person] = ArrayBuffer[Person]()

  def addFriends(friends: Person*): Unit = {
    `friends friends!`.append(friends: _*)
  }

  override def equals(other: Any): Boolean = other match {
    case that: Person =>
      other.isInstanceOf[Person] &&
        `friends friends!` == that.`friends friends!` &&
        name == that.name
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(`friends friends!`, name)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

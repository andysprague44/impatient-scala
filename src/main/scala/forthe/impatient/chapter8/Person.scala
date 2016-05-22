package forthe.impatient.chapter8

/**
  * Created by Andrew on 14/05/2016.
  */
class Person(val name: String) {
  override def toString = getClass.getName + s"[name=$name]"
}

class SecretAgent(codename: String) extends Person(codename) {
  override val name = "secret"

  override def toString: String = "secret"
}

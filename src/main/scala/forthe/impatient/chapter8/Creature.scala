package forthe.impatient.chapter8

/**
  * Created by Andrew on 21/05/2016.
  */
class Creature {
  val range: Int = 10
  val env: Array[Int] = new Array[Int](range)
}

class Ant extends Creature {
  override val range = 2
}

class CreatureWithDef {
  def range: Int = 10

  val env: Array[Int] = new Array[Int](range)
}

class AntWithDef extends CreatureWithDef {
  override def range = 2
}

class AntWithVal extends CreatureWithDef {
  override val range = 2
}
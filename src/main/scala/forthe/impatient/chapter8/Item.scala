package forthe.impatient.chapter8

abstract class Item {
  def price: Double

  def description: String
}

class SimpleItem(val price: Double = 10.0, val description: String = "") extends Item

class Bundle(initialItems: Item*) extends Item {
  private var items = initialItems.toList

  def price = items.map(_.price).sum

  def description: String = s"A Bundle containing: ${items.map(_.description).mkString(",")}"

  def addToBundle(i: Item): Bundle = {
    items = i :: items; this
  }

  def removeFromBundle(i: Item): Bundle = {
    items = items.filterNot(x => x.price == i.price && x.description == i.description); this
  }
}
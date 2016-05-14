package forthe.impatient.chapter8

import java.awt.Dimension

abstract class Shape {
  def centrePoint: Point
}

class Rectangle(val x: Double, val y: Double, val w: Double, val h: Double) extends Shape {
  val centrePoint = new Point(x + 0.5 * w, y + 0.5 * h)
  val area = w * h
}

class Circle(originX: Double, originY: Double, val r: Double) extends Shape {
  val centrePoint = new Point(originX, originY)
  val area = 0.5 * Math.PI * r * r
}

class Square(w: Int = 0, corner: java.awt.Point = new java.awt.Point(0, 0)) extends java.awt.Rectangle {
  x = corner.x
  y = corner.y
  height = w
  width = w

  def this(corner: java.awt.Point) = this(0, corner)

  override def setSize(w: Int, h: Int) = super.setSize(w, w)

  override def setSize(d: Dimension) = super.setSize(d.width, d.width)

  //If you were to do this fully, there is a whole more methods to override to make sure it's always a square
}

class Point(val x: Double, val y: Double) {
  override def equals(other: Any): Boolean = other match {
    case that: Point => x == that.x && y == that.y
    case _ => false
  }

  override def hashCode(): Int = Seq(x, y).map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
}

class LabelledPoint(val label: String, x: Double, y: Double) extends Point(x, y)

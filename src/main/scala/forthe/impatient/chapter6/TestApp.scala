package forthe.impatient.chapter6

/**
  * App to print list of arguments in reverse
  */
object TestApp extends App {
  val result = args.reverse.mkString(" ")
  println(result)
  if (result == "World Hello") sys.exit(0)
  else sys.exit(1)
}

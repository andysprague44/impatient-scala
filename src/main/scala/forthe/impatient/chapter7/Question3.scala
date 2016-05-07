package forthe.impatient.chapter7 {

  package question3{}

  package object question3 {
    private var next: Double = _
    def setSeed(seed: Int): Unit = {next = seed}
    def nextInt(): Int = nextDouble().asInstanceOf[Int]
    def nextDouble(): Double = {next = (next * 1664525L + 1013904223L) % math.pow(2, 32); next}
  }
}


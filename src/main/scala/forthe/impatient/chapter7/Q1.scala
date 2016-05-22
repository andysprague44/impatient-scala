package com.hortman {
class TestClass1
}
package com.horstman.impatient {



class ImpatientClass {
  import com.hortman.TestClass1
  val res = new TestClass1 // Needs import of class in scope of parent package if using chained notation
}

}

package com {
  package horstman {
    class TestClass2
  }
}

package com {
  package horstman {
    package impatient {
      class Impatient2 {
        val res = new TestClass2 //No import needed for nested notation
      }
    }
  }
}
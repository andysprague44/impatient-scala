package forthe.impatient.chapter7.question1 {
  package com.horstman {
    object TestClass
  }
  package com.horstman.impatient {
    class Impatient1NeedsImport {
      //To access 'TestClass' from here need to import it as using chained notation
      import com.horstman.TestClass
      val res = TestClass
    }
  }
  package com.horstman {
    package impatient {
      class Impatient2DoesNotNeedImport {
        //No import needed for nested notation (can access parent package)
        val res = TestClass
      }
    }
  }
}
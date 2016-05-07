package question5 {
  package com.manager.payroll {
    object Employee {
      var readOnlyWage = 50000.00
      private[com] def giveRaise(rate: Double): Unit = {readOnlyWage = readOnlyWage + rate}
      private[manager] def giveRaiseHidden(rate: Double): Unit = {readOnlyWage = readOnlyWage + rate}
    }
  }

  package com.employee.technology {
    object SlyITPerson {
      import com.manager.payroll.Employee
      def giveMyselfARaise(rate: Double): Unit = Employee.giveRaise(rate)
      //def `foiled!` = Try{Employee.giveRaiseHidden(20000.00)}
    }
  }

}
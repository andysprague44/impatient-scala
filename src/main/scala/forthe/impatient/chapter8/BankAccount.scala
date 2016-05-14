package forthe.impatient.chapter8

class BankAccount(initialBalance: Double) {
  private var balance = initialBalance

  def currentBalance = balance

  def deposit(amount: Double): Double = {
    balance += amount; balance
  }

  def withdraw(amount: Double): Double = {
    balance -= amount; balance
  }
}

class CheckingAccount(initialBalance: Double) extends BankAccount(initialBalance) {
  override def deposit(amount: Double) = super.deposit(amount - 1)

  override def withdraw(amount: Double) = super.withdraw(amount + 1)
}

class SavingAccount(initialBalance: Double) extends BankAccount(initialBalance) {
  private var transactionCount: Int = 0

  override def deposit(amount: Double): Double = {
    transactionCount += 1
    if (transactionCount <= 3) super.deposit(amount) else super.deposit(amount - 1)
  }

  override def withdraw(amount: Double): Double = {
    transactionCount += 1
    if (transactionCount <= 3) super.withdraw(amount) else super.withdraw(amount + 1)
  }

  def earnMonthlyInterest(interestRate: Double): Unit = {
    transactionCount = 0
    super.deposit(currentBalance * interestRate)
  }
}

package ru.unn.agile.FinanceCalculator.Model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Collection;
import static org.junit.Assert.assertEquals;

public class DayExpensesTest {

    @RunWith(Parameterized.class)
    public static class AddGetTest {
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return ExpensesType.arrayOfTypes();
        }

        public AddGetTest(final ExpensesType type) {
                myType = type;
            }

            @Test
            public void isConstructorCorrect() {
                DayExpenses input = new DayExpenses();
                Money money = new Money();
                assertEquals(input.get(myType), money);
            }

            @Test
            public void canAddPositiveAmount() {
                DayExpenses input = new DayExpenses();
                BigDecimal value = new BigDecimal("60.99");
                Money money = new Money(value);
                input.add(myType, money);
                assertEquals(input.get(myType), money);
            }

            @Test
            public void canAddMultiplePositiveAmount() {
                DayExpenses input = new DayExpenses();
                Money firstMoney = new Money("77.55");
                Money secondMoney = new Money("6.99");
                Money thirdMoney = new Money("699.7");
                input.add(myType, firstMoney);
                input.add(myType, secondMoney);
                input.add(myType, thirdMoney);
                Money expectedMoney = new Money("784.24");
                assertEquals(input.get(myType), expectedMoney);
            }

            @Test(expected = IllegalArgumentException.class)
            public void canNotAddNegativeAmount() throws IllegalArgumentException {
                DayExpenses input = new DayExpenses();
                Money thirdMoney = new Money("-699.7");
                input.add(myType, thirdMoney);
            }

            private ExpensesType myType;
        }

        @Test
        public void canAddMultipleTypesPositiveAmounts() {
            DayExpenses input = new DayExpenses();
            Money nullMoney = new Money();

            Money unreasonableWasteMoney = new Money("99.7");
            input.add(ExpensesType.UnreasonableWaste, unreasonableWasteMoney);

            Money eatingOutMoney = new Money("77.88");
            input.add(ExpensesType.EatingOut, eatingOutMoney);

            Money productsMoney = new Money("9999.88");
            input.add(ExpensesType.Products, productsMoney);

            DayExpenses actual = new DayExpenses(eatingOutMoney,
                    productsMoney, unreasonableWasteMoney,
                    nullMoney, nullMoney, nullMoney);

            assertEquals(input, actual);
        }
}

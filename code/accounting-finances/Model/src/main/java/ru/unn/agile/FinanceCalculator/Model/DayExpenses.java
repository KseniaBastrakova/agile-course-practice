package ru.unn.agile.FinanceCalculator.Model;

import java.util.HashMap;
import java.util.Map;

public class DayExpenses {
    public DayExpenses() {
        expenses = new HashMap<ExpensesType, Money>();
        for (ExpensesType type : ExpensesType.values()) {
            expenses.put(type, new Money("0.00"));
        }
    }

    public DayExpenses(final Money eatingOut, final Money products,
                       final Money unreasonableWaste, final Money transport,
                       final Money services, final Money entertainment) {
        expenses = new HashMap<ExpensesType, Money>();
        expenses.put(ExpensesType.EatingOut, eatingOut);
        expenses.put(ExpensesType.Products, products);
        expenses.put(ExpensesType.UnreasonableWaste, unreasonableWaste);
        expenses.put(ExpensesType.Transport, transport);
        expenses.put(ExpensesType.Services, services);
        expenses.put(ExpensesType.Entertainment, entertainment);
    }

    public void add(final ExpensesType type, final Money amount) throws IllegalArgumentException {
        Money newAmount = expenses.get(type).add(amount);
        expenses.put(type, newAmount);
    }

    public Money get(final ExpensesType type) {
        return expenses.get(type);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof DayExpenses) {
            DayExpenses expense = (DayExpenses) o;
            return expenses.equals(expense.expenses);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return expenses.hashCode();
    }

    private Map<ExpensesType, Money> expenses;
}

package ru.unn.agile.FinanceCalculator.Model;

import java.util.Calendar;
import java.util.Date;

public final class Expenses {
    public Expenses() {
        storage = new ExpensesStorage();
    }

    public Money getCost(final Calendar calendarDate, final ExpensesType type) {
        DayExpenses expense = storage.get(calendarDate);
        return expense.get(type);
    }

    public void addCost(final DayExpenses cost,
                        final Calendar calendarDate) throws IllegalArgumentException {
        storage.put(calendarDate, cost);
    }

    public void addCost(final Money cost,
                        final Calendar calendarDate,
                        final ExpensesType type) throws IllegalArgumentException {
        checkDate(calendarDate);
        DayExpenses expense = storage.get(calendarDate);
        expense.add(type, cost);
        storage.put(calendarDate, expense);
    }

    private void checkDate(final Calendar calendarDate) throws IllegalArgumentException {
        Calendar currentDate = Calendar.getInstance();
        Date today = currentDate.getTime();
        Date inputDate = calendarDate.getTime();
        if (inputDate.after(today)) {
           throw new IllegalArgumentException("Date can't be after today");
        }
    }

    private ExpensesStorage storage;
}

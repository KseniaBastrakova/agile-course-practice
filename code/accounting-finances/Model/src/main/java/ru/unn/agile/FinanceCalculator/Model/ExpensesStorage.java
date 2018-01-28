package ru.unn.agile.FinanceCalculator.Model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ExpensesStorage {
    public ExpensesStorage() {
        daysExpenses = new HashMap<Calendar, DayExpenses>();
    }

    public DayExpenses get(final Calendar calendarDate) {
        Calendar normalDate = computeKey(calendarDate);
        DayExpenses expense = daysExpenses.get(normalDate);
        if (expense != null) {
            return expense;
        } else {
            return new DayExpenses();
        }
    }

    public void put(final Calendar calendarDate, final DayExpenses expense) {
        Calendar normalDate = computeKey(calendarDate);
        daysExpenses.put(normalDate, expense);
    }

    private Calendar computeKey(final Calendar calendarDate) {
        Calendar keyDate = calendarDate;
        keyDate.set(Calendar.MILLISECOND, 0);
        keyDate.set(Calendar.SECOND, 0);
        keyDate.set(Calendar.MINUTE, 0);
        keyDate.set(Calendar.HOUR, 0);
        return keyDate;
    }

    private Map<Calendar, DayExpenses> daysExpenses;
}

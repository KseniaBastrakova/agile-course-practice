package ru.unn.agile.FinanceCalculator.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum ExpensesType {
    EatingOut,
    Products,
    UnreasonableWaste,
    Transport,
    Services,
    Entertainment;

    public static Collection<Object[]> arrayOfTypes() {
        List<ExpensesType> enumValuesList = Arrays.asList(ExpensesType.values());
        List<Object[]> list = new ArrayList<>();
        for (ExpensesType value: enumValuesList) {
            list.add(new Object[]{value});
        }
        return list;
    }
}

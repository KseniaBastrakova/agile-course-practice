package ru.unn.agile.FinanceCalculator.Model;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertTrue;

public class MoneyTest {
    @Test
    public void isConstructorCorrect() {
        Money money = new Money("55.99");
        BigDecimal expected = new BigDecimal("55.99");
        assertTrue(money.getAmount().equals(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void canNotCreateCurrencyWithNegativeValue() throws IllegalArgumentException {
        Money money = new Money("-699.7");
    }

    @Test
    public void areEqualCurrenciesWithDifferentNumberOfDigitsEqual() {
        Money moneyFirst = new Money("55.9000000000000");
        Money moneySecond = new Money("55.9");
        assertTrue(moneyFirst.equals(moneySecond));
    }

    @Test
    public void areEqualCurrenciesEqual() {
        Money moneyFirst = new Money("55.9");
        Money moneySecond = new Money("55.9");
        assertTrue(moneyFirst.equals(moneySecond));
    }

    @Test
    public void isRoundingCorrect() {
        Money input = new Money("55.99999");
        Money expected = new Money("55.99");
        assertTrue(input.equals(expected));
    }
}

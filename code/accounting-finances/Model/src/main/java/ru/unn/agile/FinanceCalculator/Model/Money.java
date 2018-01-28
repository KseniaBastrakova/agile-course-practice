package ru.unn.agile.FinanceCalculator.Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    public Money(final BigDecimal theAmount) throws IllegalArgumentException {
       init(theAmount);
    }

    public Money(final String theAmount) throws IllegalArgumentException {
        BigDecimal value = new BigDecimal(theAmount);
        init(value);
    }

    public Money() {
        amount = new BigDecimal("0.00");
        amount = amount.setScale(2, RoundingMode.DOWN);
    }

    public Money add(final Money other) {
        BigDecimal newAmount = other.getAmount().add(getAmount());
        return new Money(newAmount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Money) {
            return amount.equals(((Money) o).amount);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    private void init(final BigDecimal theAmount) {
        checkPrecondition(theAmount);
        amount = theAmount;
        amount = amount.setScale(2, RoundingMode.DOWN);
    }

    private void checkPrecondition(final BigDecimal theAmount) throws IllegalArgumentException {
        if (theAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value must be non-negative!");
        }
    }

    private BigDecimal amount;
}

package com.litethinking.enterprise.domain.model.valueobject;

import com.litethinking.enterprise.domain.exception.InvalidValueObjectException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final BigDecimal amount;
    private final String currencyCode;

    private Money(BigDecimal amount, String currencyCode) {
        this.amount = amount.setScale(SCALE, ROUNDING_MODE);
        this.currencyCode = currencyCode;
    }

    public static Money of(BigDecimal amount, String currencyCode) {
        if (amount == null) {
            throw new InvalidValueObjectException("Amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidValueObjectException("Amount cannot be negative");
        }

        if (currencyCode == null || currencyCode.isBlank()) {
            throw new InvalidValueObjectException("Currency code cannot be null or empty");
        }

        return new Money(amount, currencyCode.toUpperCase());
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Money add(Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currencyCode);
    }

    public Money multiply(int quantity) {
        if (quantity < 0) {
            throw new InvalidValueObjectException("Quantity cannot be negative");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currencyCode);
    }

    private void ensureSameCurrency(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new InvalidValueObjectException(
                    "Cannot operate with different currencies: " + this.currencyCode + " vs " + other.currencyCode
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currencyCode, money.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currencyCode);
    }

    @Override
    public String toString() {
        return amount + " " + currencyCode;
    }
}

package com.parking;

public class Money {
    private long cents;

    public Money(long cents){
        this.cents = cents;
    }
    /**
     * Get the amount in cents
     * @return The amount in cents
     */
    public long getCents() {
        return cents;
    }

    /**
     * Get the amount in dollars
     * @return The amount in dollars as a double
     */
    public double getDollars() {
        return cents / 100.0;
    }
    /**
     * Add another Money amount to this amount
     * @param amountOther The Money amount to add
     * @return A new Money object with the sum
     */
    public Money add(Money amountOther) {
        return new Money(this.cents + amountOther.cents);
    }

    /**
     * String representation showing the amount in dollar format
     * @return Formatted string like "$5.00"
     */
    @Override
    public String toString() {
        return String.format("$%.2f", getDollars());
    }
}

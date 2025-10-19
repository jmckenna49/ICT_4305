package com.parking;
import java.time.Instant;

public class ParkingCharge {
    private String permitId;
    private String lotId;
    private Instant incurred;
    private Money amount;

    /**
     * Constructor for ParkingCharge
     * @param permitId The ID of the parking permit
     * @param lotId The ID of the parking lot
     * @param incurred The timestamp when the charge was incurred
     * @param amount The monetary amount of the charge
     */
    public ParkingCharge(String permitId, String lotId, Instant incurred, Money amount) {
        this.permitId = permitId;
        this.lotId = lotId;
        this.incurred = incurred;
        this.amount = amount;
    }

    /**
     * Get the permit ID associated with this charge
     * @return The permit ID
     */
    public String getPermitId() {
        return permitId;
    }

    /**
     * Get the lot ID where the charge was incurred
     * @return The lot ID
     */
    public String getLotId() {
        return lotId;
    }

    /**
     * Get the timestamp when the charge was incurred
     * @return The Instant timestamp
     */
    public Instant getIncurred() {
        return incurred;
    }

    /**
     * Get the monetary amount of the charge
     * @return The Money amount
     */
    public Money getAmount() {
        return amount;
    }

    /**
     * String representation of the parking charge
     * @return Formatted string with all charge details
     */
    @Override
    public String toString() {
        return String.format("ParkingCharge{permitId='%s', lotId='%s', incurred=%s, amount=%s}",
                permitId, lotId, incurred, amount);
    }
}

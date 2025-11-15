package com.parking;

import java.util.Calendar;
import java.util.Objects;

/**
 * This is the class that manages the parking transactions. It stores that permit
 * and which lot the vehicle is using.
 */
public class ParkingTransaction {
    private Calendar transactionDate;
    private ParkingPermit permit;
    private ParkingLot lot;
    private Money feeCharged;

    public ParkingTransaction(Calendar transactionDate, ParkingPermit permit, ParkingLot lot, Money feeCharged) {
        this.transactionDate = transactionDate;
        this.permit = permit;
        this.lot = lot;
        this.feeCharged = feeCharged;
    }

    public Calendar getTransactionDate() {
        return transactionDate;
    }

    public ParkingPermit getPermit() {
        return permit;
    }

    public ParkingLot getLot() {
        return lot;
    }

    public Money getFeeCharged() {
        return feeCharged;
    }

    @Override
    public String toString() {
        return String.format("ParkingTransaction{transactionDate=%tF, permit=%s, lot=%s, feeCharged=%s}",
                transactionDate, permit.getId(), lot.getLotId(), feeCharged);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingTransaction that = (ParkingTransaction) o;
        return Objects.equals(transactionDate, that.transactionDate) &&
                Objects.equals(permit, that.permit) &&
                Objects.equals(lot, that.lot) &&
                Objects.equals(feeCharged, that.feeCharged);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionDate, permit, lot, feeCharged);
    }
}

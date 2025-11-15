package com.parking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * This is the class that manages all the parking transactions.
 */
public class TransactionManager {
    private ArrayList<ParkingTransaction> transactions;
    private HashMap<String, ArrayList<ParkingTransaction>> vehicleTransaction;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
        this.vehicleTransaction = new HashMap<>();
    }

    /**
     * This method will create a parking transaction and will add it to the transactions list.
     * @param date The date of the parking event
     * @param permit The parking permit used
     * @param lot The parking lot where the car was parked
     * @return The created ParkingTransaction
     */
    public ParkingTransaction park(Calendar date, ParkingPermit permit, ParkingLot lot) {
        Money feeCharged = calculateCharge(permit.getVehicle().getType());
        ParkingTransaction transaction = new ParkingTransaction(date, permit, lot, feeCharged);

        // Add to main transactions list
        transactions.add(transaction);

        // Add to vehicle-specific transaction tracking
        String licensePlate = permit.getVehicle().getLicense();
        if (!vehicleTransaction.containsKey(licensePlate)) {
            vehicleTransaction.put(licensePlate, new ArrayList<>());
        }
        vehicleTransaction.get(licensePlate).add(transaction);

        // Also record the car entering the lot
        lot.entry(permit.getVehicle());

        return transaction;
    }

    /**
     * Calculate the parking charge based on car type
     * @param carType The type of car
     * @return The Money amount for the charge
     */
    private Money calculateCharge(CarType carType) {
        // Different rates for different car types
        switch (carType) {
            case COMPACT:
                return new Money(500); // $5.00
            case SUV:
                return new Money(800); // $8.00
            default:
                return new Money(600); // $6.00 default
        }
    }

    /**
     * Get all transactions for a specific permit
     * @param permit The permit to search for
     * @return List of transactions for this permit
     */
    public ArrayList<ParkingTransaction> getTransactionsForPermit(ParkingPermit permit) {
        ArrayList<ParkingTransaction> permitTransactions = new ArrayList<>();

        for (ParkingTransaction transaction : transactions) {
            if (transaction.getPermit().equals(permit)) {
                permitTransactions.add(transaction);
            }
        }

        return permitTransactions;
    }

    /**
     * Get all transactions for a specific customer
     * @param customer The customer whose transactions to retrieve
     * @return List of transactions for this customer
     */
    public ArrayList<ParkingTransaction> getTransactionsForCustomer(Customer customer) {
        ArrayList<ParkingTransaction> customerTransactions = new ArrayList<>();
        String customerId = customer.getCustomerId();

        for (ParkingTransaction transaction : transactions) {
            if (transaction.getPermit().getVehicle().getOwner().equals(customerId)) {
                customerTransactions.add(transaction);
            }
        }

        return customerTransactions;
    }

    /**
     * Calculate total charges for a specific permit
     * @param permit The permit to calculate charges for
     * @return Total Money amount of charges
     */
    public Money getParkingCharges(ParkingPermit permit) {
        Money total = new Money(0);

        for (ParkingTransaction transaction : getTransactionsForPermit(permit)) {
            total = total.add(transaction.getFeeCharged());
        }

        return total;
    }

    /**
     * Calculate total charges for a vehicle by license plate
     * @param licensePlate The license plate to calculate charges for
     * @return Total Money amount of charges
     */
    public Money getParkingCharges(String licensePlate) {
        Money total = new Money(0);

        ArrayList<ParkingTransaction> vehicleTransactions = vehicleTransaction.get(licensePlate);
        if (vehicleTransactions != null) {
            for (ParkingTransaction transaction : vehicleTransactions) {
                total = total.add(transaction.getFeeCharged());
            }
        }

        return total;
    }

    /**
     * Calculate total charges for a customer across all their permits
     * @param customer The customer to calculate charges for
     * @return Total Money amount of charges
     */
    public Money getParkingCharges(Customer customer) {
        Money total = new Money(0);

        for (ParkingTransaction transaction : getTransactionsForCustomer(customer)) {
            total = total.add(transaction.getFeeCharged());
        }

        return total;
    }

    /**
     * Get all transactions
     * @return List of all parking transactions
     */
    public ArrayList<ParkingTransaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
}
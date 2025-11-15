package com.parking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * This is the manager class which manages all the parking permits.
 */
public class PermitManager {
    private HashMap<String, ParkingPermit> permits;
    private int permitIdCounter;

    public PermitManager() {
        this.permits = new HashMap<>();
        this.permitIdCounter = 0;
    }

    /**
     * This method will create an object of ParkingPermit class and will add it
     * to the permits collection.
     * Note: The expiration date will be one year from the current date.
     * @param vehicle The car/vehicle to register
     * @return The newly created ParkingPermit
     */
    public ParkingPermit register(Car vehicle) {
        Calendar registrationDate = Calendar.getInstance();
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.YEAR, 1);

        String permitId = "PERMIT" + permitIdCounter++;
        ParkingPermit permit = new ParkingPermit(permitId, vehicle, expirationDate, registrationDate);
        permits.put(permitId, permit);

        // Update the vehicle with permit information
        vehicle.setPermit(permitId);
        vehicle.setPermitExpiration(expirationDate);

        return permit;
    }

    /**
     * Register a new parking permit for a vehicle with a specific expiration date
     * @param vehicle The car/vehicle to register
     * @param expirationDate The expiration date for the permit
     * @return The newly created ParkingPermit
     */
    public ParkingPermit register(Car vehicle, Calendar expirationDate) {
        Calendar registrationDate = Calendar.getInstance();

        String permitId = "PERMIT" + permitIdCounter++;
        ParkingPermit permit = new ParkingPermit(permitId, vehicle, expirationDate, registrationDate);
        permits.put(permitId, permit);

        // Update the vehicle with permit information
        vehicle.setPermit(permitId);
        vehicle.setPermitExpiration(expirationDate);

        return permit;
    }

    /**
     * Get a permit by its ID
     * @param permitId The permit ID to search for
     * @return The ParkingPermit, or null if not found
     */
    public ParkingPermit getPermit(String permitId) {
        return permits.get(permitId);
    }

    /**
     * Get all permits for a specific customer
     * @param customer The customer whose permits to retrieve
     * @return List of parking permits for the customer
     */
    public List<ParkingPermit> getPermitsForCustomer(Customer customer) {
        List<ParkingPermit> customerPermits = new ArrayList<>();
        String customerId = customer.getCustomerId();

        for (ParkingPermit permit : permits.values()) {
            if (permit.getVehicle().getOwner().equals(customerId)) {
                customerPermits.add(permit);
            }
        }

        return customerPermits;
    }

    /**
     * Get all permit IDs
     * @return List of all permit IDs
     */
    public List<String> getAllPermitIds() {
        return new ArrayList<>(permits.keySet());
    }

    /**
     * Check if a permit is valid on a given date
     * @param permit The permit to check
     * @param date The date to check against
     * @return true if the permit is valid, false if expired
     */
    public boolean isPermitValid(ParkingPermit permit, Calendar date) {
        return !permit.isExpired(date);
    }
}

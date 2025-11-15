package com.parking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ParkingOffice {
    private String parkingOfficeName;
    private Address parkingOfficeAddress;
    private List<Customer> listOfCustomers;
    private List<ParkingLot> listOfParkingLots;
    private TransactionManager transactionManager;
    private PermitManager permitManager;
    private int customerIdCounter;

    public ParkingOffice(String parkingOfficeName, Address parkingOfficeAddress) {
        this.parkingOfficeName = parkingOfficeName;
        this.parkingOfficeAddress = parkingOfficeAddress;
        this.listOfCustomers = new ArrayList<>();
        this.listOfParkingLots = new ArrayList<>();
        this.transactionManager = new TransactionManager();
        this.permitManager = new PermitManager();
        this.customerIdCounter = 0;
    }

    /**
     * Get the parking office name
     * @return The name of the parking office
     */
    public String getParkingOfficeName() {
        return parkingOfficeName;
    }

    /**
     * Get the parking office address
     * @return The address of the parking office
     */
    public Address getParkingOfficeAddress() {
        return parkingOfficeAddress;
    }

    /**
     * Register a new customer to the parking office
     * @param name Customer name
     * @param address Customer address
     * @param phone Customer phone
     * @return The newly created Customer object
     */
    public Customer register(String name, Address address, String phone) {
        String customerId = "Customer" + customerIdCounter++;
        Customer customer = new Customer(customerId, name, address, phone);
        listOfCustomers.add(customer);
        return customer;
    }

    /**
     * Register a customer object to the parking office
     * @param customer The customer to register
     */
    public void register(Customer customer) {
        if (!listOfCustomers.contains(customer)) {
            listOfCustomers.add(customer);
        }
    }

    /**
     * Register a car and create a parking permit for it
     * @param car The car to register
     * @return The newly created ParkingPermit
     */
    public ParkingPermit register(Car car) {
        return permitManager.register(car);
    }

    /**
     * Register a car with a specific expiration date
     * @param car The car to register
     * @param expirationDate The expiration date for the permit
     * @return The newly created ParkingPermit
     */
    public ParkingPermit register(Car car, Calendar expirationDate) {
        return permitManager.register(car, expirationDate);
    }

    /**
     * Park a car using a permit at a specific lot
     * @param date The date of parking
     * @param permit The parking permit
     * @param lot The parking lot
     * @return The created ParkingTransaction
     */
    public ParkingTransaction park(Calendar date, ParkingPermit permit, ParkingLot lot) {
        return transactionManager.park(date, permit, lot);
    }

    /**
     * Get total parking charges for a specific permit
     * @param permit The parking permit
     * @return Total Money amount of charges
     */
    public Money getParkingCharges(ParkingPermit permit) {
        return transactionManager.getParkingCharges(permit);
    }

    /**
     * Get total parking charges for a customer
     * @param customer The customer
     * @return Total Money amount of charges
     */
    public Money getParkingCharges(Customer customer) {
        return transactionManager.getParkingCharges(customer);
    }

    /**
     * Find a customer by customer ID
     * @param customerId The customer's ID to search for
     * @return The Customer object, or null if not found
     */
    public Customer getCustomer(String customerId) {
        for (Customer customer : listOfCustomers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Get a collection of all customer IDs
     * @return List of customer IDs
     */
    public List<String> getCustomerIds() {
        List<String> customerIds = new ArrayList<>();
        for (Customer customer : listOfCustomers) {
            customerIds.add(customer.getCustomerId());
        }
        return customerIds;
    }

    /**
     * Get all customers
     * @return List of all customers
     */
    public List<Customer> getCustomers() {
        return new ArrayList<>(listOfCustomers);
    }

    /**
     * Add a parking lot to the office's management
     * @param lot The ParkingLot to add
     */
    public void addLot(ParkingLot lot) {
        listOfParkingLots.add(lot);
    }

    /**
     * Get all parking lots
     * @return List of all parking lots
     */
    public List<ParkingLot> getParkingLots() {
        return new ArrayList<>(listOfParkingLots);
    }

    /**
     * Get a permit by its ID
     * @param permitId The permit ID
     * @return The ParkingPermit, or null if not found
     */
    public ParkingPermit getPermit(String permitId) {
        return permitManager.getPermit(permitId);
    }

    /**
     * Get all permit IDs
     * @return List of all permit IDs
     */
    public List<String> getPermitIds() {
        return permitManager.getAllPermitIds();
    }

    /**
     * Get permits for a specific customer
     * @param customer The customer
     * @return List of parking permits
     */
    public List<ParkingPermit> getPermitsForCustomer(Customer customer) {
        return permitManager.getPermitsForCustomer(customer);
    }

    /**
     * String representation of the parking office
     * @return Summary of the office and its managed entities
     */
    @Override
    public String toString() {
        return "ParkingOffice [Name: " + parkingOfficeName +
                ", Address: " + parkingOfficeAddress.getCity() +
                ", Customers: " + listOfCustomers.size() +
                ", Lots: " + listOfParkingLots.size() + "]";
    }
}
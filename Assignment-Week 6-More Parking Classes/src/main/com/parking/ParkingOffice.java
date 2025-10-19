package com.parking;

import java.util.ArrayList;
import java.util.List;

public class ParkingOffice {
    private String name;
    private Address address;
    private List<Customer> customers;
    private List<Car> cars;
    private List<ParkingLot> lots;
    private List<ParkingCharge> charges;
    private int customerIdCounter;

    public ParkingOffice(String name, Address address) {
        this.name = name;
        this.address = address;
        this.customers = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.lots = new ArrayList<>();
        this.charges = new ArrayList<>();
        this.customerIdCounter = 0;
    }
    /**
     * Register a customer to the parking office
     * @param name, Customer name
     * @param phone, Customers phone
     * @param address, Customer address
     * @return Return new customer object
     */
    public Customer register(String name, Address address, String phone) {
        String customerId = "Customer" + customerIdCounter++;
        Customer customer = new Customer(customerId, name, address, phone);
        customers.add(customer);
        return customer;
    }
    /**
     * Register a car for an existing customer to the parking office
     * Use existing customer register method
     * @param customer, Customer who owns the car
     * @param license, Customers license plate
     * @param type, Customers type of car, enum
     * @return Return newly created car object
     */
    public Car register(Customer customer, String license, CarType type) {
        Car car = customer.register(license, type);
        cars.add(car);
        return car;
    }
    /**
     * Find a customer by name (case-insensitive search)
     * @param customerId The customer's name to search for
     * @return The Customer object, or null if not found
     */
    public Customer getCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
    return null;
    }
    /**
     * Add a parking charge to the system and update the customer's balance
     * @param charge The ParkingCharge to add
     * @return The total amount now owed by the customer (or the charge amount if customer not found)
     */
    public Money addCharge(ParkingCharge charge){
        charges.add(charge);

        // Find the customer associated with the permit
        Customer customer = findCustomerByPermitId(charge.getPermitId());

        if (customer != null) {
            return getTotalChargesForCustomer(customer);
        }

        // If customer not found, return the charge amount
        return charge.getAmount();
    }
    /**
     * Calculate the total charges owed by a specific customer
     * @param customer The customer to calculate charges for
     * @return The total Money amount of all charges for this customer
     */
    private Money getTotalChargesForCustomer(Customer customer) {
        Money total = new Money(0);
        String customerId = customer.getCustomerId();

        // Sum all charges associated with this customer's cars
        for (ParkingCharge charge : charges) {
            // Find the car with this permit
            for (Car car : cars) {
                if (car.getPermit() != null &&
                        car.getPermit().equals(charge.getPermitId()) &&
                        car.getOwner().equals(customerId)) {
                    total = total.add(charge.getAmount());
                    break;
                }
            }
        }

        return total;
    }

    /**
     * Find a customer by their car's permit ID
     * @param permitId The permit ID to search for
     * @return The Customer who owns the car with this permit, or null if not found
     */
    private Customer findCustomerByPermitId(String permitId) {
        // First, find the car with this permit
        for (Car car : cars) {
            if (car.getPermit() != null && car.getPermit().equals(permitId)) {
                // car.getOwner() returns a String (customerId), not a Customer object
                String ownerId = car.getOwner();

                // Now find the customer with this customerId
                for (Customer customer : customers) {
                    if (customer.getCustomerId().equals(ownerId)) {
                        return customer;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Add a parking lot to the office's management
     * @param lot The ParkingLot to add
     */
    public void addLot(ParkingLot lot) {
        lots.add(lot);
    }

    /**
     * String representation of the parking office
     * @return Summary of the office and its managed entities
     */
    @Override
    public String toString() {
        return "ParkingOffice [Name: " + name +
                ", Customers: " + customers.size() +
                ", Cars: " + cars.size() +
                ", Lots: " + lots.size() +
                ", Total Charges: " + charges.size() + "]";
    }
}

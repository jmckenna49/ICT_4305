package com.parking;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String name;
    private Address address;
    private String phoneNumber;
    private List<Car> registeredCars;

    public Customer(String customerId, String name, Address address, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registeredCars = new ArrayList<>();
    }
    public String getCustomerId() {
        return customerId;
    }
    public String getName() {
        return name;
    }
    public Address getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public List<Car> getRegisteredCars() {
        return registeredCars;
    }

    public Car register(String license, CarType type) {
        Car newCar = new Car(license, type, this.customerId);
        registeredCars.add(newCar);
        return newCar;
    }

    @Override
    public String toString() {
        String result = "Customer ID: " + customerId +
                ", Name: " + name +
                ", Phone: " + phoneNumber + "\n";

        result += "Address:\n" + address.getAddressInfo();

        if (!registeredCars.isEmpty()) {
            result += "\nRegistered Cars: " + registeredCars.size();
            for (Car car : registeredCars) {
                result += "\n  " + car.toString();
            }
        }

        return result;
    }
}

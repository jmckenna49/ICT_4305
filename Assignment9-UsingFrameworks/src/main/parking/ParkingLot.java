package com.parking;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingLot {
    private String lotId;
    private Address address;
    private int capacity;
    private List<Car> parkedCars;

    public ParkingLot(String lotId, Address address, int capacity) {
        this.lotId = lotId;
        this.address = address;
        this.capacity = capacity;
        this.parkedCars = new ArrayList<>();
    }

    public String getLotId() {
        return this.lotId;
    }

    public Address getAddress() {
        return this.address;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void entry(Car car) {
        if (parkedCars.size() >= capacity) {
            System.out.println("Parking lot is FULL. Cannot admit " + car.getLicense());
            return;
        }

        parkedCars.add(car);
        System.out.println("Car " + car.getLicense() + " entered lot " + lotId);
    }

    @Override
    public String toString() {
        String result = "ParkingLot [ID: " + lotId +
                ", Capacity: " + capacity +
                ", Current Occupancy: " + parkedCars.size() + "]\n";

        result += "Address:\n" + address.getAddressInfo();

        if (!parkedCars.isEmpty()) {
            result += "\nParked Cars:";
            for (Car car : parkedCars) {
                result += "\n  " + car.toString();
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLot that = (ParkingLot) o;
        return capacity == that.capacity &&
                Objects.equals(lotId, that.lotId) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lotId, address, capacity);
    }
}

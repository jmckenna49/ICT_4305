package com.parking;

import java.util.Calendar;
import java.util.Objects;

/**
 * This class maintains all the Parking permit related information like its id, vehicle,
 * and expiration date details. Using this class we can create, modify, and print parking permit information.
 */
public class ParkingPermit {
    private String id;
    private Car vehicle;
    private Calendar expirationDate;
    private Calendar registrationDate;

    public ParkingPermit(String id, Car vehicle, Calendar expirationDate, Calendar registrationDate) {
        this.id = id;
        this.vehicle = vehicle;
        this.expirationDate = expirationDate;
        this.registrationDate = registrationDate;
    }

    public String getId() {
        return id;
    }

    public Car getVehicle() {
        return vehicle;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public Calendar getRegistrationDate() {
        return registrationDate;
    }

    public boolean isExpired(Calendar currentDate) {
        return currentDate.after(expirationDate);
    }

    @Override
    public String toString() {
        return String.format("ParkingPermit{id='%s', vehicle=%s, expirationDate=%tF, registrationDate=%tF}",
                id, vehicle.getLicense(), expirationDate, registrationDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingPermit that = (ParkingPermit) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(vehicle, that.vehicle) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(registrationDate, that.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vehicle, expirationDate, registrationDate);
    }
}

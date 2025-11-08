package com.parking;

import java.time.LocalDate;
import java.util.Objects;

public class Car {
    private String permit;
    private LocalDate permitExpiration;
    private String license;
    private CarType type;
    private String owner;

    public Car(String license, CarType type, String owner) {
        this.license = license;
        this.type = type;
        this.owner = owner;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public LocalDate getPermitExpiration() {
        return permitExpiration;
    }

    public void setPermitExpiration(LocalDate permitExpiration) {
        this.permitExpiration = permitExpiration;
    }

    public String getLicense() {
        return license;
    }

    public CarType getType() {
        return type;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        String permitInfo = (permit != null) ? permit : "None";
        String expirationInfo = (permitExpiration != null) ? permitExpiration.toString() : "N/A";

        return "Car [License: " + license +
                ", Type: " + type +
                ", Owner: " + owner +
                ", Permit: " + permitInfo +
                ", Expires: " + expirationInfo +
                "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(license, car.license) &&
                type == car.type &&
                Objects.equals(owner, car.owner) &&
                Objects.equals(permit, car.permit) &&
                Objects.equals(permitExpiration, car.permitExpiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(license, type, owner, permit, permitExpiration);
    }
}
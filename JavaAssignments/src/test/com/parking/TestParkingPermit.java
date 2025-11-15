package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;

public class TestParkingPermit {

    private Car car;
    private Calendar registrationDate;
    private Calendar expirationDate;
    private ParkingPermit permit;

    @BeforeEach
    public void setUp() {
        car = new Car("ABC123", CarType.COMPACT, "Customer0");

        registrationDate = Calendar.getInstance();
        registrationDate.set(2025, Calendar.JANUARY, 1);

        expirationDate = Calendar.getInstance();
        expirationDate.set(2026, Calendar.JANUARY, 1);

        permit = new ParkingPermit("PERMIT0", car, expirationDate, registrationDate);
    }

    @Test
    public void testParkingPermitConstructor() {
        assertNotNull(permit);
        assertEquals("PERMIT0", permit.getId());
        assertEquals(car, permit.getVehicle());
        assertEquals(expirationDate, permit.getExpirationDate());
        assertEquals(registrationDate, permit.getRegistrationDate());
    }

    @Test
    public void testGetId() {
        assertEquals("PERMIT0", permit.getId());
    }

    @Test
    public void testGetVehicle() {
        assertEquals(car, permit.getVehicle());
        assertEquals("ABC123", permit.getVehicle().getLicense());
        assertEquals(CarType.COMPACT, permit.getVehicle().getType());
    }

    @Test
    public void testGetExpirationDate() {
        assertEquals(expirationDate, permit.getExpirationDate());
    }

    @Test
    public void testGetRegistrationDate() {
        assertEquals(registrationDate, permit.getRegistrationDate());
    }

    @Test
    public void testIsExpiredWhenNotExpired() {
        Calendar checkDate = Calendar.getInstance();
        checkDate.set(2025, Calendar.JUNE, 1);

        assertFalse(permit.isExpired(checkDate));
    }

    @Test
    public void testIsExpiredWhenExpired() {
        Calendar checkDate = Calendar.getInstance();
        checkDate.set(2027, Calendar.JANUARY, 1);

        assertTrue(permit.isExpired(checkDate));
    }

    @Test
    public void testIsExpiredOnExpirationDate() {
        // On the exact expiration date, should not be expired
        assertFalse(permit.isExpired(expirationDate));
    }

    @Test
    public void testIsExpiredOneDayAfter() {
        Calendar checkDate = (Calendar) expirationDate.clone();
        checkDate.add(Calendar.DAY_OF_MONTH, 1);

        assertTrue(permit.isExpired(checkDate));
    }

    @Test
    public void testToString() {
        String result = permit.toString();

        assertTrue(result.contains("PERMIT0"));
        assertTrue(result.contains("ABC123"));
    }

    @Test
    public void testParkingPermitEquals() {
        ParkingPermit permit1 = new ParkingPermit("PERMIT0", car, expirationDate, registrationDate);
        ParkingPermit permit2 = new ParkingPermit("PERMIT0", car, expirationDate, registrationDate);

        Car differentCar = new Car("XYZ789", CarType.SUV, "Customer1");
        ParkingPermit permit3 = new ParkingPermit("PERMIT1", differentCar, expirationDate, registrationDate);

        assertEquals(permit1, permit2);
        assertNotEquals(permit1, permit3);
        assertEquals(permit1, permit1); // reflexive
    }

    @Test
    public void testParkingPermitHashCode() {
        ParkingPermit permit1 = new ParkingPermit("PERMIT0", car, expirationDate, registrationDate);
        ParkingPermit permit2 = new ParkingPermit("PERMIT0", car, expirationDate, registrationDate);

        assertEquals(permit1.hashCode(), permit2.hashCode());
    }

    @Test
    public void testPermitWithSUV() {
        Car suvCar = new Car("SUV123", CarType.SUV, "Customer1");
        ParkingPermit suvPermit = new ParkingPermit("PERMIT-SUV", suvCar, expirationDate, registrationDate);

        assertEquals(CarType.SUV, suvPermit.getVehicle().getType());
    }

    @Test
    public void testParkingPermitEqualsWithNull() {
        assertNotEquals(null, permit);
    }

    @Test
    public void testParkingPermitEqualsWithDifferentClass() {
        assertFalse(permit.equals("Not a permit"));
    }

    @Test
    public void testParkingPermitEqualsWithDifferentId() {
        ParkingPermit permit1 = new ParkingPermit("PERMIT0", car, expirationDate, registrationDate);
        ParkingPermit permit2 = new ParkingPermit("PERMIT1", car, expirationDate, registrationDate);

        assertNotEquals(permit1, permit2);
    }

    @Test
    public void testParkingPermitEqualsWithDifferentDates() {
        Calendar differentExpiration = Calendar.getInstance();
        differentExpiration.set(2027, Calendar.JANUARY, 1);

        ParkingPermit permit1 = new ParkingPermit("PERMIT0", car, expirationDate, registrationDate);
        ParkingPermit permit2 = new ParkingPermit("PERMIT0", car, differentExpiration, registrationDate);

        assertNotEquals(permit1, permit2);
    }
}
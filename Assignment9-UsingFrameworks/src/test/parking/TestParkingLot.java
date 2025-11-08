package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class TestParkingLot {

    private ParkingLot lot;
    private Address lotAddress;
    private Car car1;
    private Car car2;

    @BeforeEach
    public void setUp() {
        lotAddress = new Address("1243 St", "",
                "Boston", "MA", "12345");
        lot = new ParkingLot("Lot-01", lotAddress, 100);

        car1 = new Car("WEE123", CarType.COMPACT, "James");
        car1.setPermit("P-001");
        car1.setPermitExpiration(LocalDate.now().plusMonths(6));

        car2 = new Car("BEE456", CarType.SUV, "Luna");
        car2.setPermit("P-002");
        car2.setPermitExpiration(LocalDate.now().plusMonths(3));
    }

    @Test
    public void testParkingLotCreation() {
        assertEquals("Lot-01", lot.getLotId());
        assertEquals(lotAddress, lot.getAddress());
        assertEquals(100, lot.getCapacity());
    }

    @Test
    public void testEntryWithValidCar() {
        lot.entry(car1);

        String result = lot.toString();
        assertTrue(result.contains("WEE123"));
        assertTrue(result.contains("Current Occupancy: 1"));
    }

    @Test
    public void testEntryMultipleCars() {
        lot.entry(car1);
        lot.entry(car2);

        String result = lot.toString();
        assertTrue(result.contains("WEE123"));
        assertTrue(result.contains("BEE456"));
        assertTrue(result.contains("Current Occupancy: 2"));
    }

    @Test
    public void testEntryWhenFull() {
        ParkingLot smallLot = new ParkingLot("Lot-02", lotAddress, 2);

        smallLot.entry(car1);
        smallLot.entry(car2);

        Car car3 = new Car("SEE789", CarType.COMPACT, "Nathan");
        car3.setPermit("P-003");
        car3.setPermitExpiration(LocalDate.now().plusMonths(1));

        // Third car should be rejected (lot is full)
        smallLot.entry(car3);

        String result = smallLot.toString();
        assertTrue(result.contains("Current Occupancy: 2"));
        assertFalse(result.contains("SEE789"));
    }

    @Test
    public void testToStringEmptyLot() {
        String result = lot.toString();

        assertTrue(result.contains("Lot-01"));
        assertTrue(result.contains("Capacity: 100"));
        assertTrue(result.contains("Current Occupancy: 0"));
        assertTrue(result.contains("1243 St"));
    }

    @Test
    public void testToStringWithCars() {
        lot.entry(car1);
        lot.entry(car2);

        String result = lot.toString();

        assertTrue(result.contains("Parked Cars:"));
        assertTrue(result.contains("WEE123"));
        assertTrue(result.contains("BEE456"));
    }

    @Test
    public void testCapacityLimit() {
        ParkingLot tinyLot = new ParkingLot("LOT-C", lotAddress, 1);

        tinyLot.entry(car1);
        tinyLot.entry(car2); // Should fail

        String result = tinyLot.toString();
        assertTrue(result.contains("Current Occupancy: 1"));
    }

    @Test
    public void testParkingLotEquals() {
        ParkingLot lot1 = new ParkingLot("Lot-01", lotAddress, 100);
        ParkingLot lot2 = new ParkingLot("Lot-01", lotAddress, 100);
        ParkingLot lot3 = new ParkingLot("Lot-99", lotAddress, 100);

        assertEquals(lot1, lot2);
        assertNotEquals(lot1, lot3);
        assertEquals(lot1, lot1);
    }

    @Test
    public void testParkingLotHashCode() {
        ParkingLot lot1 = new ParkingLot("Lot-01", lotAddress, 100);
        ParkingLot lot2 = new ParkingLot("Lot-01", lotAddress, 100);

        assertEquals(lot1.hashCode(), lot2.hashCode());
    }

    @Test
    public void testParkingLotEqualsWithNull() {
        assertNotEquals(null, lot);
    }

    @Test
    public void testParkingLotEqualsWithDifferentClass() {
        assertFalse(lot.equals("Not a parking lot"));
    }
}
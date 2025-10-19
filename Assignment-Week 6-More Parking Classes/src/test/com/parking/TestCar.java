package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class TestCar {

    private Car car;

    @BeforeEach
    public void setUp() {
        car = new Car("WEE123", CarType.COMPACT, "James");
    }

    @Test
    public void testCarCreation() {
        assertEquals("WEE123", car.getLicense());
        assertEquals(CarType.COMPACT, car.getType());
        assertEquals("James", car.getOwner());
        assertNull(car.getPermit());
        assertNull(car.getPermitExpiration());
    }

    @Test
    public void testSetPermit() {
        car.setPermit("123456");
        assertEquals("123456", car.getPermit());
    }

    @Test
    public void testSetPermitExpiration() {
        LocalDate expiration = LocalDate.now().plusMonths(6);
        car.setPermitExpiration(expiration);
        assertEquals(expiration, car.getPermitExpiration());
    }

    @Test
    public void testToStringWithNoPermit() {
        String result = car.toString();

        assertTrue(result.contains("WEE123"));
        assertTrue(result.contains("COMPACT"));
        assertTrue(result.contains("James"));
        assertTrue(result.contains("None"));
    }

    @Test
    public void testToStringWithPermit() {
        car.setPermit("123456");
        car.setPermitExpiration(LocalDate.of(2025, 12, 31));

        String result = car.toString();

        assertTrue(result.contains("WEE123"));
        assertTrue(result.contains("123456"));
        assertTrue(result.contains("2025-12-31"));
    }

    @Test
    public void testSUVCarType() {
        Car suvCar = new Car("BEE456", CarType.SUV, "Luna");
        assertEquals(CarType.SUV, suvCar.getType());
        assertTrue(suvCar.toString().contains("SUV"));
    }
}

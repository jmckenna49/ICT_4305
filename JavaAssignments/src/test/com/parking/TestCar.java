package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;

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
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MONTH, 6);
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
        Calendar expiration = Calendar.getInstance();
        expiration.set(2025, Calendar.DECEMBER, 31);
        car.setPermitExpiration(expiration);

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

    @Test
    public void testCarEquals() {
        Car car1 = new Car("WEE123", CarType.COMPACT, "James");
        Car car2 = new Car("WEE123", CarType.COMPACT, "James");
        Car car3 = new Car("BEE456", CarType.SUV, "Luna");

        assertEquals(car1, car2);
        assertNotEquals(car1, car3);
        assertEquals(car1, car1); // reflexive
    }

    @Test
    public void testCarHashCode() {
        Car car1 = new Car("WEE123", CarType.COMPACT, "James");
        Car car2 = new Car("WEE123", CarType.COMPACT, "James");

        assertEquals(car1.hashCode(), car2.hashCode());
    }

    @Test
    public void testCarEqualsWithPermit() {
        Car car1 = new Car("WEE123", CarType.COMPACT, "James");
        Car car2 = new Car("WEE123", CarType.COMPACT, "James");

        Calendar expiration = Calendar.getInstance();
        expiration.set(2025, Calendar.DECEMBER, 31);

        car1.setPermit("P-001");
        car2.setPermit("P-001");
        car1.setPermitExpiration(expiration);
        car2.setPermitExpiration(expiration);

        assertEquals(car1, car2);
    }

    @Test
    public void testCarEqualsWithDifferentPermit() {
        Car car1 = new Car("WEE123", CarType.COMPACT, "James");
        Car car2 = new Car("WEE123", CarType.COMPACT, "James");

        car1.setPermit("P-001");
        car2.setPermit("P-002");

        assertNotEquals(car1, car2);
    }

    @Test
    public void testCarEqualsWithDifferentFields() {
        Car base = new Car("WEE123", CarType.COMPACT, "James");
        Car diffLicense = new Car("BEE456", CarType.COMPACT, "James");
        Car diffType = new Car("WEE123", CarType.SUV, "James");
        Car diffOwner = new Car("WEE123", CarType.COMPACT, "Luna");

        assertNotEquals(base, diffLicense);
        assertNotEquals(base, diffType);
        assertNotEquals(base, diffOwner);
    }

    @Test
    public void testCarEqualsWithNull() {
        Car car = new Car("WEE123", CarType.COMPACT, "James");
        assertNotEquals(null, car);
    }

    @Test
    public void testCarEqualsWithDifferentClass() {
        Car car = new Car("WEE123", CarType.COMPACT, "James");
        assertFalse(car.equals("Not a car"));
    }
}

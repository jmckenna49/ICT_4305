package com.parking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCustomer {

    private Customer customer;
    private Address address;

    @BeforeEach
    public void setUp() {
        address = new Address("1243 St", "Apt 201",
                "Boston", "MA", "12345");
        customer = new Customer("001", "James McKenna",
                address, "123-456-7890");
    }

    @Test
    public void testCustomerConstructor() {
        assertEquals("001", customer.getCustomerId());
        assertEquals("James McKenna", customer.getName());
        assertEquals(address, customer.getAddress());
        assertEquals("123-456-7890", customer.getPhoneNumber());
        assertEquals(0, customer.getRegisteredCars().size());
    }

    @Test
    public void testRegisterOneCar() {
        Car car = customer.register("ABC123", CarType.COMPACT);

        assertNotNull(car);
        assertEquals("ABC123", car.getLicense());
        assertEquals(CarType.COMPACT, car.getType());
        assertEquals("001", car.getOwner());
        assertEquals(1, customer.getRegisteredCars().size());
    }

    @Test
    public void testRegisterMultipleCars() {
        Car car1 = customer.register("ABC123", CarType.COMPACT);
        Car car2 = customer.register("XYZ789", CarType.SUV);

        assertEquals(2, customer.getRegisteredCars().size());
        assertTrue(customer.getRegisteredCars().contains(car1));
        assertTrue(customer.getRegisteredCars().contains(car2));
    }

    @Test
    public void testRegisterCarWithSameLicense() {
        Car car1 = customer.register("ABC123", CarType.COMPACT);
        Car car2 = customer.register("ABC123", CarType.SUV);

        // Should still have 2 separate cars (no duplicate prevention in simplified version)
        assertEquals(2, customer.getRegisteredCars().size());
    }

    @Test
    public void testToStringWithNoCars() {
        String result = customer.toString();

        assertTrue(result.contains("Customer ID: 001"));
        assertTrue(result.contains("Name: James McKenna"));
        assertTrue(result.contains("Phone: 123-456-7890"));
        assertTrue(result.contains("1243 St"));
    }

    @Test
    public void testToStringWithCars() {
        customer.register("ABC123", CarType.COMPACT);
        customer.register("XYZ789", CarType.SUV);

        String result = customer.toString();

        assertTrue(result.contains("Registered Cars: 2"));
        assertTrue(result.contains("ABC123"));
        assertTrue(result.contains("XYZ789"));
    }
}

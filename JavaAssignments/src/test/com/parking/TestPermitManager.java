package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import java.util.List;

public class TestPermitManager {

    private PermitManager permitManager;
    private Customer customer;
    private Car car1;
    private Car car2;

    @BeforeEach
    public void setUp() {
        permitManager = new PermitManager();

        Address customerAddress = new Address("123 Main St", "Apt 1", "Boston", "MA", "12345");
        customer = new Customer("Customer0", "John Doe", customerAddress, "555-1234");

        car1 = new Car("ABC123", CarType.COMPACT, customer.getCustomerId());
        car2 = new Car("XYZ789", CarType.SUV, customer.getCustomerId());
    }

    @Test
    public void testRegisterCarDefaultExpiration() {
        ParkingPermit permit = permitManager.register(car1);

        assertNotNull(permit);
        assertNotNull(permit.getId());
        assertTrue(permit.getId().startsWith("PERMIT"));
        assertEquals(car1, permit.getVehicle());
        assertNotNull(permit.getExpirationDate());
        assertNotNull(permit.getRegistrationDate());

        // Verify car was updated with permit info
        assertEquals(permit.getId(), car1.getPermit());
        assertEquals(permit.getExpirationDate(), car1.getPermitExpiration());
    }

    @Test
    public void testRegisterCarWithCustomExpiration() {
        Calendar customExpiration = Calendar.getInstance();
        customExpiration.set(2027, Calendar.DECEMBER, 31);

        ParkingPermit permit = permitManager.register(car1, customExpiration);

        assertNotNull(permit);
        assertEquals(customExpiration, permit.getExpirationDate());
        assertEquals(customExpiration, car1.getPermitExpiration());
    }

    @Test
    public void testRegisterMultipleCars() {
        ParkingPermit permit1 = permitManager.register(car1);
        ParkingPermit permit2 = permitManager.register(car2);

        assertNotNull(permit1);
        assertNotNull(permit2);
        assertNotEquals(permit1.getId(), permit2.getId());
    }

    @Test
    public void testGetPermit() {
        ParkingPermit permit = permitManager.register(car1);

        ParkingPermit retrieved = permitManager.getPermit(permit.getId());

        assertNotNull(retrieved);
        assertEquals(permit.getId(), retrieved.getId());
        assertEquals(permit.getVehicle(), retrieved.getVehicle());
    }

    @Test
    public void testGetPermitNotFound() {
        ParkingPermit retrieved = permitManager.getPermit("NONEXISTENT");
        assertNull(retrieved);
    }

    @Test
    public void testGetPermitsForCustomer() {
        customer.register("ABC123", CarType.COMPACT);
        customer.register("XYZ789", CarType.SUV);

        List<Car> cars = customer.getRegisteredCars();
        ParkingPermit permit1 = permitManager.register(cars.get(0));
        ParkingPermit permit2 = permitManager.register(cars.get(1));

        List<ParkingPermit> permits = permitManager.getPermitsForCustomer(customer);

        assertEquals(2, permits.size());
        assertTrue(permits.contains(permit1));
        assertTrue(permits.contains(permit2));
    }

    @Test
    public void testGetPermitsForCustomerWithNoCars() {
        List<ParkingPermit> permits = permitManager.getPermitsForCustomer(customer);

        assertEquals(0, permits.size());
    }

    @Test
    public void testGetPermitsForDifferentCustomers() {
        Address addr2 = new Address("456 Oak St", null, "Boston", "MA", "12346");
        Customer customer2 = new Customer("Customer1", "Jane Smith", addr2, "555-5678");

        Car car3 = new Car("DEF456", CarType.COMPACT, customer2.getCustomerId());

        ParkingPermit permit1 = permitManager.register(car1);
        ParkingPermit permit2 = permitManager.register(car2);
        ParkingPermit permit3 = permitManager.register(car3);

        List<ParkingPermit> customer1Permits = permitManager.getPermitsForCustomer(customer);
        List<ParkingPermit> customer2Permits = permitManager.getPermitsForCustomer(customer2);

        assertEquals(2, customer1Permits.size());
        assertTrue(customer1Permits.contains(permit1));
        assertTrue(customer1Permits.contains(permit2));
        assertFalse(customer1Permits.contains(permit3));

        assertEquals(1, customer2Permits.size());
        assertTrue(customer2Permits.contains(permit3));
    }

    @Test
    public void testGetAllPermitIds() {
        ParkingPermit permit1 = permitManager.register(car1);
        ParkingPermit permit2 = permitManager.register(car2);

        List<String> permitIds = permitManager.getAllPermitIds();

        assertEquals(2, permitIds.size());
        assertTrue(permitIds.contains(permit1.getId()));
        assertTrue(permitIds.contains(permit2.getId()));
    }

    @Test
    public void testGetAllPermitIdsEmpty() {
        List<String> permitIds = permitManager.getAllPermitIds();
        assertEquals(0, permitIds.size());
    }

    @Test
    public void testIsPermitValid() {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.YEAR, 1);
        ParkingPermit permit = permitManager.register(car1, expiration);

        Calendar today = Calendar.getInstance();
        assertTrue(permitManager.isPermitValid(permit, today));
    }

    @Test
    public void testIsPermitExpired() {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.DAY_OF_MONTH, -1); // Yesterday
        ParkingPermit permit = permitManager.register(car1, expiration);

        Calendar today = Calendar.getInstance();
        assertFalse(permitManager.isPermitValid(permit, today));
    }

    @Test
    public void testPermitIdCounter() {
        ParkingPermit permit1 = permitManager.register(car1);
        ParkingPermit permit2 = permitManager.register(car2);

        assertTrue(permit1.getId().startsWith("PERMIT"));
        assertTrue(permit2.getId().startsWith("PERMIT"));

        // Extract the numbers and verify they increment
        String id1 = permit1.getId().replace("PERMIT", "");
        String id2 = permit2.getId().replace("PERMIT", "");

        int num1 = Integer.parseInt(id1);
        int num2 = Integer.parseInt(id2);

        assertEquals(num1 + 1, num2);
    }

    @Test
    public void testDefaultExpirationIsOneYear() {
        Calendar beforeRegistration = Calendar.getInstance();
        ParkingPermit permit = permitManager.register(car1);
        Calendar afterRegistration = Calendar.getInstance();

        Calendar permitExpiration = permit.getExpirationDate();
        Calendar expectedExpiration = Calendar.getInstance();
        expectedExpiration.add(Calendar.YEAR, 1);

        // Check that expiration is approximately 1 year from now (within 1 day)
        long daysDifference = Math.abs(
                (permitExpiration.getTimeInMillis() - expectedExpiration.getTimeInMillis())
                        / (1000 * 60 * 60 * 24)
        );

        assertTrue(daysDifference <= 1);
    }
}

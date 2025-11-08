package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.util.List;

public class TestParkingOffice {

    private ParkingOffice office;
    private Address officeAddress;

    @BeforeEach
    public void setUp() {
        officeAddress = new Address("100 Main St", null, "Boston", "MA", "62701");
        office = new ParkingOffice("City Parking", officeAddress);
    }

    @Test
    public void testParkingOfficeConstructor() {
        assertNotNull(office);
        assertTrue(office.toString().contains("City Parking"));
    }

    @Test
    public void testRegisterCustomer() {
        Address customerAddress = new Address("123 Hello St", "Apt 201", "Boston", "MA", "67890");
        Customer customer = office.register("James McKenna", customerAddress, "000-1234");

        assertNotNull(customer);
        assertEquals("James McKenna", customer.getName());
        assertEquals("000-1234", customer.getPhoneNumber());
        assertTrue(customer.getCustomerId().startsWith("Customer"));
    }

    @Test
    public void testRegisterMultipleCustomers() {
        Address addr1 = new Address("123 Hello St", null, "Boston", "MA", "67890");
        Address addr2 = new Address("456 Goodbye St", null, "Boston", "MA", "62703");

        Customer customer1 = office.register("James McKenna", addr1, "000-1111");
        Customer customer2 = office.register("Luna McKenna", addr2, "000-2222");

        assertNotNull(customer1);
        assertNotNull(customer2);
        assertNotEquals(customer1.getCustomerId(), customer2.getCustomerId());
    }

    @Test
    public void testRegisterCar() {
        Address customerAddress = new Address("789 Standing St", null, "Boston", "MA", "62704");
        Customer customer = office.register("Alice Johnson", customerAddress, "000-5555");

        Car car = office.register(customer, "CAR-A01", CarType.SUV);

        assertNotNull(car);
        assertEquals("CAR-A01", car.getLicense());
        assertEquals(CarType.SUV, car.getType());
        assertEquals(customer.getCustomerId(), car.getOwner());
        assertEquals(1, customer.getRegisteredCars().size());
    }

    @Test
    public void testRegisterMultipleCarsForCustomer() {
        Address customerAddress = new Address("321 Maple St", null, "Boston", "MA", "62705");
        Customer customer = office.register("Bob Williams", customerAddress, "000-6666");

        Car car1 = office.register(customer, "CAR-B01", CarType.COMPACT);
        Car car2 = office.register(customer, "CAR-B02", CarType.SUV);

        assertEquals(2, customer.getRegisteredCars().size());
        assertTrue(customer.getRegisteredCars().contains(car1));
        assertTrue(customer.getRegisteredCars().contains(car2));
    }

    @Test
    public void testGetCustomerById() {
        Address customerAddress = new Address("555 Cedar St", null, "Boston", "MA", "62706");
        Customer customer = office.register("Charlie Brown", customerAddress, "000-7777");

        Customer found = office.getCustomer(customer.getCustomerId());

        assertNotNull(found);
        assertEquals("Charlie Brown", found.getName());
        assertEquals(customer.getCustomerId(), found.getCustomerId());
    }

    @Test
    public void testGetCustomerNotFound() {
        Customer found = office.getCustomer("NonexistentCustomer999");
        assertNull(found);
    }

    @Test
    public void testAddLot() {
        Address lotAddress = new Address("200 Park Ave", null, "Boston", "MA", "62708");
        ParkingLot lot = new ParkingLot("Lot-001", lotAddress, 50);

        office.addLot(lot);

        String result = office.toString();
        assertTrue(result.contains("Lots: 1"));
    }

    @Test
    public void testAddChargeSingleCharge() {
        Address customerAddress = new Address("777 Spruce St", null, "Boston", "MA", "62709");
        Customer customer = office.register("Emma Davis", customerAddress, "000-9999");
        Car car = office.register(customer, "CAR-C01", CarType.COMPACT);

        String permitId = "Permit-A01";
        car.setPermit(permitId);

        Money amount = new Money(500);
        Instant now = Instant.now();
        ParkingCharge charge = new ParkingCharge(permitId, "Lot-001", now, amount);

        Money total = office.addCharge(charge);

        assertNotNull(total);
        assertEquals(500, total.getCents());
    }

    @Test
    public void testAddChargeMultipleCharges() {
        Address customerAddress = new Address("888 Willow St", null, "Boston", "MA", "62711");
        Customer customer = office.register("Frank Miller", customerAddress, "000-0000");
        Car car = office.register(customer, "CAR-D01", CarType.SUV);

        String permitId = "Permit-B01";
        car.setPermit(permitId);

        Money amount1 = new Money(500);
        Instant time1 = Instant.now();
        ParkingCharge charge1 = new ParkingCharge(permitId, "Lot-002", time1, amount1);
        Money total1 = office.addCharge(charge1);
        assertEquals(500, total1.getCents());

        Money amount2 = new Money(1000);
        Instant time2 = time1.plusSeconds(3600);
        ParkingCharge charge2 = new ParkingCharge(permitId, "Lot-002", time2, amount2);
        Money total2 = office.addCharge(charge2);
        assertEquals(1500, total2.getCents());
    }

    @Test
    public void testAddChargeForDifferentCustomers() {
        Address addr1 = new Address("111 First St", null, "Boston", "MA", "62713");
        Customer customer1 = office.register("Grace Taylor", addr1, "000-1111");
        Car car1 = office.register(customer1, "CAR-E01", CarType.COMPACT);

        Address addr2 = new Address("222 Second St", null, "Boston", "MA", "62714");
        Customer customer2 = office.register("Henry Wilson", addr2, "000-2222");
        Car car2 = office.register(customer2, "CAR-F01", CarType.SUV);

        String permit1 = "Permit-C01";
        String permit2 = "Permit-C02";
        car1.setPermit(permit1);
        car2.setPermit(permit2);

        Money amount1 = new Money(500);
        ParkingCharge charge1 = new ParkingCharge(permit1, "Lot-003", Instant.now(), amount1);
        Money total1 = office.addCharge(charge1);
        assertEquals(500, total1.getCents());

        Money amount2 = new Money(1000);
        ParkingCharge charge2 = new ParkingCharge(permit2, "Lot-003", Instant.now(), amount2);
        Money total2 = office.addCharge(charge2);
        assertEquals(1000, total2.getCents());
    }

    @Test
    public void testAddChargeWithInvalidPermit() {
        Money amount = new Money(500);
        Instant now = Instant.now();
        ParkingCharge charge = new ParkingCharge("Permit-Invalid", "Lot-999", now, amount);

        Money total = office.addCharge(charge);

        assertEquals(500, total.getCents());
    }

    @Test
    public void testToStringWithData() {
        Address customerAddress = new Address("999 Last St", null, "Boston", "MA", "62716");
        Customer customer = office.register("Iris Anderson", customerAddress, "000-3333");
        office.register(customer, "CAR-G01", CarType.COMPACT);

        Address lotAddress = new Address("600 Final Ave", null, "Boston", "MA", "62717");
        ParkingLot lot = new ParkingLot("Lot-004", lotAddress, 25);
        office.addLot(lot);

        String result = office.toString();
        assertTrue(result.contains("Customers: 1"));
        assertTrue(result.contains("Cars: 1"));
        assertTrue(result.contains("Lots: 1"));
    }

    @Test
    public void testAddChargeWithMultipleCarsForSameCustomer() {
        Address customerAddress = new Address("333 Double St", null, "Boston", "MA", "62718");
        Customer customer = office.register("Jack Black", customerAddress, "000-4444");
        Car car1 = office.register(customer, "CAR-H01", CarType.COMPACT);
        Car car2 = office.register(customer, "CAR-H02", CarType.SUV);

        String permit1 = "Permit-D01";
        String permit2 = "Permit-D02";
        car1.setPermit(permit1);
        car2.setPermit(permit2);

        Money amount1 = new Money(500);
        ParkingCharge charge1 = new ParkingCharge(permit1, "Lot-005", Instant.now(), amount1);
        Money total1 = office.addCharge(charge1);
        assertEquals(500, total1.getCents());

        Money amount2 = new Money(750);
        ParkingCharge charge2 = new ParkingCharge(permit2, "Lot-005", Instant.now(), amount2);
        Money total2 = office.addCharge(charge2);
        assertEquals(1250, total2.getCents());
    }

    // NEW TESTS FOR PART 1 REQUIREMENTS

    @Test
    public void testGetCustomerIds() {
        Address addr1 = new Address("123 First St", null, "Boston", "MA", "12345");
        Address addr2 = new Address("456 Second St", null, "Boston", "MA", "12346");
        Address addr3 = new Address("789 Third St", null, "Boston", "MA", "12347");

        Customer customer1 = office.register("Alice", addr1, "111-1111");
        Customer customer2 = office.register("Bob", addr2, "222-2222");
        Customer customer3 = office.register("Charlie", addr3, "333-3333");

        List<String> customerIds = office.getCustomerIds();

        assertNotNull(customerIds);
        assertEquals(3, customerIds.size());
        assertTrue(customerIds.contains(customer1.getCustomerId()));
        assertTrue(customerIds.contains(customer2.getCustomerId()));
        assertTrue(customerIds.contains(customer3.getCustomerId()));
    }

    @Test
    public void testGetCustomerIdsEmpty() {
        List<String> customerIds = office.getCustomerIds();

        assertNotNull(customerIds);
        assertEquals(0, customerIds.size());
    }

    @Test
    public void testGetPermitIds() {
        Address addr = new Address("123 Main St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", addr, "555-5555");

        Car car1 = office.register(customer, "LICENSE-1", CarType.COMPACT);
        Car car2 = office.register(customer, "LICENSE-2", CarType.SUV);
        Car car3 = office.register(customer, "LICENSE-3", CarType.COMPACT);

        car1.setPermit("PERMIT-001");
        car2.setPermit("PERMIT-002");
        // car3 has no permit

        List<String> permitIds = office.getPermitIds();

        assertNotNull(permitIds);
        assertEquals(2, permitIds.size());
        assertTrue(permitIds.contains("PERMIT-001"));
        assertTrue(permitIds.contains("PERMIT-002"));
        assertFalse(permitIds.contains(null));
    }

    @Test
    public void testGetPermitIdsEmpty() {
        List<String> permitIds = office.getPermitIds();

        assertNotNull(permitIds);
        assertEquals(0, permitIds.size());
    }

    @Test
    public void testGetPermitIdsWithNullPermits() {
        Address addr = new Address("123 Main St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", addr, "555-5555");

        Car car1 = office.register(customer, "LICENSE-1", CarType.COMPACT);
        Car car2 = office.register(customer, "LICENSE-2", CarType.SUV);

        // Neither car has a permit
        List<String> permitIds = office.getPermitIds();

        assertNotNull(permitIds);
        assertEquals(0, permitIds.size());
    }

    @Test
    public void testGetPermitIdsForCustomer() {
        Address addr1 = new Address("123 First St", null, "Boston", "MA", "12345");
        Address addr2 = new Address("456 Second St", null, "Boston", "MA", "12346");

        Customer customer1 = office.register("Alice", addr1, "111-1111");
        Customer customer2 = office.register("Bob", addr2, "222-2222");

        Car car1 = office.register(customer1, "LICENSE-1", CarType.COMPACT);
        Car car2 = office.register(customer1, "LICENSE-2", CarType.SUV);
        Car car3 = office.register(customer2, "LICENSE-3", CarType.COMPACT);

        car1.setPermit("PERMIT-A01");
        car2.setPermit("PERMIT-A02");
        car3.setPermit("PERMIT-B01");

        List<String> customer1Permits = office.getPermitIds(customer1);
        List<String> customer2Permits = office.getPermitIds(customer2);

        assertNotNull(customer1Permits);
        assertEquals(2, customer1Permits.size());
        assertTrue(customer1Permits.contains("PERMIT-A01"));
        assertTrue(customer1Permits.contains("PERMIT-A02"));
        assertFalse(customer1Permits.contains("PERMIT-B01"));

        assertNotNull(customer2Permits);
        assertEquals(1, customer2Permits.size());
        assertTrue(customer2Permits.contains("PERMIT-B01"));
    }

    @Test
    public void testGetPermitIdsForCustomerWithNoPermits() {
        Address addr = new Address("123 Main St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", addr, "555-5555");

        Car car1 = office.register(customer, "LICENSE-1", CarType.COMPACT);
        Car car2 = office.register(customer, "LICENSE-2", CarType.SUV);

        // No permits assigned
        List<String> permitIds = office.getPermitIds(customer);

        assertNotNull(permitIds);
        assertEquals(0, permitIds.size());
    }

    @Test
    public void testGetPermitIdsForCustomerWithNoCars() {
        Address addr = new Address("123 Main St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", addr, "555-5555");

        // No cars registered
        List<String> permitIds = office.getPermitIds(customer);

        assertNotNull(permitIds);
        assertEquals(0, permitIds.size());
    }

    @Test
    public void testGetPermitIdsForNullCustomer() {
        List<String> permitIds = office.getPermitIds(null);

        assertNotNull(permitIds);
        assertEquals(0, permitIds.size());
    }

    @Test
    public void testGetPermitIdsForCustomerMixedPermits() {
        Address addr = new Address("123 Main St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", addr, "555-5555");

        Car car1 = office.register(customer, "LICENSE-1", CarType.COMPACT);
        Car car2 = office.register(customer, "LICENSE-2", CarType.SUV);
        Car car3 = office.register(customer, "LICENSE-3", CarType.COMPACT);

        car1.setPermit("PERMIT-001");
        // car2 has no permit
        car3.setPermit("PERMIT-003");

        List<String> permitIds = office.getPermitIds(customer);

        assertNotNull(permitIds);
        assertEquals(2, permitIds.size());
        assertTrue(permitIds.contains("PERMIT-001"));
        assertTrue(permitIds.contains("PERMIT-003"));
        assertFalse(permitIds.contains(null));
    }
}
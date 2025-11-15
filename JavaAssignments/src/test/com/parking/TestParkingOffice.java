package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
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
        assertEquals("City Parking", office.getParkingOfficeName());
        assertEquals(officeAddress, office.getParkingOfficeAddress());
    }

    @Test
    public void testGetParkingOfficeName() {
        assertEquals("City Parking", office.getParkingOfficeName());
    }

    @Test
    public void testGetParkingOfficeAddress() {
        assertEquals(officeAddress, office.getParkingOfficeAddress());
    }

    @Test
    public void testRegisterCustomerWithDetails() {
        Address customerAddress = new Address("123 Hello St", "Apt 201", "Boston", "MA", "67890");
        Customer customer = office.register("James McKenna", customerAddress, "000-1234");

        assertNotNull(customer);
        assertEquals("James McKenna", customer.getName());
        assertEquals("000-1234", customer.getPhoneNumber());
        assertTrue(customer.getCustomerId().startsWith("Customer"));
        assertEquals(1, office.getCustomers().size());
    }

    @Test
    public void testRegisterCustomerObject() {
        Address customerAddress = new Address("456 Oak St", null, "Boston", "MA", "67891");
        Customer customer = new Customer("CUST001", "Jane Smith", customerAddress, "555-5678");

        office.register(customer);

        assertTrue(office.getCustomers().contains(customer));
    }

    @Test
    public void testRegisterCustomerObjectNoDuplicates() {
        Address customerAddress = new Address("456 Oak St", null, "Boston", "MA", "67891");
        Customer customer = new Customer("CUST001", "Jane Smith", customerAddress, "555-5678");

        office.register(customer);
        office.register(customer); // Try to register again

        assertEquals(1, office.getCustomers().size());
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
        assertEquals(2, office.getCustomers().size());
    }

    @Test
    public void testRegisterCarReturnsPermit() {
        Address customerAddress = new Address("789 Standing St", null, "Boston", "MA", "62704");
        Customer customer = office.register("Alice Johnson", customerAddress, "000-5555");
        Car car = customer.register("CAR-A01", CarType.SUV);

        ParkingPermit permit = office.register(car);

        assertNotNull(permit);
        assertNotNull(permit.getId());
        assertEquals(car, permit.getVehicle());
        assertEquals("CAR-A01", permit.getVehicle().getLicense());
        assertNotNull(permit.getExpirationDate());
        assertNotNull(permit.getRegistrationDate());
    }

    @Test
    public void testRegisterCarWithCustomExpiration() {
        Address customerAddress = new Address("789 Standing St", null, "Boston", "MA", "62704");
        Customer customer = office.register("Alice Johnson", customerAddress, "000-5555");
        Car car = customer.register("CAR-A01", CarType.SUV);

        Calendar customExpiration = Calendar.getInstance();
        customExpiration.set(2027, Calendar.DECEMBER, 31);

        ParkingPermit permit = office.register(car, customExpiration);

        assertEquals(customExpiration, permit.getExpirationDate());
    }

    @Test
    public void testRegisterMultipleCarsForCustomer() {
        Address customerAddress = new Address("321 Maple St", null, "Boston", "MA", "62705");
        Customer customer = office.register("Bob Williams", customerAddress, "000-6666");

        Car car1 = customer.register("CAR-B01", CarType.COMPACT);
        Car car2 = customer.register("CAR-B02", CarType.SUV);

        ParkingPermit permit1 = office.register(car1);
        ParkingPermit permit2 = office.register(car2);

        assertEquals(2, customer.getRegisteredCars().size());
        assertNotNull(permit1);
        assertNotNull(permit2);
        assertNotEquals(permit1.getId(), permit2.getId());
    }

    @Test
    public void testPark() {
        Address customerAddress = new Address("555 Cedar St", null, "Boston", "MA", "62706");
        Customer customer = office.register("Charlie Brown", customerAddress, "000-7777");
        Car car = customer.register("CAR-C01", CarType.COMPACT);
        ParkingPermit permit = office.register(car);

        Address lotAddress = new Address("200 Park Ave", null, "Boston", "MA", "62708");
        ParkingLot lot = new ParkingLot("Lot-001", lotAddress, 50);
        office.addLot(lot);

        Calendar parkingDate = Calendar.getInstance();
        parkingDate.set(2025, Calendar.NOVEMBER, 14);

        ParkingTransaction transaction = office.park(parkingDate, permit, lot);

        assertNotNull(transaction);
        assertEquals(parkingDate, transaction.getTransactionDate());
        assertEquals(permit, transaction.getPermit());
        assertEquals(lot, transaction.getLot());
        assertNotNull(transaction.getFeeCharged());
    }

    @Test
    public void testGetParkingChargesForPermit() {
        Address customerAddress = new Address("777 Spruce St", null, "Boston", "MA", "62709");
        Customer customer = office.register("Emma Davis", customerAddress, "000-9999");
        Car car = customer.register("CAR-D01", CarType.COMPACT);
        ParkingPermit permit = office.register(car);

        Address lotAddress = new Address("200 Park Ave", null, "Boston", "MA", "62708");
        ParkingLot lot = new ParkingLot("Lot-001", lotAddress, 50);
        office.addLot(lot);

        Calendar date = Calendar.getInstance();
        office.park(date, permit, lot);
        office.park(date, permit, lot);
        office.park(date, permit, lot);

        Money charges = office.getParkingCharges(permit);

        assertEquals(1500, charges.getCents()); // 3 * $5.00 = $15.00
    }

    @Test
    public void testGetParkingChargesForCustomer() {
        Address customerAddress = new Address("888 Willow St", null, "Boston", "MA", "62711");
        Customer customer = office.register("Frank Miller", customerAddress, "000-0000");

        Car car1 = customer.register("CAR-E01", CarType.COMPACT);
        Car car2 = customer.register("CAR-E02", CarType.SUV);

        ParkingPermit permit1 = office.register(car1);
        ParkingPermit permit2 = office.register(car2);

        Address lotAddress = new Address("200 Park Ave", null, "Boston", "MA", "62708");
        ParkingLot lot = new ParkingLot("Lot-001", lotAddress, 50);
        office.addLot(lot);

        Calendar date = Calendar.getInstance();
        office.park(date, permit1, lot); // $5.00
        office.park(date, permit2, lot); // $8.00
        office.park(date, permit1, lot); // $5.00

        Money charges = office.getParkingCharges(customer);

        assertEquals(1800, charges.getCents()); // $5 + $8 + $5 = $18.00
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
    public void testAddLot() {
        Address lotAddress = new Address("200 Park Ave", null, "Boston", "MA", "62708");
        ParkingLot lot = new ParkingLot("Lot-001", lotAddress, 50);

        office.addLot(lot);

        assertEquals(1, office.getParkingLots().size());
        assertTrue(office.getParkingLots().contains(lot));
    }

    @Test
    public void testGetPermit() {
        Address customerAddress = new Address("555 Cedar St", null, "Boston", "MA", "62706");
        Customer customer = office.register("Charlie Brown", customerAddress, "000-7777");
        Car car = customer.register("CAR-C01", CarType.COMPACT);
        ParkingPermit permit = office.register(car);

        ParkingPermit found = office.getPermit(permit.getId());

        assertNotNull(found);
        assertEquals(permit.getId(), found.getId());
        assertEquals(permit.getVehicle(), found.getVehicle());
    }

    @Test
    public void testGetPermitNotFound() {
        ParkingPermit found = office.getPermit("NONEXISTENT");
        assertNull(found);
    }

    @Test
    public void testGetPermitIds() {
        Address addr = new Address("123 Main St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", addr, "555-5555");

        Car car1 = customer.register("LICENSE-1", CarType.COMPACT);
        Car car2 = customer.register("LICENSE-2", CarType.SUV);
        Car car3 = customer.register("LICENSE-3", CarType.COMPACT);

        ParkingPermit permit1 = office.register(car1);
        ParkingPermit permit2 = office.register(car2);
        ParkingPermit permit3 = office.register(car3);

        List<String> permitIds = office.getPermitIds();

        assertNotNull(permitIds);
        assertEquals(3, permitIds.size());
        assertTrue(permitIds.contains(permit1.getId()));
        assertTrue(permitIds.contains(permit2.getId()));
        assertTrue(permitIds.contains(permit3.getId()));
    }

    @Test
    public void testGetPermitIdsEmpty() {
        List<String> permitIds = office.getPermitIds();

        assertNotNull(permitIds);
        assertEquals(0, permitIds.size());
    }

    @Test
    public void testGetPermitsForCustomer() {
        Address addr1 = new Address("123 First St", null, "Boston", "MA", "12345");
        Address addr2 = new Address("456 Second St", null, "Boston", "MA", "12346");

        Customer customer1 = office.register("Alice", addr1, "111-1111");
        Customer customer2 = office.register("Bob", addr2, "222-2222");

        Car car1 = customer1.register("LICENSE-1", CarType.COMPACT);
        Car car2 = customer1.register("LICENSE-2", CarType.SUV);
        Car car3 = customer2.register("LICENSE-3", CarType.COMPACT);

        ParkingPermit permit1 = office.register(car1);
        ParkingPermit permit2 = office.register(car2);
        ParkingPermit permit3 = office.register(car3);

        List<ParkingPermit> customer1Permits = office.getPermitsForCustomer(customer1);
        List<ParkingPermit> customer2Permits = office.getPermitsForCustomer(customer2);

        assertNotNull(customer1Permits);
        assertEquals(2, customer1Permits.size());
        assertTrue(customer1Permits.contains(permit1));
        assertTrue(customer1Permits.contains(permit2));
        assertFalse(customer1Permits.contains(permit3));

        assertNotNull(customer2Permits);
        assertEquals(1, customer2Permits.size());
        assertTrue(customer2Permits.contains(permit3));
    }

    @Test
    public void testGetPermitsForCustomerWithNoCars() {
        Address addr = new Address("123 Main St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", addr, "555-5555");

        List<ParkingPermit> permitIds = office.getPermitsForCustomer(customer);

        assertNotNull(permitIds);
        assertEquals(0, permitIds.size());
    }

    @Test
    public void testToStringWithData() {
        Address customerAddress = new Address("999 Last St", null, "Boston", "MA", "62716");
        Customer customer = office.register("Iris Anderson", customerAddress, "000-3333");
        Car car = customer.register("CAR-G01", CarType.COMPACT);
        office.register(car);

        Address lotAddress = new Address("600 Final Ave", null, "Boston", "MA", "62717");
        ParkingLot lot = new ParkingLot("Lot-004", lotAddress, 25);
        office.addLot(lot);

        String result = office.toString();
        assertTrue(result.contains("City Parking"));
        assertTrue(result.contains("Customers: 1"));
        assertTrue(result.contains("Lots: 1"));
    }

    @Test
    public void testNoParkingChargesForNewCustomer() {
        Address customerAddress = new Address("123 Test St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", customerAddress, "555-5555");

        Money charges = office.getParkingCharges(customer);

        assertEquals(0, charges.getCents());
    }

    @Test
    public void testNoParkingChargesForNewPermit() {
        Address customerAddress = new Address("123 Test St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", customerAddress, "555-5555");
        Car car = customer.register("TEST123", CarType.COMPACT);
        ParkingPermit permit = office.register(car);

        Money charges = office.getParkingCharges(permit);

        assertEquals(0, charges.getCents());
    }

    @Test
    public void testDifferentCarTypeCharges() {
        Address customerAddress = new Address("333 Double St", null, "Boston", "MA", "62718");
        Customer customer = office.register("Jack Black", customerAddress, "000-4444");

        Car compactCar = customer.register("COMP01", CarType.COMPACT);
        Car suvCar = customer.register("SUV01", CarType.SUV);

        ParkingPermit compactPermit = office.register(compactCar);
        ParkingPermit suvPermit = office.register(suvCar);

        Address lotAddress = new Address("200 Park Ave", null, "Boston", "MA", "62708");
        ParkingLot lot = new ParkingLot("Lot-005", lotAddress, 100);
        office.addLot(lot);

        Calendar date = Calendar.getInstance();
        office.park(date, compactPermit, lot);
        office.park(date, suvPermit, lot);

        Money compactCharges = office.getParkingCharges(compactPermit);
        Money suvCharges = office.getParkingCharges(suvPermit);

        assertEquals(500, compactCharges.getCents()); // $5.00 for COMPACT
        assertEquals(800, suvCharges.getCents()); // $8.00 for SUV
    }

    @Test
    public void testPermitExpirationIsOneYear() {
        Address customerAddress = new Address("123 Test St", null, "Boston", "MA", "12345");
        Customer customer = office.register("Test User", customerAddress, "555-5555");
        Car car = customer.register("TEST123", CarType.COMPACT);

        Calendar beforeRegistration = Calendar.getInstance();
        ParkingPermit permit = office.register(car);
        Calendar afterRegistration = Calendar.getInstance();

        Calendar expectedExpiration = Calendar.getInstance();
        expectedExpiration.add(Calendar.YEAR, 1);

        Calendar permitExpiration = permit.getExpirationDate();

        // Check that expiration is approximately 1 year from now (within 1 day tolerance)
        long daysDifference = Math.abs(
                (permitExpiration.getTimeInMillis() - expectedExpiration.getTimeInMillis())
                        / (1000 * 60 * 60 * 24)
        );

        assertTrue(daysDifference <= 1,
                "Permit expiration should be approximately 1 year from registration");
    }
}

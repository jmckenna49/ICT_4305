package com.parking;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestAddress {
    @Test
    public void testAddressConstructor() {
        Address addressTest = new Address("1243 St",
                "Apt 201",
                "Boston", "MA",
                "12345");
        assertEquals("1243 St", addressTest.getStreetAddress1());
        assertEquals("Apt 201", addressTest.getStreetAddress2());
        assertEquals("Boston", addressTest.getCity());
        assertEquals("MA", addressTest.getState());
        assertEquals("12345", addressTest.getZipcode());
    }

    @Test
    public void testGetAddressInfoWithSecondLine() {
        Address addressTest = new Address("1243 St", "Apt 201",
                "Boston", "MA", "12345");

        String info = addressTest.getAddressInfo();

        assertTrue(info.contains("1243 St"));
        assertTrue(info.contains("Apt 201"));
        assertTrue(info.contains("Boston, MA 12345"));
    }

    @Test
    public void testGetAddressInfoWithoutSecondLine() {
        Address addressTest = new Address("1243 St", "",
                "Boston", "MA", "12345");

        String info = addressTest.getAddressInfo();

        assertTrue(info.contains("1243 St"));
        assertFalse(info.contains("Apt"));
        assertTrue(info.contains("Boston, MA 12345"));
    }

    @Test
    public void testGetAddressInfoWithNullSecondLine() {
        Address addressTest = new Address("1243 St", null,
                "Boston", "MA", "12345");

        String info = addressTest.getAddressInfo();

        assertTrue(info.contains("1243 St"));
        assertTrue(info.contains("Boston, MA 12345"));
    }

    @Test
    public void testAddressEquals() {
        Address addr1 = new Address("1243 St", "Apt 201", "Boston", "MA", "12345");
        Address addr2 = new Address("1243 St", "Apt 201", "Boston", "MA", "12345");
        Address addr3 = new Address("999 Main St", "Apt 201", "Boston", "MA", "12345");

        assertEquals(addr1, addr2);
        assertNotEquals(addr1, addr3);
        assertEquals(addr1, addr1); // reflexive
    }

    @Test
    public void testAddressHashCode() {
        Address addr1 = new Address("1243 St", "Apt 201", "Boston", "MA", "12345");
        Address addr2 = new Address("1243 St", "Apt 201", "Boston", "MA", "12345");

        assertEquals(addr1.hashCode(), addr2.hashCode());
    }

    @Test
    public void testAddressEqualsWithDifferentFields() {
        Address base = new Address("1243 St", "Apt 201", "Boston", "MA", "12345");
        Address diffStreet1 = new Address("999 St", "Apt 201", "Boston", "MA", "12345");
        Address diffStreet2 = new Address("1243 St", "Apt 999", "Boston", "MA", "12345");
        Address diffCity = new Address("1243 St", "Apt 201", "Cambridge", "MA", "12345");
        Address diffState = new Address("1243 St", "Apt 201", "Boston", "NY", "12345");
        Address diffZip = new Address("1243 St", "Apt 201", "Boston", "MA", "99999");

        assertNotEquals(base, diffStreet1);
        assertNotEquals(base, diffStreet2);
        assertNotEquals(base, diffCity);
        assertNotEquals(base, diffState);
        assertNotEquals(base, diffZip);
    }

    @Test
    public void testAddressEqualsWithNull() {
        Address addr = new Address("1243 St", "Apt 201", "Boston", "MA", "12345");
        assertNotEquals(null, addr);
    }

    @Test
    public void testAddressEqualsWithDifferentClass() {
        Address addr = new Address("1243 St", "Apt 201", "Boston", "MA", "12345");
        assertFalse(addr.equals("Not an address"));
    }
}
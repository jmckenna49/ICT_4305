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
}

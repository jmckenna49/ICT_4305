package com.dates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyDateTest {
    /**
     * This test case will test the default constructor with no arguments passed in
     */
    @Test
    void testMyDateNoArgs() {
        MyDate myDate = new MyDate();
        assertEquals(1, myDate.getDay());
        assertEquals(1, myDate.getMonth());
        assertEquals(1970, myDate.getYear());
    }

    @Test
    void testMyDateExistingDate() {
        MyDate myExistingDate = new MyDate( 7,7,2077 );
        MyDate myDate = new MyDate( myExistingDate);
        assertEquals(7, myDate.getDay());
        assertEquals(7, myDate.getMonth());
        assertEquals(2077, myDate.getYear());
        assertEquals(7, myExistingDate.getDay());
        assertEquals(7, myExistingDate.getMonth());
        assertEquals(2077, myExistingDate.getYear());
    }

    @Test
    void testMyDateIsLeapYear() {
        assertTrue(MyDate.isLeapYear(2024));
        assertFalse(MyDate.isLeapYear(2025));
    }
    @Test
    void testMyDateLastDayOfMonth() {
        assertEquals(28, MyDate.getLastDayOfMonth(2,2025));
        assertEquals(31, MyDate.getLastDayOfMonth(10,2025));
    }

}

package com.parking;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMoney {

    @Test
    public void testMoneyConstructor() {
        Money money = new Money(500);
        assertEquals(500, money.getCents());
    }

    @Test
    public void testGetDollars() {
        Money money = new Money(500);
        assertEquals(5.00, money.getDollars(), 0.001);
    }

    @Test
    public void testGetDollarsZero() {
        Money money = new Money(0);
        assertEquals(0.00, money.getDollars(), 0.001);
    }

    @Test
    public void testGetDollarsLargeAmount() {
        Money money = new Money(123456);
        assertEquals(1234.56, money.getDollars(), 0.001);
    }

    @Test
    public void testAdd() {
        Money money1 = new Money(500);
        Money money2 = new Money(300);
        Money result = money1.add(money2);

        assertEquals(800, result.getCents());
        assertEquals(8.00, result.getDollars(), 0.001);
    }

    @Test
    public void testAddZero() {
        Money money1 = new Money(500);
        Money money2 = new Money(0);
        Money result = money1.add(money2);

        assertEquals(500, result.getCents());
    }

    @Test
    public void testAddMultiple() {
        Money money1 = new Money(100);
        Money money2 = new Money(200);
        Money money3 = new Money(300);

        Money result = money1.add(money2).add(money3);
        assertEquals(600, result.getCents());
    }

    @Test
    public void testToString() {
        Money money = new Money(500);
        assertEquals("$5.00", money.toString());
    }

    @Test
    public void testToStringZero() {
        Money money = new Money(0);
        assertEquals("$0.00", money.toString());
    }

    @Test
    public void testToStringLargeAmount() {
        Money money = new Money(123456);
        assertEquals("$1234.56", money.toString());
    }

    @Test
    public void testToStringWithCents() {
        Money money = new Money(1005);
        assertEquals("$10.05", money.toString());
    }
}
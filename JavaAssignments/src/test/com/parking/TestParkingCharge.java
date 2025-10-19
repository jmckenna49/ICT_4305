package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;

public class TestParkingCharge {

    private Money amount;
    private Instant timestamp;
    private ParkingCharge charge;

    @BeforeEach
    public void setUp() {
        amount = new Money(500);
        timestamp = Instant.parse("2025-01-15T10:30:00Z");
        charge = new ParkingCharge("PERMIT-123", "LOT-001", timestamp, amount);
    }

    @Test
    public void testParkingChargeConstructor() {
        assertNotNull(charge);
        assertEquals("PERMIT-123", charge.getPermitId());
        assertEquals("LOT-001", charge.getLotId());
        assertEquals(timestamp, charge.getIncurred());
        assertEquals(amount, charge.getAmount());
    }

    @Test
    public void testGetPermitId() {
        assertEquals("PERMIT-123", charge.getPermitId());
    }

    @Test
    public void testGetLotId() {
        assertEquals("LOT-001", charge.getLotId());
    }

    @Test
    public void testGetIncurred() {
        assertEquals(timestamp, charge.getIncurred());
    }

    @Test
    public void testGetAmount() {
        assertEquals(amount, charge.getAmount());
        assertEquals(500, charge.getAmount().getCents());
        assertEquals(5.00, charge.getAmount().getDollars(), 0.001);
    }

    @Test
    public void testToString() {
        String result = charge.toString();
        assertTrue(result.contains("PERMIT-123"));
        assertTrue(result.contains("LOT-001"));
        assertTrue(result.contains("$5.00"));
    }

    @Test
    public void testDifferentPermitIds() {
        ParkingCharge charge1 = new ParkingCharge("PERMIT-AAA", "LOT-001", timestamp, amount);
        ParkingCharge charge2 = new ParkingCharge("PERMIT-BBB", "LOT-001", timestamp, amount);

        assertNotEquals(charge1.getPermitId(), charge2.getPermitId());
    }

    @Test
    public void testDifferentLotIds() {
        ParkingCharge charge1 = new ParkingCharge("PERMIT-123", "LOT-DOWNTOWN", timestamp, amount);
        ParkingCharge charge2 = new ParkingCharge("PERMIT-123", "LOT-AIRPORT", timestamp, amount);

        assertNotEquals(charge1.getLotId(), charge2.getLotId());
    }

    @Test
    public void testDifferentTimestamps() {
        Instant time1 = Instant.now();
        Instant time2 = time1.plusSeconds(3600);

        ParkingCharge charge1 = new ParkingCharge("PERMIT-123", "LOT-001", time1, amount);
        ParkingCharge charge2 = new ParkingCharge("PERMIT-123", "LOT-001", time2, amount);

        assertNotEquals(charge1.getIncurred(), charge2.getIncurred());
    }

    @Test
    public void testDifferentAmounts() {
        Money amount1 = new Money(500);
        Money amount2 = new Money(1000);

        ParkingCharge charge1 = new ParkingCharge("PERMIT-123", "LOT-001", timestamp, amount1);
        ParkingCharge charge2 = new ParkingCharge("PERMIT-123", "LOT-001", timestamp, amount2);

        assertNotEquals(charge1.getAmount().getCents(), charge2.getAmount().getCents());
    }

    @Test
    public void testChargeWithZeroAmount() {
        Money zeroAmount = new Money(0);
        ParkingCharge freeCharge = new ParkingCharge("PERMIT-FREE", "LOT-FREE", timestamp, zeroAmount);

        assertEquals(0, freeCharge.getAmount().getCents());
        assertEquals("$0.00", freeCharge.getAmount().toString());
    }

    @Test
    public void testChargeWithLargeAmount() {
        Money largeAmount = new Money(999999);
        ParkingCharge expensiveCharge = new ParkingCharge("PERMIT-VIP", "LOT-VIP", timestamp, largeAmount);

        assertEquals(999999, expensiveCharge.getAmount().getCents());
        assertEquals(9999.99, expensiveCharge.getAmount().getDollars(), 0.001);
    }
}

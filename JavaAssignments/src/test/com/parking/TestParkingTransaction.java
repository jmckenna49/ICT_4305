package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;

public class TestParkingTransaction {

    private Calendar transactionDate;
    private ParkingPermit permit;
    private ParkingLot lot;
    private Money feeCharged;
    private ParkingTransaction transaction;

    @BeforeEach
    public void setUp() {
        // Create a car and permit
        Car car = new Car("ABC123", CarType.COMPACT, "Customer0");
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.set(2025, Calendar.JANUARY, 1);
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.set(2026, Calendar.JANUARY, 1);
        permit = new ParkingPermit("PERMIT0", car, expirationDate, registrationDate);

        // Create a parking lot
        Address lotAddress = new Address("100 Park Ave", null, "Boston", "MA", "12345");
        lot = new ParkingLot("LOT1", lotAddress, 50);

        // Create transaction date and fee
        transactionDate = Calendar.getInstance();
        transactionDate.set(2025, Calendar.NOVEMBER, 14);
        feeCharged = new Money(500);

        transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
    }

    @Test
    public void testParkingTransactionConstructor() {
        assertNotNull(transaction);
        assertEquals(transactionDate, transaction.getTransactionDate());
        assertEquals(permit, transaction.getPermit());
        assertEquals(lot, transaction.getLot());
        assertEquals(feeCharged, transaction.getFeeCharged());
    }

    @Test
    public void testGetTransactionDate() {
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    public void testGetPermit() {
        assertEquals(permit, transaction.getPermit());
        assertEquals("PERMIT0", transaction.getPermit().getId());
    }

    @Test
    public void testGetLot() {
        assertEquals(lot, transaction.getLot());
        assertEquals("LOT1", transaction.getLot().getLotId());
    }

    @Test
    public void testGetFeeCharged() {
        assertEquals(feeCharged, transaction.getFeeCharged());
        assertEquals(500, transaction.getFeeCharged().getCents());
        assertEquals(5.00, transaction.getFeeCharged().getDollars(), 0.001);
    }

    @Test
    public void testToString() {
        String result = transaction.toString();

        assertTrue(result.contains("PERMIT0"));
        assertTrue(result.contains("LOT1"));
        assertTrue(result.contains("$5.00"));
        assertTrue(result.contains("2025-11-14"));
    }

    @Test
    public void testTransactionWithSUV() {
        Car suvCar = new Car("SUV123", CarType.SUV, "Customer1");
        Calendar regDate = Calendar.getInstance();
        Calendar expDate = Calendar.getInstance();
        expDate.add(Calendar.YEAR, 1);
        ParkingPermit suvPermit = new ParkingPermit("PERMIT-SUV", suvCar, expDate, regDate);

        Money suvFee = new Money(800);
        ParkingTransaction suvTransaction = new ParkingTransaction(transactionDate, suvPermit, lot, suvFee);

        assertEquals(800, suvTransaction.getFeeCharged().getCents());
        assertEquals(CarType.SUV, suvTransaction.getPermit().getVehicle().getType());
    }

    @Test
    public void testTransactionEquals() {
        ParkingTransaction transaction1 = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        ParkingTransaction transaction2 = new ParkingTransaction(transactionDate, permit, lot, feeCharged);

        Car differentCar = new Car("XYZ789", CarType.SUV, "Customer1");
        Calendar regDate = Calendar.getInstance();
        Calendar expDate = Calendar.getInstance();
        expDate.add(Calendar.YEAR, 1);
        ParkingPermit differentPermit = new ParkingPermit("PERMIT1", differentCar, expDate, regDate);
        ParkingTransaction transaction3 = new ParkingTransaction(transactionDate, differentPermit, lot, feeCharged);

        assertEquals(transaction1, transaction2);
        assertNotEquals(transaction1, transaction3);
        assertEquals(transaction1, transaction1); // reflexive
    }

    @Test
    public void testTransactionHashCode() {
        ParkingTransaction transaction1 = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        ParkingTransaction transaction2 = new ParkingTransaction(transactionDate, permit, lot, feeCharged);

        assertEquals(transaction1.hashCode(), transaction2.hashCode());
    }

    @Test
    public void testTransactionWithDifferentDates() {
        Calendar date1 = Calendar.getInstance();
        date1.set(2025, Calendar.NOVEMBER, 14);
        Calendar date2 = Calendar.getInstance();
        date2.set(2025, Calendar.NOVEMBER, 15);

        ParkingTransaction transaction1 = new ParkingTransaction(date1, permit, lot, feeCharged);
        ParkingTransaction transaction2 = new ParkingTransaction(date2, permit, lot, feeCharged);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    public void testTransactionWithDifferentLots() {
        Address lot2Address = new Address("200 Oak St", null, "Boston", "MA", "12346");
        ParkingLot lot2 = new ParkingLot("LOT2", lot2Address, 75);

        ParkingTransaction transaction1 = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        ParkingTransaction transaction2 = new ParkingTransaction(transactionDate, permit, lot2, feeCharged);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    public void testTransactionWithDifferentFees() {
        Money fee1 = new Money(500);
        Money fee2 = new Money(800);

        ParkingTransaction transaction1 = new ParkingTransaction(transactionDate, permit, lot, fee1);
        ParkingTransaction transaction2 = new ParkingTransaction(transactionDate, permit, lot, fee2);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    public void testTransactionWithZeroFee() {
        Money zeroFee = new Money(0);
        ParkingTransaction freeTransaction = new ParkingTransaction(transactionDate, permit, lot, zeroFee);

        assertEquals(0, freeTransaction.getFeeCharged().getCents());
    }

    @Test
    public void testTransactionEqualsWithNull() {
        assertNotEquals(null, transaction);
    }

    @Test
    public void testTransactionEqualsWithDifferentClass() {
        assertFalse(transaction.equals("Not a transaction"));
    }
}

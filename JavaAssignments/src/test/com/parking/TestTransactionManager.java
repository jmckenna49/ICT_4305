package com.parking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Calendar;

public class TestTransactionManager {

    private TransactionManager transactionManager;
    private Customer customer;
    private Car compactCar;
    private Car suvCar;
    private ParkingPermit compactPermit;
    private ParkingPermit suvPermit;
    private ParkingLot lot;

    @BeforeEach
    public void setUp() {
        transactionManager = new TransactionManager();

        Address customerAddress = new Address("123 Main St", "Apt 1", "Boston", "MA", "12345");
        customer = new Customer("Customer0", "John Doe", customerAddress, "555-1234");

        compactCar = new Car("COMP123", CarType.COMPACT, customer.getCustomerId());
        suvCar = new Car("SUV456", CarType.SUV, customer.getCustomerId());

        Calendar regDate = Calendar.getInstance();
        Calendar expDate = Calendar.getInstance();
        expDate.add(Calendar.YEAR, 1);

        compactPermit = new ParkingPermit("PERMIT0", compactCar, expDate, regDate);
        suvPermit = new ParkingPermit("PERMIT1", suvCar, expDate, regDate);

        Address lotAddress = new Address("100 Park Ave", null, "Boston", "MA", "12345");
        lot = new ParkingLot("LOT1", lotAddress, 100);
    }

    @Test
    public void testParkCompactCar() {
        Calendar parkingDate = Calendar.getInstance();
        ParkingTransaction transaction = transactionManager.park(parkingDate, compactPermit, lot);

        assertNotNull(transaction);
        assertEquals(parkingDate, transaction.getTransactionDate());
        assertEquals(compactPermit, transaction.getPermit());
        assertEquals(lot, transaction.getLot());
        assertEquals(500, transaction.getFeeCharged().getCents()); // $5.00 for COMPACT
    }

    @Test
    public void testParkSUVCar() {
        Calendar parkingDate = Calendar.getInstance();
        ParkingTransaction transaction = transactionManager.park(parkingDate, suvPermit, lot);

        assertNotNull(transaction);
        assertEquals(800, transaction.getFeeCharged().getCents()); // $8.00 for SUV
    }

    @Test
    public void testParkMultipleTimes() {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date2.add(Calendar.DAY_OF_MONTH, 1);

        ParkingTransaction transaction1 = transactionManager.park(date1, compactPermit, lot);
        ParkingTransaction transaction2 = transactionManager.park(date2, compactPermit, lot);

        assertNotNull(transaction1);
        assertNotNull(transaction2);
        assertEquals(2, transactionManager.getAllTransactions().size());
    }

    @Test
    public void testGetTransactionsForPermit() {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date2.add(Calendar.DAY_OF_MONTH, 1);
        Calendar date3 = Calendar.getInstance();
        date3.add(Calendar.DAY_OF_MONTH, 2);

        transactionManager.park(date1, compactPermit, lot);
        transactionManager.park(date2, compactPermit, lot);
        transactionManager.park(date3, suvPermit, lot);

        ArrayList<ParkingTransaction> compactTransactions =
                transactionManager.getTransactionsForPermit(compactPermit);
        ArrayList<ParkingTransaction> suvTransactions =
                transactionManager.getTransactionsForPermit(suvPermit);

        assertEquals(2, compactTransactions.size());
        assertEquals(1, suvTransactions.size());
    }

    @Test
    public void testGetTransactionsForCustomer() {
        Address addr2 = new Address("456 Oak St", null, "Boston", "MA", "12346");
        Customer customer2 = new Customer("Customer1", "Jane Smith", addr2, "555-5678");
        Car car3 = new Car("DEF789", CarType.COMPACT, customer2.getCustomerId());

        Calendar regDate = Calendar.getInstance();
        Calendar expDate = Calendar.getInstance();
        expDate.add(Calendar.YEAR, 1);
        ParkingPermit permit3 = new ParkingPermit("PERMIT2", car3, expDate, regDate);

        Calendar date = Calendar.getInstance();

        transactionManager.park(date, compactPermit, lot);
        transactionManager.park(date, suvPermit, lot);
        transactionManager.park(date, permit3, lot);

        ArrayList<ParkingTransaction> customer1Transactions =
                transactionManager.getTransactionsForCustomer(customer);
        ArrayList<ParkingTransaction> customer2Transactions =
                transactionManager.getTransactionsForCustomer(customer2);

        assertEquals(2, customer1Transactions.size());
        assertEquals(1, customer2Transactions.size());
    }

    @Test
    public void testGetParkingChargesForPermit() {
        Calendar date = Calendar.getInstance();

        transactionManager.park(date, compactPermit, lot);
        transactionManager.park(date, compactPermit, lot);
        transactionManager.park(date, compactPermit, lot);

        Money charges = transactionManager.getParkingCharges(compactPermit);

        assertEquals(1500, charges.getCents()); // 3 * $5.00 = $15.00
    }

    @Test
    public void testGetParkingChargesForLicensePlate() {
        Calendar date = Calendar.getInstance();

        transactionManager.park(date, compactPermit, lot);
        transactionManager.park(date, compactPermit, lot);

        Money charges = transactionManager.getParkingCharges("COMP123");

        assertEquals(1000, charges.getCents()); // 2 * $5.00 = $10.00
    }

    @Test
    public void testGetParkingChargesForNonexistentLicensePlate() {
        Money charges = transactionManager.getParkingCharges("NONEXISTENT");

        assertEquals(0, charges.getCents());
    }

    @Test
    public void testGetParkingChargesForCustomer() {
        Calendar date = Calendar.getInstance();

        transactionManager.park(date, compactPermit, lot);
        transactionManager.park(date, suvPermit, lot);
        transactionManager.park(date, compactPermit, lot);

        Money charges = transactionManager.getParkingCharges(customer);

        // 2 * $5.00 + 1 * $8.00 = $18.00
        assertEquals(1800, charges.getCents());
    }

    @Test
    public void testGetParkingChargesForCustomerWithNoTransactions() {
        Money charges = transactionManager.getParkingCharges(customer);
        assertEquals(0, charges.getCents());
    }

    @Test
    public void testGetAllTransactions() {
        Calendar date = Calendar.getInstance();

        transactionManager.park(date, compactPermit, lot);
        transactionManager.park(date, suvPermit, lot);
        transactionManager.park(date, compactPermit, lot);

        ArrayList<ParkingTransaction> allTransactions = transactionManager.getAllTransactions();

        assertEquals(3, allTransactions.size());
    }

    @Test
    public void testVehicleTransactionTracking() {
        Calendar date = Calendar.getInstance();

        transactionManager.park(date, compactPermit, lot);
        transactionManager.park(date, compactPermit, lot);
        transactionManager.park(date, suvPermit, lot);

        Money compactCharges = transactionManager.getParkingCharges("COMP123");
        Money suvCharges = transactionManager.getParkingCharges("SUV456");

        assertEquals(1000, compactCharges.getCents()); // 2 * $5.00
        assertEquals(800, suvCharges.getCents()); // 1 * $8.00
    }

    @Test
    public void testChargeCalculationForCompact() {
        Calendar date = Calendar.getInstance();
        ParkingTransaction transaction = transactionManager.park(date, compactPermit, lot);

        assertEquals(500, transaction.getFeeCharged().getCents());
    }

    @Test
    public void testChargeCalculationForSUV() {
        Calendar date = Calendar.getInstance();
        ParkingTransaction transaction = transactionManager.park(date, suvPermit, lot);

        assertEquals(800, transaction.getFeeCharged().getCents());
    }

    @Test
    public void testMultipleParkingLots() {
        Address lot2Address = new Address("200 Oak St", null, "Boston", "MA", "12346");
        ParkingLot lot2 = new ParkingLot("LOT2", lot2Address, 50);

        Calendar date = Calendar.getInstance();

        ParkingTransaction transaction1 = transactionManager.park(date, compactPermit, lot);
        ParkingTransaction transaction2 = transactionManager.park(date, compactPermit, lot2);

        assertEquals(lot, transaction1.getLot());
        assertEquals(lot2, transaction2.getLot());
    }

    @Test
    public void testGetTransactionsForPermitEmpty() {
        ArrayList<ParkingTransaction> transactions =
                transactionManager.getTransactionsForPermit(compactPermit);

        assertEquals(0, transactions.size());
    }

    @Test
    public void testGetTransactionsForCustomerEmpty() {
        ArrayList<ParkingTransaction> transactions =
                transactionManager.getTransactionsForCustomer(customer);

        assertEquals(0, transactions.size());
    }
}

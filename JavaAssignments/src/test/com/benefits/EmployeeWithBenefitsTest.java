package com.benefits;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeWithBenefitsTest {

    @Test
    void testGetName() {
        Employee employee = new Employee("Alex Johnson", 60000);
        BenefitsPackage benefits = new BenefitsPackage(15, true);
        EmployeeWithBenefits empWithBenefits = new EmployeeWithBenefits(employee, benefits);

        assertEquals("Alex Johnson", empWithBenefits.getName());
    }

    @Test
    void testInitialSalary() {
        Employee employee = new Employee("Alex Johnson", 60000);
        BenefitsPackage benefits = new BenefitsPackage(15, true);
        EmployeeWithBenefits empWithBenefits = new EmployeeWithBenefits(employee, benefits);

        assertEquals(60000, empWithBenefits.getSalary(), 0.01);
    }

    @Test
    void testRaiseSalary() {
        Employee employee = new Employee("Alex Johnson", 60000);
        BenefitsPackage benefits = new BenefitsPackage(15, true);
        EmployeeWithBenefits empWithBenefits = new EmployeeWithBenefits(employee, benefits);

        empWithBenefits.raiseSalary(10); // 10% raise
        assertEquals(66000, empWithBenefits.getSalary(), 0.01);
    }

    @Test
    void testInitialVacationDays() {
        Employee employee = new Employee("Alex Johnson", 60000);
        BenefitsPackage benefits = new BenefitsPackage(15, true);
        EmployeeWithBenefits empWithBenefits = new EmployeeWithBenefits(employee, benefits);

        assertEquals(15, empWithBenefits.getVacationDays());
    }

    @Test
    void testAddVacationDays() {
        Employee employee = new Employee("Alex Johnson", 60000);
        BenefitsPackage benefits = new BenefitsPackage(15, true);
        EmployeeWithBenefits empWithBenefits = new EmployeeWithBenefits(employee, benefits);

        empWithBenefits.addVacationDays(5);
        assertEquals(20, empWithBenefits.getVacationDays());
    }

    @Test
    void testHasHealthInsurance() {
        Employee employee = new Employee("Alex Johnson", 60000);
        BenefitsPackage benefits = new BenefitsPackage(15, true);
        EmployeeWithBenefits empWithBenefits = new EmployeeWithBenefits(employee, benefits);

        assertTrue(empWithBenefits.hasHealthInsurance());
    }
}



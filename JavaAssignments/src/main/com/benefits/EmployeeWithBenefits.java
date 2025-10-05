package com.benefits;

public class EmployeeWithBenefits {
    private Employee employee; // Composition: has-an Employee
    private BenefitsPackage benefits;

    public EmployeeWithBenefits(Employee employee, BenefitsPackage benefits) {
        this.employee = employee;
        this.benefits = benefits;
    }

    public String getName() {
        return employee.getName();
    }

    public double getSalary() {
        return employee.getSalary();
    }

    public int getVacationDays() {
        return benefits.getVacationDays();
    }

    public boolean hasHealthInsurance() {
        return benefits.hasHealthInsurance();
    }

    public void raiseSalary(double byPercent) {
        employee.raiseSalary(byPercent);
    }

    public void addVacationDays(int days) {
        benefits.addVacationDays(days);
    }
}

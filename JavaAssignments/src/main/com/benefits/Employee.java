package com.benefits;

public class Employee {
    private String name;
    private double salary;

    public Employee(String name, double salary)
    {
        this.name = name;
        this.salary = salary;
    }
    public String getName()
    { return name; }
    public double getSalary()
    { return salary; }
    public void raiseSalary(double byPercent)
    { salary += salary * (byPercent / 100); }
};
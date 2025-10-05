package com.benefits;

public class BenefitsPackage {
    private int vacationDays;
    private boolean healthInsurance;

    public BenefitsPackage(int vacationDays, boolean healthInsurance) {
        this.vacationDays = vacationDays;
        this.healthInsurance = healthInsurance;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    public boolean hasHealthInsurance() {
        return healthInsurance;
    }

    public void addVacationDays(int days) {
        vacationDays += days;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 * The Benefits class holds various benefit-related data for an employee, including salary, allowances,
 * and other benefits. It also provides methods for calculating hourly rates and total allowances.
 * 
 * @author Alex Resurreccion
 */
public class Benefits {
    
    // Instance variables representing the employee's benefits and salary details
    private double basicSalary;
    private double grossSemiMonthlyRate;
    private double hourlyRate;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    
     /**
     * Constructor that initializes all the benefit-related fields.
     * 
     * @param basicSalary The basic salary of the employee
     * @param grossSemiMonthlyRate The semi-monthly rate of the employee
     * @param hourlyRate The hourly rate of the employee
     * @param riceSubsidy The rice subsidy allowance for the employee
     * @param phoneAllowance The phone allowance for the employee
     * @param clothingAllowance The clothing allowance for the employee
     */
    public Benefits(double basicSalary, double grossSemiMonthlyRate, double hourlyRate, 
            double riceSubsidy, double phoneAllowance, double clothingAllowance) {
        this.basicSalary =  basicSalary;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
    }
    
    // Default constructor (no arguments), used if the benefits data is not yet set.
    public Benefits(){}
    
    // Getter methods for each benefit field
    public double getBasicSalary() {
        return basicSalary;
    }
    
    public double getGrossSemiMonthlyRate() {
        return grossSemiMonthlyRate;
    }
    
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    public double getRiceSubsidy() {
        return riceSubsidy;
    }
    
    public double getPhoneAllowance() {
        return phoneAllowance;
    }
    
    public double getClothingAllowance() {
        return clothingAllowance;
    }
    
    // Setter methods for each benefit field
    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }
    
    public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) {
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
    }
    
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    public void setRiceSubsidy(double riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }
    
    public void setPhoneAllowance(double phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }
    
    public void setClothingAllowance(double clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }
    
    /**
     * Calculates the employee's hourly rate based on their basic salary. 
     * Assumes the employee works 21 days a month and 8 hours a day.
     * 
     * @return The calculated hourly rate of the employee
     */
    public double calculateHourlyRate(){
        return (this.basicSalary/21)/8;
    }
    
    /**
     * Calculates the total of all allowances (phone, rice, clothing).
     * 
     * @return The total value of the allowances
     */
    public double calculateTotalAllowance(){
        return this.phoneAllowance + this.riceSubsidy + this.clothingAllowance;
    }
}

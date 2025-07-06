/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.*;

/**
 * The Employee class extends the User class and represents an employee in the payroll system.
 * It contains details about the employee, including their status, position, department, leave requests, benefits, 
 * attendance records, and more. The class also allows for handling employee requests and calculating their pay.
 * 
 * @author Alex Resurreccion
 */
public class Employee extends User {
    private String status;
    private String immediateSupervisor;
    private String department;
    private Benefits benefits; // Composition: Employee "HAS-A" Benefits
    
    // Default constructor
    public Employee() {}
    
    // Constructor with employee ID
    public Employee(int employeeID) {
        super(employeeID);
    }

    // Setter and Getter methods for employee details
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }
    
    // Submit Leave Request
    public void submitLeaveRequest(String leaveType, Date startDate, Date endDate, int duration) {
        System.out.println(firstName + " " + lastName + " has submitted a leave request for " + leaveType);
    }
    
    // Getters for employee status and supervisor
    public String getStatus() {
        return status;
    }
    
    public String getImmediateSupervisor() {
        return immediateSupervisor;
    }
    
    // Benefits management: Getter and Setter methods
    public Benefits getBenefits() {
        return benefits;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setImmediateSupervisor(String immediateSupervisor) {
        this.immediateSupervisor = immediateSupervisor;
    }
    
    // Methods to fetch individual benefit components like salary, allowances
    public double getBenefitsBasicSalary(){
        return this.benefits.getBasicSalary();
    }
    
    public double getBenefitsGrossSemiMonthlyRate() {
        return this.benefits.getGrossSemiMonthlyRate();
    }
    
    public double getBenefitsHourlyRate() {
        return this.benefits.getHourlyRate();
    }
    
    public double getBenefitsRiceSubsidy() {
        return this.benefits.getRiceSubsidy();
    }
    
    public double getBenefitsPhoneAllowance() {
        return this.benefits.getPhoneAllowance();
    }
    
    public double getBenefitsClothingAllowance() {
        return this.benefits.getClothingAllowance();
    }
    
    public void setBenefits(Benefits benefit){
        this.benefits = benefit;
    }
    
    // Setters for individual benefit components
    public void setBenefitsBasicSalary(double basicSalary) {
        this.benefits.setBasicSalary(basicSalary);
    }
    
    public void setBenefitsGrossSemiMonthlyRate(double grossSemiMonthlyRate) {
        this.benefits.setGrossSemiMonthlyRate(grossSemiMonthlyRate);
    }
    
    public void setBenefitsHourlyRate(double hourlyRate) {
        this.benefits.setHourlyRate(hourlyRate);
    }
    
    public void setBenefitsRiceSubsidy(double riceSubsidy) {
        this.benefits.setRiceSubsidy(riceSubsidy);
    }
    
    public void setBenefitsPhoneAllowance(double phoneAllowance) {
        this.benefits.setPhoneAllowance(phoneAllowance);
    }
    
    public void setBenefitsClothingAllowance(double clothingAllowance) {
        this.benefits.setClothingAllowance(clothingAllowance);
    }
    
    // Calculate the total benefits provided to the employee
    public double calculateTotalBenefits(){
        return this.benefits.getClothingAllowance() + 
                this.benefits.getPhoneAllowance() + 
                this.benefits.getRiceSubsidy();
    }
}

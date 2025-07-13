/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * This is the abstract User class representing the common properties and behaviors
 * for any type of user in the system. It can be extended by specific user types
 * like SystemAdministrator, HRPersonnel, and Employee.
 * 
 * @author Alex Resurreccion
 */
public abstract class User {
    private String status;
    private int immediateSupervisor;
    private Benefits benefits; // Composition: Employee "HAS-A" Benefits
    protected int employeeID;
    protected String password;
    protected String role;
    protected String firstName;
    protected String lastName;
    protected Date birthday;
    protected String position;
    protected String phoneNumber;
    protected String sssNumber;
    protected String philhealthNumber;
    protected String tinNumber;
    protected String pagibigNumber;
    protected String department;
    protected Date dateHired;
    private Address address;  // Composition: Employee "HAS-A" Address
    
    public User(){}
    
    //Constuctor
    public User(int employeeID){
        this.employeeID = employeeID;
    }

    public Date getDateHired() {
        return dateHired;
    }
    
    // Encapsulation - Getters and Setters
    
    public String getStatus() {
    return status;
    }
    
    public int getImmediateSupervisor() {
        return immediateSupervisor;
    }
    
    // Benefits management: Getter and Setter methods
    public Benefits getBenefits() {
        return benefits;
    }
    
    public int getEmployeeID() {
        return employeeID;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public Date getBirthday() {
        return birthday;
    }
    
    public String getPosition() {
        return position;
    }
    
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getSssNumber() {
        return sssNumber;
    }
    
    public String getPhilhealthNumber() {
        return philhealthNumber;
    }
    
    public String getTinNumber() {
        return tinNumber;
    }
    
    public String getPagibigNumber() {
        return pagibigNumber;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public Address getAddress() {
        return address;
    }
       
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setImmediateSupervisor(int immediateSupervisor) {
        this.immediateSupervisor = immediateSupervisor;
    }
    
    public void setDateHired(Date dateHired) {
        this.dateHired = dateHired;
    }
    
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setSssNumber(String sssNumber) {
        this.sssNumber =  sssNumber;
    }
    
    public void setPhilhealthNumber(String philhealthNumber) {
        this.philhealthNumber = philhealthNumber;
    }
    
    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }
    
    public void setPagibigNumber(String pagibigNumber) {
        this.pagibigNumber = pagibigNumber;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }

    public void setStreet(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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

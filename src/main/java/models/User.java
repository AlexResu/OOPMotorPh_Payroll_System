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
} 

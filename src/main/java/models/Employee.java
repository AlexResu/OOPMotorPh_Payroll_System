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
    private String department;

    
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
}

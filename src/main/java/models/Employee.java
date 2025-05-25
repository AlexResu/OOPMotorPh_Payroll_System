/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import alex.oopmotorphpayrollsystem.MySQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 * The Employee class extends the User class and represents an employee in the payroll system.
 * It contains details about the employee, including their status, position, department, leave requests, benefits, 
 * attendance records, and more. The class also allows for handling employee requests and calculating their pay.
 * 
 * @author Alex Resurreccion
 */
@Entity
@Table(name = "employees",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "sss_number"),
           @UniqueConstraint(columnNames = "philhealth_number"),
           @UniqueConstraint(columnNames = "tin_number"),
           @UniqueConstraint(columnNames = "pagibig_number")
       }
)
public class Employee extends User {
    @Column(name = "status", length = 255)
    private String status;
    
    @Column(name = "immediate_supervisor", length = 255)
    private String immediateSupervisor;
    
    @Column(name = "department", length = 150)
    private String department;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "basicSalary", column = @Column(name = "basic_salary")),
        @AttributeOverride(name = "grossSemiMonthlyRate", column = @Column(name = "gross_semi_monthly_rate")),
        @AttributeOverride(name = "hourlyRate", column = @Column(name = "hourly_rate")),
        @AttributeOverride(name = "riceSubsidy", column = @Column(name = "rice_subsidy")),
        @AttributeOverride(name = "phoneAllowance", column = @Column(name = "phone_allowance")),
        @AttributeOverride(name = "clothingAllowance", column = @Column(name = "clothing_allowance"))
    })
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
    
    // Methods for Time-In and Time-Out (attendance management)
    public void timeIn() {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
        MySQL mySQL = new MySQL();
        // Insert attendance record into the database
        mySQL.createAttendanceRecord(employeeID, currentDate, currentTime);
        mySQL.close();
    }
    
    public void timeOut() {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
        MySQL mySQL = new MySQL();
        // Update the time-out in the attendance record
        mySQL.updateAttendanceRecord(employeeID, currentDate, currentTime);
        mySQL.close();
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
    
    // Load the employee's leave requests from the database
    public List<LeaveRequest> loadLeaveRequests(){
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getLeaveRequestByEmpNumber(employeeID);
        try { 
            while (result.next()) {
                HRPersonnel approver = new HRPersonnel();
                approver.setEmployeeID(result.getInt("employee_number"));
                approver.setLastName(result.getString("last_name"));
                approver.setFirstName(result.getString("first_name"));
                leaveRequests.add(
                    new LeaveRequest(
                        this,
                        result.getInt("id"),
                        result.getString("leave_type"),
                        result.getDate("date_from"),
                        result.getDate("date_until"),
                        result.getInt("number_of_days"),
                        result.getString("remarks"),
                        approver
                    )
                );
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        mySQL.close();
        return leaveRequests;
    }
    
    // Load leave credits for the employee (emergency, sick, vacation)
    public List<Map<String, Object>> loadLeaveCredits(){
        int maxVacation = 20, maxSick = 15, maxEmergency = 5; 
        List<Map<String, Object>> leaveCredits = new ArrayList<>();

        // Create first dictionary (map)
        
        MySQL mySQL = new MySQL();
        int currentYear = LocalDate.now().getYear();
        ResultSet result = mySQL.getLeaveRequestByEmpNumberAndYear(employeeID, currentYear);
        
        try { 
            if (result.next()) {
                System.out.println(result.getInt("emergency_leave_count"));
                Map<String, Object> el = new HashMap<>();
                el.put("Type", "EL - Emergency Leave");
                el.put("Allowable", maxEmergency); // Hardcoded allowable limit
                el.put("Available", maxEmergency - result.getInt("emergency_leave_count"));
                leaveCredits.add(el);

                // Map for SL
                Map<String, Object> sl = new HashMap<>();
                sl.put("Type", "SL - Sick Leave");
                sl.put("Allowable", maxSick);
                sl.put("Available", maxSick - result.getInt("sick_leave_count"));
                leaveCredits.add(sl);

                // Map for VL
                Map<String, Object> vl = new HashMap<>();
                vl.put("Type", "VL - Vaction Leave");
                vl.put("Allowable", maxVacation);
                vl.put("Available", maxVacation - result.getInt("vacation_leave_count"));
                leaveCredits.add(vl);
            } else {
                Map<String, Object> el = new HashMap<>();
                el.put("Type", "EL - Emergency Leave");
                el.put("Allowable", maxEmergency); 
                el.put("Available", maxEmergency);
                leaveCredits.add(el);
                
                // Map for SL
                Map<String, Object> sl = new HashMap<>();
                sl.put("Type", "SL - Sick Leave");
                sl.put("Allowable", maxSick);
                sl.put("Available", maxSick);
                leaveCredits.add(sl);

                // Map for VL
                Map<String, Object> vl = new HashMap<>();
                vl.put("Type", "VL - Vacation Leave");
                vl.put("Allowable", maxVacation);
                vl.put("Available", maxVacation);
                leaveCredits.add(vl);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        mySQL.close();
        return leaveCredits;
    }
    
    // Load employee's attendance records from the database
    public List<AttendanceRecord> loadAttendanceList(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getAttendanceList(this.employeeID);
        List<AttendanceRecord> attendances = mapAttendance(result);
        mySQL.close();
        return attendances;
    }
    
    // Load attendance records for a specific date range
    public List<AttendanceRecord> loadAttendanceList(
            Date startDate, Date endDate){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getAttendanceList(
                this.employeeID,
                new java.sql.Date(startDate.getTime()), 
                new java.sql.Date(endDate.getTime()));
        List<AttendanceRecord> attendances = mapAttendance(result);
        mySQL.close();
        return attendances;
    }
    
    // Helper method to map attendance records from the ResultSet
    private List<AttendanceRecord> mapAttendance(ResultSet result){
        List<AttendanceRecord> attendance = new ArrayList<>();
        try { 
            while (result.next()) {
                AttendanceRecord record = new AttendanceRecord(
                        result.getInt("employee_number"),
                        result.getDate("date"),
                        result.getTime("time_in"),
                        result.getTime("time_out"));
                attendance.add(record);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return attendance;
    }
    
    // Save employee's updated information to the database
    public boolean save(){
        MySQL mySQL = new MySQL();
        boolean result = mySQL.updateEmployeeSelf(
                getEmployeeID(), getAddress(), getPhoneNumber()) == 1;
        mySQL.close();
        return result;
    }
    
    // Create a new leave request for the employee
    public boolean createLeaveRequest(LeaveRequest leave){
        MySQL mySQL = new MySQL();
        boolean result = mySQL.createLeaveRequest(leave) == 1;
        mySQL.close();
        return result;
    }
    
    // Method to load the employee's payslip based on a date range
    public Payroll loadPayslip(Date startDate, Date endDate){
        Payroll payslip = new Payroll();
        MySQL mySQL = new MySQL();
        // Query the database to retrieve payslip details based on the employee and date range
        ResultSet result = mySQL.loadPayslip(
                new java.sql.Date(startDate.getTime()), 
                new java.sql.Date(endDate.getTime()), 
                this);
        try{
            if(result.next()){
                payslip = mapPayroll(result);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        mySQL.close();
        return payslip;
    }
    
    // Helper method to map the ResultSet data to a Payroll object
    private Payroll mapPayroll(ResultSet result){
        Payroll pr = new Payroll();
        try {
            // Set the attributes of the Payroll object from the ResultSet
            pr.setGrossIncome(result.getInt("gross_income"));
            pr.setHoursWorked(result.getInt("hours_worked"));
            pr.setNetPay(result.getInt("take_home_pay"));
            pr.setOvertimeHours(result.getInt("overtime_hours"));
            pr.setPaymentDate(result.getDate("payment_date"));
            pr.setPayrollID(result.getInt("id"));
            pr.setWeekPeriodEnd(result.getDate("period_end"));
            pr.setWeekPeriodStart(result.getDate("period_start"));
            
            // Initialize a new Deductions object to hold the payroll deductions
            Deductions deduction = new Deductions();
            deduction.setPagIbigDeduction(result.getInt("pagibig_deduction"));
            deduction.setPhilhealthDeduction(result.getInt("philhealth_deduction"));
            deduction.setSssDeduction(result.getInt("sss_deduction"));
            deduction.setTaxDeduction(result.getInt("withholding_tax"));
            pr.setDeductions(deduction);
            
            // Set the employee’s benefits in the payroll (using the Employee instance)
            Employee emp = this;

            // Initialize a Benefits object and set the various benefits based on query results
            Benefits benefit = new Benefits(
                    result.getInt("monthly_rate"),
                    result.getInt("gross_income"),
                    result.getInt("hourly_rate"),
                    result.getInt("rice_subsidy"),
                    result.getInt("phone_allowance"),
                    result.getInt("clothing_allowance")
                );
            emp.setBenefits(benefit);
            pr.setEmployee(emp);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        // Return the populated Payroll object
        return pr;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The Payroll class represents an employee's payroll information.
 * It includes details like hours worked, overtime hours, deductions,
 * gross income, net pay, and more. The class uses composition to 
 * associate deductions with payroll.
 * @author Alex Resurreccion
 */
public class Payroll {
    private String payrollID;
    private Employee employee;
    private Date weekPeriodStart;
    private Date weekPeriodEnd;
    private Deductions deductions; // Composition: Payroll "Has-A" Deductions
    private double hoursWorked;
    private double overtimeHours;
    private double grossIncome;
    private double netPay;
    private Date paymentDate;

    // Getter and Setter Methods
    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public void setGrossIncome(double grossIncome) {
        this.grossIncome = grossIncome;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public double getGrossIncome() {
        return grossIncome;
    }

    public double getNetPay() {
        return netPay;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public String getPayrollID() {
        return payrollID;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Date getWeekPeriodStart() {
        return weekPeriodStart;
    }

    public Date getWeekPeriodEnd() {
        return weekPeriodEnd;
    }

    public Deductions getDeductions() {
        return deductions;
    } 

    public void setPayrollID(String payrollID) {
        this.payrollID = payrollID;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setWeekPeriodStart(Date weekPeriodStart) {
        this.weekPeriodStart = weekPeriodStart;
    }

    public void setWeekPeriodEnd(Date weekPeriodEnd) {
        this.weekPeriodEnd = weekPeriodEnd;
    }

    public void setDeductions(Deductions deductions) {
        this.deductions = deductions;
    }
    
    public Payroll() {}
    
    // Constructor with payroll ID, employee, and pay period start and end
    public Payroll(String payrollID, Employee employee, Date weekPeriodStart, Date weekPeriodEnd) {
        this.payrollID = payrollID;
        this.employee = employee;
        this.weekPeriodStart = weekPeriodStart;
        this.weekPeriodEnd = weekPeriodEnd;
    }
    
    // Constructor for payroll generation based on worked hours and employee
    // This constructor is used by HR Personnel to generate a payroll
    public Payroll(Employee employee, double workedHours, Date weekPeriodStart, Date weekPeriodEnd) {
        this.employee = employee;
        this.hoursWorked = workedHours;
        this.overtimeHours = workedHours > 40 ? workedHours - 40 : 0;
        this.weekPeriodStart = weekPeriodStart;
        this.weekPeriodEnd = weekPeriodEnd;
        checkBenefits();
    }
    
    // Calculate the gross income based on hours worked and hourly rate
    public void calculateGrossPay() {
        this.grossIncome = this.hoursWorked * employee.getBenefitsHourlyRate();
    }
    
    // Calculate the net pay by deducting deductions from the gross income and adding benefits
    public void calculateNetPay() {
        this.netPay =  this.deductions != null 
                ? this.grossIncome + this.employee.calculateTotalBenefits()- deductions.calculateTotalDeductions()
                : 0;
    }
    
    // Ensure deductions can be loaded, returns false if no salary information is set
    public boolean loadAllDeductions(){
        if(employee.getBenefitsBasicSalary() == 0.0){
            // employee not set yet
            return false;
        }
        
        return true;
    }
    
    // Check and ensure that benefits are correctly set based on hours worked
    // If no benefits are applicable (no worked hours or no basic salary), set allowances to 0
    public void checkBenefits(){
        // no benefits if no worked hours
        if(!isLastSunday()){
            this.employee.setBenefitsClothingAllowance(0);
            this.employee.setBenefitsPhoneAllowance(0);
            this.employee.setBenefitsRiceSubsidy(0);
        }
    }
    
    // Generate the deductions based on the employee's basic salary and worked hours
    public void generateDeductions(double totalMonthWorkHours){
        // no deductions if no worked hours
        if(isLastSunday()){
            this.deductions = new Deductions(employee.getBenefitsBasicSalary());
            // Calculate taxable income (gross income minus existing deductions)
            double taxableIncome = this.employee.getBenefitsHourlyRate() * this.hoursWorked - 
                    (this.deductions.getSssDeduction() + this.deductions.getPhilhealthDeduction()
                    + this.deductions.getPagIbigDeduction());
            // Calculate tax deduction based on taxable income
            this.deductions.calculateTaxDeduction(taxableIncome);
        } else {
            this.deductions = new Deductions();
            return;
        }
        
    }
    
    private boolean isLastSunday() {
        // Use Calendar to work with the Date (weekendPeriod)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.weekPeriodEnd);
        
        // Set the calendar to the last day of the month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        
        // Find the last Sunday of the month
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        // Move back to the Sunday before or on the last day of the month
        while (dayOfWeek != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        }
        
        // The date to compare with (last Sunday)
        Date lastSunday = calendar.getTime();
        
        // Check if the weekendPeriod matches the last Sunday
        return this.weekPeriodEnd.equals(lastSunday);
    }
}

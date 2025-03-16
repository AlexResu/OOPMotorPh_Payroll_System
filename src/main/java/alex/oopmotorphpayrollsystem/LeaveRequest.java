/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;

import java.util.Date;

/**
 * LeaveRequest class represents a request for an employee's leave.
 * It stores details about the leave request including the employee,
 * leave type, dates, status, approval details, and reason for the leave.
 * 
 * @author Alex Resurreccion
 */
public class LeaveRequest {
    // Instance variables
    private Employee employee;
    private int leaveID;
    private String leaveType;
    private Date startDate;
    private Date endDate;
    private Date dateCreated;
    private int duration;
    private String leaveStatus;
    private HRPersonnel approvedBy;
    private String reason;
    
    // Default constructor
    public LeaveRequest() {}
    
    // Parameterized constructor for initializing the LeaveRequest object.
    public LeaveRequest(Employee employee, int leaveID, String leaveType, 
            Date startDate, Date endDate, int duration, 
            String leaveStatus, HRPersonnel approvedBy) {
        this.employee = employee;
        this.leaveID = leaveID;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.leaveStatus = leaveStatus;
        this.approvedBy = approvedBy;
    }
    
    // Getter and Setter methods for all instance variables
    public Employee getEmployee() {
        return employee;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
    
    public int getLeaveID() {
        return leaveID;
    }
    
    public String getLeaveType() {
        return leaveType;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public String getLeaveStatus() {
        return leaveStatus;
    }
    
    public HRPersonnel getApprovedBy() {
        return approvedBy;
    }
    
    public String getReason() {
        return reason;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public void setLeaveID(int leaveID) {
        this.leaveID = leaveID;
    }
    
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
    
    public void setApprovedBy(HRPersonnel approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}

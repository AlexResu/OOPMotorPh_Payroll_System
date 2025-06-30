/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;

import dao.EmployeeDao;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;

/**
 * The AttendanceRecord class stores and processes attendance data for an employee.
 * It includes time-in, time-out, and calculates work hours based on the difference between the time-in and time-out values.
 * The class can also retrieve an employee's attendance record from the database.
 * @author Alex Resurreccion
 */
public class AttendanceRecord {
    
    // Instance variables: employee ID, date of attendance, time-in, and time-out
    private int employeeID;
    private Date date;
    private Time timeIn;
    private Time timeOut;
    
    /**
     * Constructor that initializes an AttendanceRecord with all relevant information.
     */
    public AttendanceRecord(int employeeID, Date date, Time timeIn, Time timeOut) {
        this.employeeID = employeeID;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }
    
    /**
     * Constructor that initializes an AttendanceRecord with employee ID and date.
     * It calls initAttendanceRecord to fetch the time-in and time-out values from the database.
     * 
     * @param employeeID The ID of the employee
     * @param date The date of the attendance record
     */
    public AttendanceRecord(int employeeID, Date date) {
        this.employeeID = employeeID;
        this.date = date;
    }
    
    // Getter methods for employee ID, date, time-in, and time-out
    public int getEmployeeId() {
        return employeeID;
    }
    
    public Date getDate() {
        return date;
    }
    
    public Time getTimeIn() {
        return timeIn;
    }
    
    public Time getTimeOut() {
        return timeOut;
    }
    
    // Setter methods for date, time-in, and time-out
    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setTimeIn(Time timeIn) {
        this.timeIn = timeIn;
    }
    
    public void setTimeOut(Time timeout) {
        this.timeOut = timeOut;
    }
    
    /**
     * Calculates the total number of work hours based on the time-in and time-out values.
     * 
     * @return The total work hours (double) for the day
     */
    public double calculateWorkHours() {
        long diff = timeOut.getTime() - timeIn.getTime();
        return diff / (1000.0 * 60 * 60); // Convert milliseconds to hours
    }
    
    /**
     * Checks whether the time-in and time-out have been set for this attendance record.
     * 
     * @return true if both time-in and time-out are set, false otherwise
     */
    public boolean isTimeSet() {
        return timeIn != null && timeOut != null; // Return true if both time-in and time-out are not null
    }
}

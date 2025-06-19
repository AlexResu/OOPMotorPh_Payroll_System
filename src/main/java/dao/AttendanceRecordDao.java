/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import alex.oopmotorphpayrollsystem.AttendanceRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import utils.DbConnection;

/**
 *
 * @author Alex Resurreccion
 */
public class AttendanceRecordDao {
    private Connection conn;
    private Statement statement = null;
    private ResultSet rs = null;
    private PreparedStatement preparedStatement = null;

    // Constructor with custom connection
    public AttendanceRecordDao(Connection conn) {
        this.conn = conn;
    }

    // Constructor with default connection
    public AttendanceRecordDao() {
        this.conn = DbConnection.getConnection();
    }
    

    public AttendanceRecord initAttendanceRecord(int employeeID){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(new Date());
        try {

            // Define the query
            String query = "SELECT * FROM attendance_record "
                    + "WHERE employee_id = ? AND date = ?";
            
            preparedStatement = conn.prepareStatement(query);

            // Setting parameters
            preparedStatement.setInt(1, employeeID);
            preparedStatement.setString(2, formattedDate);

            // Execute the query
            rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                AttendanceRecord attendanceRecord = new AttendanceRecord(
                    rs.getInt("employee_id"), 
                    rs.getDate("date"), 
                    rs.getTime("time_in"), 
                    rs.getTime("time_out")
                );

                return attendanceRecord;
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }   
}


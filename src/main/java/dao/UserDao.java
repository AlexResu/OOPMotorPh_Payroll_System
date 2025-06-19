/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import alex.oopmotorphpayrollsystem.MySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utils.DbConnection;

/**
 *
 * @author Alex Resurreccion
 */
public class UserDao {
    private Connection conn;
    private Statement statement = null;
    private ResultSet rs = null;
    private PreparedStatement preparedStatement = null;

    // Constructor with custom connection
    public UserDao(Connection conn) {
        this.conn = conn;
    }

    // Constructor with default connection
    public UserDao() {
        this.conn = DbConnection.getConnection();
    }
    
    // Method to get the role name of the user by querying the database
    public String getRoleName(int employeeId){
        try {
            // Create a statement object
            statement = conn.createStatement();

            // Define the query
            String query = "SELECT r.role_id, r.role_name "
                    + "FROM employees e INNER JOIN user_credentials uc "
                    + "ON uc.employee_id = e.employee_id "
                    + "INNER JOIN roles r ON r.role_id = uc.role_id "
                    + "WHERE e.employee_id = " + employeeId;

            // Execute the query
            rs = statement.executeQuery(query);
            try { 
                if (rs.next()) {
                    return rs.getString("role_name");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return "";
    }
}



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import models.Address;
import models.Benefits;
import models.Employee;
import models.HRPersonnel;
import models.SystemAdministrator;
import models.User;
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
    
    public User getUserInfo(int employeeId, String role, boolean isAuth){
        User user = null;
        
        // don't fetch info if not yet login
        if(employeeId == 0 || isAuth == false){
            return user;
        }
        try {
            // Create a statement object
            statement = conn.createStatement();

            // Define the query
            String query = "SELECT * FROM employee_view " 
                    + "WHERE employee_id = " + employeeId ;

            // Execute the query
            rs = statement.executeQuery(query);
            // If user exists, populate user information based on role
            if (rs.next()) {
                
                if ("HR Personnel".equals(role)) {
                    user = new HRPersonnel();
                } else if ("System Admin".equals(role)) {
                    user = new SystemAdministrator();
                } else {
                    user = new Employee(); // Assign Employee role
                    // Set additional employee details
                    ((Employee)user).setStatus(rs.getString("status"));
                    ((Employee)user).setImmediateSupervisor(rs.getString("immediate_supervisor"));
                    
                    // Create Benefits object and assign to Employee
                    Benefits benefit = new Benefits(
                        rs.getInt("basic_salary"),
                        rs.getInt("gross_semi_monthly_rate"),
                        rs.getInt("hourly_rate"),
                        rs.getInt("rice_subsidy"),
                        rs.getInt("phone_allowance"),
                        rs.getInt("clothing_allowance")
                    );
                    ((Employee)user).setBenefits(benefit);
                }
                
                // Set common employee details
                user.setEmployeeID(rs.getInt("employee_id"));
                user.setLastName(rs.getString("last_name"));
                user.setFirstName(rs.getString("first_name"));
                user.setBirthday(rs.getDate("birthdate"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setSssNumber(rs.getString("sss_number"));
                user.setPhilhealthNumber(rs.getString("philhealth_number"));
                user.setTinNumber(rs.getString("tin_number"));
                user.setPagibigNumber(rs.getString("pagibig_number"));
                user.setPosition(rs.getString("position"));
                user.setDepartment(rs.getString("department"));
                user.setDateHired(rs.getDate("date_hired"));
                
                // Populate address
                Address address = new Address();
                address.setBarangay(rs.getString("barangay"));
                address.setCity(rs.getString("date_hired"));
                address.setProvince(rs.getString("province"));
                address.setStreet(rs.getString("street"));
                address.setZipcode(rs.getString("date_hired"));
                user.setAddress(address);
            } else {
                System.out.println("No user");
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return user; // Return the populated user object or null if no data found
    }
}



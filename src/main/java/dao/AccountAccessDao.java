/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import alex.oopmotorphpayrollsystem.MySQL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DbConnection;
import alex.oopmotorphpayrollsystem.AccountAccess;
import alex.oopmotorphpayrollsystem.Benefits;
import alex.oopmotorphpayrollsystem.Employee;
import alex.oopmotorphpayrollsystem.HRPersonnel;
import alex.oopmotorphpayrollsystem.SystemAdministrator;
import alex.oopmotorphpayrollsystem.User;
import java.sql.PreparedStatement;
import java.sql.Statement;
/**
 *
 * @author Alex Resurreccion
 */
public class AccountAccessDao {
    private Connection connection;
    private Statement statement = null;
    private ResultSet result = null;
    private PreparedStatement preparedStatement = null;

    // Constructor with custom connection
    public AccountAccessDao(Connection connection) {
        this.connection = connection;
    }

    // Constructor with default connection
    public AccountAccessDao() {
        this.connection = DbConnection.getConnection();
    }
    
    /**
     * This method handles user login by verifying the employee number and password.
     * If the credentials match, it retrieves the user's role and permissions.
     * 
     * @param employeeNumber The employee number of the user trying to log in
     * @param password The password entered by the user
     * @return true if login is successful, false otherwise
     */
    public AccountAccess login(int employeeNumber, String password) {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT employee_id, password, role_name "
                    + "FROM user_credentials uc " 
                    + "JOIN roles r ON r.role_id = uc.role_id WHERE employee_id = "
                    + employeeNumber ;

            // Execute the query
            result = statement.executeQuery(query);
            AccountAccess account = new AccountAccess();
            if (result.next()) {
                // Populate model object from DB data
                System.err.println("Error while executing SQL query!");
                account.setEmployeeNumber(result.getInt("employee_id"));
                account.setRole(result.getString("role_name"));
            } else {
                return null;
            }
            
            // Get list of permissions
            try {
                // Create a statement object
                statement = connection.createStatement();

                // Define the query
                String permQuery = "SELECT CONCAT(\n" +
                    "        p.module, \n" +
                    "        CASE \n" +
                    "            WHEN p.submodule IS NOT NULL THEN CONCAT('-', p.submodule)\n" +
                    "            ELSE ''\n" +
                    "        END\n" +
                    "    ) AS permission FROM user_credentials uc \n" +
                    "JOIN roles r ON r.role_id = uc.role_id\n" +
                    "JOIN role_permissions rp ON r.role_id = rp.role_id\n" +
                    "JOIN permissions p ON p.permission_id = rp.permission_id\n" +
                    "WHERE employee_id = " + employeeNumber;

                // Execute the query
                result = statement.executeQuery(permQuery);
                List<String> resultPermissions = new ArrayList<>();
                while (result.next()) {
                    resultPermissions.add(result.getString("permission"));
                }
                account.setIsAuthenticated(true);
                account.setPermissions(resultPermissions);
                return account;
            } catch (SQLException e) {
                System.err.println("Error while executing SQL query!");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return null;
    }
    
    public User getUserInfo(int employeeId, String role, boolean isAuth){
        User user = null;
        
        // don't fetch info if not yet login
        if(employeeId == 0 || isAuth == false){
            return user;
        }
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT * FROM employee_view " 
                    + "WHERE employee_id = " + employeeId ;

            // Execute the query
            result = statement.executeQuery(query);
            // If user exists, populate user information based on role
            if (result.next()) {
                
                if ("HR Personnel".equals(role)) {
                    user = new HRPersonnel();
                } else if ("System Admin".equals(role)) {
                    user = new SystemAdministrator();
                } else {
                    user = new Employee(); // Assign Employee role
                    // Set additional employee details
                    ((Employee)user).setStatus(result.getString("status"));
                    ((Employee)user).setImmediateSupervisor(result.getString("immediate_supervisor"));
                    
                    // Create Benefits object and assign to Employee
                    Benefits benefit = new Benefits(
                        result.getInt("basic_salary"),
                        result.getInt("gross_semi_monthly_rate"),
                        result.getInt("hourly_rate"),
                        result.getInt("rice_subsidy"),
                        result.getInt("phone_allowance"),
                        result.getInt("clothing_allowance")
                    );
                    ((Employee)user).setBenefits(benefit);
                }
                
                // Set common employee details
                user.setEmployeeID(result.getInt("employee_id"));
                user.setLastName(result.getString("last_name"));
                user.setFirstName(result.getString("first_name"));
//                user.setAddress(result.getString("address"));
                user.setBirthday(result.getDate("birthdate"));
                user.setPhoneNumber(result.getString("phone_number"));
                user.setSssNumber(result.getString("sss_number"));
                user.setPhilhealthNumber(result.getString("philhealth_number"));
                user.setTinNumber(result.getString("tin_number"));
                user.setPagibigNumber(result.getString("pagibig_number"));
                user.setPosition(result.getString("position"));
                user.setDepartment(result.getString("department"));
                user.setDateHired(result.getDate("date_hired"));
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

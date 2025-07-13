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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex Resurreccion
 */
public class UserDao {
    private Connection conn;
    private Statement statement = null;
    private ResultSet rs = null;
    private PreparedStatement preparedStatement = null;

    // Kept your original constructor
    public UserDao(Connection conn) {
        this.conn = conn;
    }

    // Kept your original constructor
    public UserDao() {
        // Assuming you have a DbConnection class for getting the connection
        this.conn = DbConnection.getConnection();
    }

    /**
     * Authenticates a user and handles login attempts.
     * @param employeeId The user's employee ID.
     * @param password The user's password.
     * @return A User object if successful, null if failed, locked, or user not found.
     */
    public User authenticate(String employeeId, String password) {
        if (isAccountLocked(employeeId)) {
            System.out.println("Authentication failed: Account is locked for employee ID " + employeeId);
            return null;
        }

        // The SQL joins the user_credentials with roles to get the role_name
        String sql = "SELECT uc.password, r.role_name " +
                     "FROM user_credentials uc " +
                     "JOIN roles r ON uc.role_id = r.role_id " +
                     "WHERE uc.employee_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // User found, now check the password
                    if (rs.getString("password").equals(password)) {
                        // Password is correct. Reset login attempts.
                        resetLoginAttempts(employeeId);

                        String roleName = rs.getString("role_name");
                        User user; // Declare as the abstract User type

                        // Instantiate the correct concrete class based on the role name.
                        switch (roleName) {
                            case "Employee":
                                user = new Employee(Integer.parseInt(employeeId));
                                break;
                            case "HR Personnel":
                                user = new HRPersonnel(Integer.parseInt(employeeId));
                                break;
                            case "System Admin":
                                user = new SystemAdministrator(Integer.parseInt(employeeId));
                                break;
                            default:
                                // If the role from the DB is unknown, treat as a failure.
                                System.err.println("Unknown role encountered: " + roleName);
                                return null;
                        }
                        return user; // Return the created Employee, HR, or Admin object
                    } else {
                        // Password was incorrect. Increment login attempts.
                        incrementLoginAttempts(employeeId);
                        return null;
                    }
                } else {
                    // Employee ID not found in the database.
                    return null;
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Increments the login attempt counter for a user. Locks the account if attempts exceed the limit.
     */
    public void incrementLoginAttempts(String employeeId) {
        String updateSql = "UPDATE user_credentials SET login_attempts = login_attempts + 1 WHERE employee_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setString(1, employeeId);
            pstmt.executeUpdate();
            // After incrementing, check if the account should be locked
            lockAccountIfNecessary(employeeId);
        } catch (SQLException e) {
            System.err.println("Error incrementing login attempts: " + e.getMessage());
        }
    }
    
    /**
     * Checks the number of login attempts and locks the account if it reaches the threshold (3).
     */
    private void lockAccountIfNecessary(String employeeId) {
        if (getLoginAttempts(employeeId) >= 3) {
            String lockSql = "UPDATE user_credentials SET is_locked = TRUE WHERE employee_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(lockSql)) {
                pstmt.setString(1, employeeId);
                pstmt.executeUpdate();
                System.out.println("Account locked for employee ID: " + employeeId);
            } catch (SQLException e) {
                 System.err.println("Error locking account: " + e.getMessage());
            }
        }
    }

    /**
     * Resets login attempts to 0 upon a successful login.
     */
    public void resetLoginAttempts(String employeeId) {
        String sql = "UPDATE user_credentials SET login_attempts = 0 WHERE employee_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error resetting login attempts: " + e.getMessage());
        }
    }

    /**
     * Checks if an account is currently locked.
     * @return true if the account is locked, false otherwise.
     */
    public boolean isAccountLocked(String employeeId) {
        String sql = "SELECT is_locked FROM user_credentials WHERE employee_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_locked");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if account is locked: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Retrieves the current number of failed login attempts for a user.
     */
    public int getLoginAttempts(String employeeId) {
        String sql = "SELECT login_attempts FROM user_credentials WHERE employee_id = ?";
         try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("login_attempts");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting login attempts: " + e.getMessage());
        }
        return 0;
    }

    // --- Admin Methods ---

    /**
     * Unlocks an account and resets its login attempts. (For Admin use)
     */
    public void unlockAccount(String employeeId) {
        String sql = "UPDATE user_credentials SET is_locked = FALSE, login_attempts = 0 WHERE employee_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error unlocking account: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves a list of all employee IDs whose accounts are locked. (For Admin use)
     */
    public List<String> getLockedAccounts() {
        List<String> lockedAccounts = new ArrayList<>();
        String sql = "SELECT employee_id FROM user_credentials WHERE is_locked = TRUE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lockedAccounts.add(rs.getString("employee_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving locked accounts: " + e.getMessage());
        }
        return lockedAccounts;
    }
    
    /**
     * Retrieves a list of all users with their name and lock status for display.
     * @return A list of UserDetails objects.
     */
    public List<UserDetails> getAllUsersWithLockStatus() {
        List<UserDetails> users = new ArrayList<>();
        String sql = "SELECT e.employee_id, e.first_name, e.last_name, uc.is_locked " +
                     "FROM employees e " +
                     "JOIN user_credentials uc ON e.employee_id = uc.employee_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String employeeId = rs.getString("employee_id");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                boolean isLocked = rs.getBoolean("is_locked");
                users.add(new UserDetails(employeeId, name, isLocked));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user details: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
    
    // --- YOUR ORIGINAL METHOD - REFACTORED FOR SECURITY ---

    /**
     * Gets the role name of a user from the database.
     * This method is now safer, using a PreparedStatement.
     */
    public String getRoleName(int employeeId) {
        String query = "SELECT r.role_name " +
                       "FROM user_credentials uc " +
                       "JOIN roles r ON r.role_id = uc.role_id " +
                       "WHERE uc.employee_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting role name: " + e.getMessage());
            e.printStackTrace();
        }
        return ""; // Return empty string if not found
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
                user.setStatus(rs.getString("status"));
                    user.setImmediateSupervisor(rs.getInt("immediate_supervisor"));
                    
                    // Create Benefits object and assign to Employee
                    Benefits benefit = new Benefits(
                        rs.getInt("basic_salary"),
                        rs.getInt("gross_semi_monthly_rate"),
                        rs.getInt("hourly_rate"),
                        rs.getInt("rice_subsidy"),
                        rs.getInt("phone_allowance"),
                        rs.getInt("clothing_allowance")
                    );
                    user.setBenefits(benefit);
                
                // Populate address
                Address address = new Address();
                address.setBarangay(rs.getString("barangay"));
                address.setCity(rs.getString("city"));
                address.setProvince(rs.getString("province"));
                address.setStreet(rs.getString("street"));
                address.setZipcode(rs.getString("zipcode"));
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

/**
 * A simple data container class to hold user information for the JTable.
 */
class UserDetails {
    private final String employeeId;
    private final String name;
    private final boolean isLocked;

    public UserDetails(String employeeId, String name, boolean isLocked) {
        this.employeeId = employeeId;
        this.name = name;
        this.isLocked = isLocked;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public boolean isLocked() { return isLocked; }
}



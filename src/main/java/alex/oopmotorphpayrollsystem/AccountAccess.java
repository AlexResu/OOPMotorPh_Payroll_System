/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * The AccountAccess class manages user authentication, permissions, and roles.
 * It provides methods for logging in, logging out, and retrieving user information 
 * from a database (via MySQL).
 * @author Alex Resurreccion
 */
public class AccountAccess {
    // Instance variables for employee number, permissions, authentication status, and role
    private int employeeNumber;
    private List<String> permissions;
    private boolean isAuthenticated;
    private String role;
    
    //  Constructor that initializes the permissions list and sets the authentication status to false.
    // @param permissions List of permissions granted to the user
    public AccountAccess(List<String> permissions) {
        this.permissions = permissions;
        this.isAuthenticated = false;
    }
    
    //  Default constructor.
    public AccountAccess() {}
    
    // Getter methods for employee number, authentication status, permissions, and role
    public int getEmployeeNumber() {
        return employeeNumber;
    }
    
    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public String getRole() {
        return role;
    }
    
    // Setter methods for employee number, permissions, authentication status, and role
    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    
    public void setIsAuthenticated(boolean authenticated) {
        this.isAuthenticated = authenticated;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    /**
     * Logs out the user by clearing authentication details.
     * 
     * @param user The user object to be logged out
     */
    public void logoutUser(User user) {
        setRole(null);
        setIsAuthenticated(false);
    }
}

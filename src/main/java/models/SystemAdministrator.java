/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The SystemAdministrator class represents a user who has the highest level of access within the system.
 * They can manage roles, configure the system, add or update employees, and reset passwords.
 * It extends the User class and inherits its basic properties such as employee ID, name, etc.
 * 
 * @author Alex Resurreccion
 */
public class SystemAdministrator extends User {
    private List<String> roles = new ArrayList<>();
    
    // Constructor with employeeID, calls the superclass (User) constructor
    public SystemAdministrator(int employeeID) {
        super(employeeID);
    }
    
    public SystemAdministrator() {}
    
    // Add a new role to the system administrator's list of roles (prevents duplicates)
    public void addRole(String role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }
    
    // Update an existing role, replacing the old role with a new one
    public void updateRole(String oldRole, String newRole) {
        if (roles.contains(oldRole)) {
            roles.remove(oldRole);
            roles.add(newRole);
        }
    }
    
    // Remove a role from the system administrator's list
    public void deleteRole(String role) {
        roles.remove(role);
    }
    
    // Get a list of the system administrator's roles (returns a new list to maintain encapsulation)
    public List<String> getRoles() {
        return new ArrayList<>(roles);
    }
    
    // Reset the password of the system administrator (only resets if employeeID matches)
    public void resetPassword(int employeeID, String newPassword) {
        if (this.employeeID == employeeID) {
            this.password = newPassword;
        }
    }
    
    // Configure the system (this method might include some future system setup code)
    public void configureSystem() {
        System.out.println("Confirguring system...");
    }
}

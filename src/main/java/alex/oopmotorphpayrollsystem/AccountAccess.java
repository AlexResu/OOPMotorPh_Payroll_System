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
     * This method handles user login by verifying the employee number and password.
     * If the credentials match, it retrieves the user's role and permissions.
     * 
     * @param employeeNumber The employee number of the user trying to log in
     * @param password The password entered by the user
     * @return true if login is successful, false otherwise
     */
    public boolean login(int employeeNumber, String password) {
        // Create an instance of the MySQL class to interact with the database
        MySQL mySQL = new MySQL();
        
        // Fetch user credentials from the database by employee number
        ResultSet result = mySQL.getUserCredentialsByNumber(employeeNumber);
        
        try {
            if (result.next()) {
                String userPassword = result.getString("password");
                if(!userPassword.equals(password)){
                    mySQL.close();
                    return false;
                }
                setRole(result.getString("role_name"));
            } else {
                mySQL.close();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Err 1");
            mySQL.close();
            return false;
        }
        
        result = mySQL.getPermissions(employeeNumber);
        try {
            List<String> resultPermissions = new ArrayList<>();
            while (result.next()) {
                String permission = result.getString("permission");
                System.out.println(permission);

                // Create a new Permission object and add it to the list
                resultPermissions.add(permission);
            }
            setIsAuthenticated(true);
            System.out.println(resultPermissions);
            setPermissions(resultPermissions);
        } catch (SQLException ex) {
            System.out.println("Err 2");
            mySQL.close();
            return false;
        }
        
        setEmployeeNumber(employeeNumber);
        mySQL.close();
        return true;
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
    
    /**
     * Retrieves the user information from the database based on the employee number.
     * 
     * @return A User object containing the employee's details, or null if not authenticated
     */
    public User getUserInfo(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getEmployeeByNumber(employeeNumber);
        User user = null;
        
        // don't fetch info if not yet login
        if(employeeNumber == 0 || isAuthenticated == false){
            return user;
        }
        try {
            // If user exists, populate user information based on role
            if (result.next()) {
                
                if ("HR Personnel".equals(getRole())) {
                    user = new HRPersonnel();
                } else if ("System Admin".equals(getRole())) {
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
                user.setEmployeeID(result.getInt("employee_number"));
                user.setLastName(result.getString("last_name"));
                user.setFirstName(result.getString("first_name"));
                user.setAddress(result.getString("address"));
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
        } catch (SQLException ex) {
            // Handle SQL errors here, consider logging
        }
        
        mySQL.close();
        return user; // Return the populated user object or null if no data found
    }
}

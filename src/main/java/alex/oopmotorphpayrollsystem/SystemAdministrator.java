/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;

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
    private List<String> roles;
    
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
    
    // Add a new employee to the system. It uses MySQL helper to create an employee or user
    public boolean addNewEmployee(User employee) {
        MySQL mySQL = new MySQL();
        boolean result;
        if(employee instanceof Employee){
            result = mySQL.createEmployee((Employee) employee);
        } else {
            result = mySQL.createUser(employee);
        }
        mySQL.close();
        return result;
    }
    
    // Update an existing employee in the system
    public boolean updateEmployee(User employee) {
        MySQL mySQL = new MySQL();
        boolean result = mySQL.updateEmployee(employee);
        
        mySQL.close();
        return result;
    }
    
    // Delete an employee from the system by their employee number
    public boolean deleteEmployee(User employee) {
        MySQL mySQL = new MySQL();
        int result = mySQL.deleteEmployeeByNumber(employee.getEmployeeID());
        mySQL.close();
        return result == 1;
    }
    
    // Load the list of employees (no search criteria)
    public List<User> loadEmployeeList(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getUsers();
        List<User> employees = mapEmployees(result);
        
        mySQL.close();
        return employees;
    }
    
    // Load the list of employees with a search filter
    public List<User> loadEmployeeList(String search){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getUsers(search);
        List<User> employees = mapEmployees(result);
        mySQL.close();
        return employees;
    }
    
    // Private helper method to map ResultSet to a list of User objects
    private List<User> mapEmployees(ResultSet result){
        List<User> employees = new ArrayList<>();
        try { 
            while (result.next()) {
                User user = result.getInt("role_id") == 3
                        ? new SystemAdministrator() : 
                        result.getInt("role_id") == 2 ? 
                        new HRPersonnel() : new Employee();
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
                
                if (user instanceof Employee){
                    ((Employee) user).setStatus(result.getString("status"));
                    ((Employee) user).setPosition(result.getString("position"));
                    ((Employee) user).setImmediateSupervisor(result.getString("immediate_supervisor"));

                    Benefits benefit = new Benefits(
                            result.getInt("basic_salary"),
                            result.getInt("gross_semi_monthly_rate"),
                            result.getInt("hourly_rate"),
                            result.getInt("rice_subsidy"),
                            result.getInt("phone_allowance"),
                            result.getInt("clothing_allowance")
                        );
                    ((Employee) user).setBenefits(benefit);

                    employees.add(user);
                }
                
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return employees;
    }

}

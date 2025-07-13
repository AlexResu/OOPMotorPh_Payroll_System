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
import java.util.ArrayList;
import java.util.List;
import utils.DbConnection;

/**
 *
 * @author Alex Resurreccion
 */
public class SystemAdministratorDao {
    private Connection connection;
    private Statement statement = null;
    private ResultSet result = null;
    private PreparedStatement preparedStatement = null;

    // Constructor with custom connection
    public SystemAdministratorDao(Connection connection) {
        this.connection = connection;
    }

    // Constructor with default connection
    public SystemAdministratorDao() {
        this.connection = DbConnection.getConnection();
    }
    
    // Add a new employee to the system.
    public boolean addNewEmployee(User employee) {
        int rowsAffected = 0;
        try {
            boolean initialCommit  = connection.getAutoCommit();
            connection.setAutoCommit(false); // Transaction start

            // 1. Insert into address
            String addressQuery = "INSERT INTO address (street, barangay, city, province, zipcode) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psAddress = connection.prepareStatement(addressQuery, Statement.RETURN_GENERATED_KEYS);
            psAddress.setString(1, employee.getAddress().getStreet());
            psAddress.setString(2, employee.getAddress().getBarangay());
            psAddress.setString(3, employee.getAddress().getCity());
            psAddress.setString(4, employee.getAddress().getProvince());
            psAddress.setString(5, employee.getAddress().getZipcode());
            System.out.println("Executing Employee Update: " + psAddress);
            psAddress.executeUpdate();

            ResultSet rsAddress = psAddress.getGeneratedKeys();
            int addressId = 0;
            if (rsAddress.next()) {
                addressId = rsAddress.getInt(1);
            }

            // 2. Insert into salary
            String salaryQuery = "INSERT INTO salary (basic_salary, gross_semi_monthly_rate, hourly_rate, effective_date) VALUES (?, ?, ?, CURDATE())";
            PreparedStatement psSalary = connection.prepareStatement(salaryQuery, Statement.RETURN_GENERATED_KEYS);
            Benefits benefit = employee.getBenefits();
            psSalary.setDouble(1, benefit.getBasicSalary());
            psSalary.setDouble(2, benefit.getGrossSemiMonthlyRate());
            psSalary.setDouble(3, benefit.getHourlyRate());
            System.out.println("Executing Employee Update: " + psSalary);
            psSalary.executeUpdate();

            ResultSet rsSalary = psSalary.getGeneratedKeys();
            int salaryId = 0;
            if (rsSalary.next()) {
                salaryId = rsSalary.getInt(1);
            }

            // 3. Insert into employees
            String empQuery = "INSERT INTO employees (last_name, first_name, phone_number, birthdate, is_deleted, date_hired, salary_id, supervisor_id, status_id, position_id, address_id) VALUES (?, ?, ?, ?, 0, CURDATE(), ?, ?, ?, ?, ?)";
            PreparedStatement psEmp = connection.prepareStatement(empQuery, Statement.RETURN_GENERATED_KEYS);
            psEmp.setString(1, employee.getLastName());
            psEmp.setString(2, employee.getFirstName());
            psEmp.setString(3, employee.getPhoneNumber());
            psEmp.setDate(4, new java.sql.Date(employee.getBirthday().getTime()));
            psEmp.setInt(5, salaryId);
            psEmp.setObject(6, employee.getImmediateSupervisor(), java.sql.Types.INTEGER);
            psEmp.setInt(7, getStatusId(employee.getStatus()));
            psEmp.setInt(8, getPositionId(employee.getPosition()));
            psEmp.setInt(9, addressId);
            System.out.println("Executing Employee Update: " + psEmp);
            psEmp.executeUpdate();

            ResultSet rsEmp = psEmp.getGeneratedKeys();
            int employeeId = 0;
            if (rsEmp.next()) {
                employeeId = rsEmp.getInt(1);
            }

            // 4. Insert into gov_id
            String govQuery = "INSERT INTO gov_id (sss_num, tin, philhealth, pagibig, employee_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psGov = connection.prepareStatement(govQuery);
            psGov.setString(1, employee.getSssNumber());
            psGov.setString(2, employee.getTinNumber());
            psGov.setString(3, employee.getPhilhealthNumber());
            psGov.setString(4, employee.getPagibigNumber());
            psGov.setInt(5, employeeId);
            System.out.println("Executing Employee Update: " + psGov);
            psGov.executeUpdate();

            // 5. Insert into user_credentials
            String credQuery = "INSERT INTO user_credentials (employee_id, password, role_id) VALUES (?, ?, ?)";
            PreparedStatement psCred = connection.prepareStatement(credQuery);
            psCred.setInt(1, employeeId);
            psCred.setString(2, employee.getLastName() + "123");
            psCred.setInt(3, 1); // default role_id
            System.out.println("Executing Employee Update: " + psCred);
            rowsAffected = psCred.executeUpdate();

            if(initialCommit){
                connection.commit(); // Commit if all succeeded
                connection.setAutoCommit(initialCommit);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
            try {
                connection.rollback(); // Rollback if error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return rowsAffected == 1;
    }
    
    private int getStatusId(String statusName) throws SQLException {
        String query = "SELECT status_id FROM status WHERE type = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, statusName);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("status_id") : 0;
    }
    
    private int getPositionId(String positionName) throws SQLException {
        String query = "SELECT position_id FROM positions WHERE name = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, positionName);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("position_id") : 0;
    }
    
    private int getEmployeeAddressId(int employeeId) throws SQLException {
        String query = "SELECT address_id FROM employees WHERE employee_id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, employeeId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("address_id") : 0;
    }
    
    private int getEmployeeSalaryId(int employeeId) throws SQLException {
        String query = "SELECT salary_id FROM employees WHERE employee_id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, employeeId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("salary_id") : 0;
    }
    
    // Update an existing employee in the system
    public boolean updateEmployee(User employee) {
        int rowsAffected = 0;

        try {
            // Base employee update (shared for all types of users)
            String query = "UPDATE employees SET last_name = ?, first_name = ?, birthdate = ?, phone_number = ?";

            if (employee instanceof Employee) {
                query += ", status_id = ?, position_id = ?, supervisor_id = ?";
            }

            query += " WHERE employee_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employee.getLastName());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setDate(3, new java.sql.Date(employee.getBirthday().getTime()));
            preparedStatement.setString(4, employee.getPhoneNumber());

            if (employee instanceof Employee emp) {
                preparedStatement.setInt(5, getStatusId(emp.getStatus()));
                preparedStatement.setInt(6, getPositionId(emp.getPosition()));
                if (emp.getImmediateSupervisor() != 0) {
                    preparedStatement.setObject(7, employee.getImmediateSupervisor(), java.sql.Types.INTEGER); // Nullable
                } else {
                    preparedStatement.setNull(7, java.sql.Types.INTEGER);
                }
//                preparedStatement.setInt(8, getSalaryId(emp.getSalary()));
//                preparedStatement.setInt(9, emp.getAddressId());
                preparedStatement.setInt(8, emp.getEmployeeID());
            } else {
                preparedStatement.setInt(5, employee.getEmployeeID()); // employee_id
            }

            System.out.println("Executing Employee Update: " + preparedStatement);
            preparedStatement.executeUpdate();
            
            String addressQuery = "UPDATE address SET street = ?, barangay = ?, "
                    + "city = ?, province = ?, zipcode = ? "
                    + "WHERE address_id = ?";
            PreparedStatement psAddress = connection.prepareStatement(addressQuery, Statement.RETURN_GENERATED_KEYS);
            psAddress.setString(1, employee.getAddress().getStreet());
            psAddress.setString(2, employee.getAddress().getBarangay());
            psAddress.setString(3, employee.getAddress().getCity());
            psAddress.setString(4, employee.getAddress().getProvince());
            psAddress.setString(5, employee.getAddress().getZipcode());
            psAddress.setInt(6, getEmployeeAddressId(employee.getEmployeeID()));
            psAddress.executeUpdate();
            
            // Update salary
            String salaryQuery = "UPDATE salary SET basic_salary = ?, "
                    + "gross_semi_monthly_rate = ?, hourly_rate = ? "
                    + "WHERE salary_id = ?";
            PreparedStatement psSalary = connection.prepareStatement(salaryQuery, Statement.RETURN_GENERATED_KEYS);
            Benefits benefit = employee.getBenefits();
            psSalary.setDouble(1, benefit.getBasicSalary());
            psSalary.setDouble(2, benefit.getGrossSemiMonthlyRate());
            psSalary.setDouble(3, benefit.getHourlyRate());
            psSalary.setInt(4, getEmployeeSalaryId(employee.getEmployeeID()));
            psSalary.executeUpdate();
            
            String govQuery = "UPDATE gov_id SET sss_num = ?, philhealth = ?, tin = ?, pagibig = ? WHERE employee_id = ?";
            PreparedStatement govStmt = connection.prepareStatement(govQuery);
            govStmt.setString(1, employee.getSssNumber());
            govStmt.setString(2, employee.getPhilhealthNumber());
            govStmt.setString(3, employee.getTinNumber());
            govStmt.setString(4, employee.getPagibigNumber());
            govStmt.setInt(5, employee.getEmployeeID());

            System.out.println("Executing Gov ID Update: " + govStmt);
            govStmt.executeUpdate();
            
            int roleId = (employee instanceof HRPersonnel) ? 2 :
                         (employee instanceof SystemAdministrator) ? 3 : 1;

            String credQuery = "UPDATE user_credentials SET role_id = ? WHERE employee_id = ?";
            PreparedStatement credStmt = connection.prepareStatement(credQuery);
            credStmt.setInt(1, roleId);
            credStmt.setInt(2, employee.getEmployeeID());

            System.out.println("Executing Role Update: " + credStmt);
            rowsAffected = credStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating employees table");
            e.printStackTrace();
        }
        
        System.out.println(rowsAffected);
        return rowsAffected == 1;
    }

    
    // Delete an employee from the system by their employee number
    public boolean deleteEmployee(User employee) {
        int rowsAffected = 0;
        System.out.println("deleteEmployeeByNumber");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE employees SET is_deleted = ? WHERE employee_id = ? AND is_deleted = False";
            
            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, employee.getEmployeeID());
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return rowsAffected == 1;
    }
    
    // Load the list of employees (no search criteria)
    public List<User> loadEmployeeList(){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.*, uc.role_id FROM employee_view e "
                    + "LEFT JOIN user_credentials uc "
                    + "ON e.employee_id = uc.employee_id "
                    + "WHERE e.is_deleted = False";

            // Execute the query
            result = statement.executeQuery(query);
            List<User> employees = mapEmployees(result);
            return employees;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
    
    // Load the list of employees with a search filter
    public List<User> loadEmployeeList(String search){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.*, uc.role_id FROM employee_view e LEFT JOIN user_credentials uc "
                    + "ON e.employee_id = uc.employee_id WHERE "
                    + "(e.employee_id LIKE ? OR first_name LIKE ? OR "
                    + "last_name LIKE ?) AND e.is_deleted = False";
            
            preparedStatement = connection.prepareStatement(query);

            // Setting parameters
            preparedStatement.setString(1, "%" + search + "%");
            preparedStatement.setString(2, "%" + search + "%");
            preparedStatement.setString(3, "%" + search + "%");

            // Execute the query
            result = preparedStatement.executeQuery();
            List<User> employees = mapEmployees(result);
            return employees;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
    
    public List<User> loadNewUsers(){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.*, uc.role_id FROM employee_view e "
                    + "LEFT JOIN user_credentials uc "
                    + " ON e.employee_id = uc.employee_id "
                    + "WHERE date_hired >= CURDATE() - INTERVAL 7 DAY ";
            
            result = statement.executeQuery(query);
            List<User> employees = mapEmployees(result);
            return employees;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            System.out.println("SQL Error Message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Integer> loadDashboard(){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT COUNT(*) AS total, " 
                    + " COUNT(CASE WHEN date_hired >= NOW() - INTERVAL 1 WEEK THEN 1 END) "
                    + " AS new_employees FROM employees e ";
            
            result = statement.executeQuery(query);
            List<Integer> dashboardInfo = new ArrayList<>();
            if (result.next()) {
                dashboardInfo.add(result.getInt("total"));
                dashboardInfo.add(result.getInt("new_employees"));
            }
            return dashboardInfo;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            System.out.println("SQL Error Message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Unlocks a user's account and resets their login attempts.
    public boolean unlockUserAccount(int employeeId) {
        String sql = "UPDATE user_credentials SET is_locked = FALSE, login_attempts = 0 WHERE employee_id = ?";
        // Uses the existing 'connection' object. Does not create/close its own.
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if the update was successful
        } catch (SQLException e) {
            System.err.println("Error unlocking account: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Private helper method to map ResultSet to a list of User objectsHR
    private List<User> mapEmployees(ResultSet result){
        List<User> employees = new ArrayList<>();
        try { 
            while (result.next()) {
                User user = result.getInt("role_id") == 3
                        ? new SystemAdministrator() : 
                        result.getInt("role_id") == 2 ? 
                        new HRPersonnel() : new Employee();
                System.out.println(result.getInt("employee_id"));
                user.setEmployeeID(result.getInt("employee_id"));
                user.setLastName(result.getString("last_name"));
                user.setFirstName(result.getString("first_name"));
                user.setBirthday(result.getDate("birthdate"));
                user.setPhoneNumber(result.getString("phone_number"));
                user.setSssNumber(result.getString("sss_number"));
                user.setPhilhealthNumber(result.getString("philhealth_number"));
                user.setTinNumber(result.getString("tin_number"));
                user.setPagibigNumber(result.getString("pagibig_number"));
                user.setDateHired(result.getDate("date_hired"));
                
                Address address = new Address();
                address.setBarangay(result.getString("barangay"));
                address.setCity(result.getString("city"));
                address.setProvince(result.getString("province"));
                address.setStreet(result.getString("street"));
                address.setZipcode(result.getString("zipcode"));
                user.setAddress(address);
                
                user.setStatus(result.getString("status"));
                user.setPosition(result.getString("position"));
                user.setImmediateSupervisor(result.getInt("immediate_supervisor"));

                Benefits benefit = new Benefits(
                        result.getInt("basic_salary"),
                        result.getInt("gross_semi_monthly_rate"),
                        result.getInt("hourly_rate"),
                        result.getInt("rice_subsidy"),
                        result.getInt("phone_allowance"),
                        result.getInt("clothing_allowance")
                    );
                user.setBenefits(benefit);
                employees.add(user);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return employees;
    }
}

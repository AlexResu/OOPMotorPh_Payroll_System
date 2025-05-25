/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;

/**
 *
 * @author Alex Resurreccion
 */
import models.Employee;
import models.Benefits;
import models.Payroll;
import models.SystemAdministrator;
import models.User;
import models.HRPersonnel;
import models.Deductions;
import models.LeaveRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import alex.oopmotorphpayrollsystem.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;


/**
 * Constructs a new instance of the MySQL class.
 * Establishes a connection to the MySQL database.
 * Prints "connect" to the console upon successful connection.
 */
public class MySQL {
    Statement statement = null;
    ResultSet resultSet = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    public MySQL() {
        System.out.println("connect");
        
        try {
            // Register the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            String url = "jdbc:mysql://localhost:3307/motorph_db";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
            

            // Do something with the connection...
        } catch (ClassNotFoundException e) {
            // Handle the case where the MySQL JDBC driver is not found
            System.err.println("MySQL JDBC driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            // Handle the case where failed to connect to MySQL database
            System.err.println("Failed to connect to MySQL database!");
            e.printStackTrace();
        }
    }
    
//    public MySQL() {
//        System.out.println("Connecting to H2 Database...");
//
//        try {
//            // Load H2 driver
//            Class.forName("org.h2.Driver");
//
//            // File-based H2 Database URL (will create motorph_db.mv.db file in the current directory)
//            String url = "jdbc:h2:./motorph_db"; // Relative path to store database file locally
//            String username = "sa";  // H2 default username
//            String password = "";    // H2 default password (empty)
//
//            // Open connection to H2 Database
//            connection = DriverManager.getConnection(url, username, password);
//            System.out.println("Connected to H2 Database!");
//
//            // Execute the SQL script to initialize the database (if needed)
//            executeSqlScript("resources/motorph_db.sql");
//
//        } catch (ClassNotFoundException e) {
//            System.err.println("H2 JDBC driver not found!");
//            e.printStackTrace();
//        } catch (SQLException e) {
//            System.err.println("Failed to connect to H2 database!");
//            e.printStackTrace();
//        }
//    }
//
//    // Execute SQL script from a file
//    private void executeSqlScript(String filePath) {
//        try {
//            String sql = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
//            sql = sql.replaceAll("(?m)^--.*\n", ""); // Remove comments
//
//            try (Statement stmt = connection.createStatement()) {
//                String[] queries = sql.split(";"); // Split statements
//                for (String query : queries) {
//                    query = query.trim();
//                    if (!query.isEmpty()) {
//                        System.out.println("Executing: " + query); // Debugging output
//                        stmt.execute(query);
//                    }
//                }
//                System.out.println("SQL script executed successfully!");
//            }
//        } catch (IOException | SQLException e) {
//            System.err.println("Error while executing SQL script: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    
    /**
    * Closes the database resources including the result set, connection, statement, and prepared statement.
    * Prints "close" to the console upon successful closure.
    */
    public void close(){
        // Print "close" to indicate the resource closure attempt
        System.out.println("close");
        if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        // Close the result set if it's not null
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
    * Retrieves all employee records from the database.
    * 
    * @return A ResultSet containing the employee records.
    */
    public ResultSet getEmployees() {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT * FROM employees WHERE is_deleted = False";

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getAttendanceList(int employeeNumber) {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT a.*, e.position, e.first_name, e.last_name "
                    + "FROM attendance_record a INNER JOIN employees e "
                    + "ON e.employee_number = a.employee_number ";
            if(employeeNumber != 0){
                query += "WHERE a.employee_number = ? ";
            }
            query += "ORDER BY a.date DESC";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if(employeeNumber != 0){
                preparedStatement.setInt(1, employeeNumber);
            }

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getAttendanceList(
            int employeeNumber, Date startDate, Date endDate) {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT a.*, e.position, e.first_name, e.last_name "
                    + "FROM attendance_record a INNER JOIN employees e "
                    + "ON e.employee_number = a.employee_number "
                    + "WHERE date BETWEEN ? AND ? ";
            if(employeeNumber != 0){
                query += "AND a.employee_number = ? ";
            }
            query += "ORDER BY date DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            
            if(employeeNumber != 0){
                preparedStatement.setInt(3, employeeNumber);
            }

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getSummaryList() {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT p.*, e.employee_number, e.first_name, e.last_name, e.department,"
                    + " e.position FROM payslip p INNER JOIN employees e "
                    + "ON e.employee_number = p.employee_id ";
            query += "ORDER BY payment_date DESC";

            // Execute the query
            resultSet = statement.executeQuery(query);
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getSummaryList(int month, int year) {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT p.*, e.employee_number, e.first_name, e.last_name, "
                    + "e.department, e.position FROM payslip p "
                    + "INNER JOIN employees e "
                    + "ON e.employee_number = p.employee_id "
                    + "WHERE MONTH(period_start) = ? AND YEAR(period_start) = ? ";
            query += "ORDER BY payment_date DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet loadPayslip(
            Date startDate, Date endDate, Employee employee) {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT p.*, e.employee_number, e.first_name, e.last_name, "
                    + "e.department, e.position FROM payslip p "
                    + "INNER JOIN employees e "
                    + "ON e.employee_number = p.employee_id "
                    + "WHERE period_start = ? AND period_end = ? "
                    + "AND p.employee_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            
            preparedStatement.setInt(3, employee.getEmployeeID());

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getUsers() {
        System.err.println("Get users");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.*, uc.role_id FROM employees e "
                    + "LEFT JOIN user_credentials uc "
                    + "ON e.employee_number = uc.employee_number "
                    + "WHERE e.is_deleted = False";

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    /**
    * Retrieves employee records from the database based on a search query.
    * 
    * @param search A string representing the search query.
    * @return A ResultSet containing the employee records matching the search query.
    */
    public ResultSet getUsers(String search) {
        System.err.println("Get users");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.*, uc.role_id FROM employees e LEFT JOIN user_credentials uc "
                    + "ON e.employee_number = uc.employee_number WHERE "
                    + "(e.employee_number LIKE ? OR first_name LIKE ? OR "
                    + "last_name LIKE ?) AND e.is_deleted = False";
            
            preparedStatement = connection.prepareStatement(query);

            // Setting parameters
            preparedStatement.setString(1, "%" + search + "%");
            preparedStatement.setString(2, "%" + search + "%");
            preparedStatement.setString(3, "%" + search + "%");

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        // Return the result set containing the employee records matching the search query
        return resultSet;
    }
    
    public ResultSet getEmployeeUsers() {
        System.err.println("Get employee users");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.* FROM employees e "
                    + "INNER JOIN user_credentials uc "
                    + "ON e.employee_number = uc.employee_number "
                    + "WHERE uc.role_id = 1 AND e.is_deleted = False";

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    /**
    * Retrieves employee records from the database based on a search query.
    * 
    * @param search A string representing the search query.
    * @return A ResultSet containing the employee records matching the search query.
    */
    public ResultSet getEmployeesWithSearch(String search) {
        System.err.println("Get employee users");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT * FROM employees e INNER JOIN user_credentials uc "
                    + "ON e.employee_number = uc.employee_number WHERE uc.role_id = 1 "
                    + "AND is_deleted = False AND "
                    + "(e.employee_number LIKE ? OR first_name LIKE ? OR "
                    + "last_name LIKE ?)";
            
            preparedStatement = connection.prepareStatement(query);

            // Setting parameters
            preparedStatement.setString(1, "%" + search + "%");
            preparedStatement.setString(2, "%" + search + "%");
            preparedStatement.setString(3, "%" + search + "%");

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        // Return the result set containing the employee records matching the search query
        return resultSet;
    }
    
    /**
    * Retrieves employee records from the database based on the employee number.
    * 
    * @param employeeNumber The employee number used for retrieval.
    * @return A ResultSet containing the employee records matching the provided employee number.
    */
    public ResultSet getEmployeeByNumber(int employeeNumber) {
        System.out.println("getEmployeeByNumber");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT * FROM employees " 
                    + "WHERE employee_number = " + employeeNumber ;

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return resultSet;
    }
    
    public ResultSet getUserRole(User user) {
        System.out.println(user.getEmployeeID());
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT r.role_id, r.role_name "
                    + "FROM employees e INNER JOIN user_credentials uc "
                    + "ON uc.employee_number = e.employee_number "
                    + "INNER JOIN roles r ON r.role_id = uc.role_id "
                    + "WHERE e.employee_number = " + user.getEmployeeID();

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getDashboard(User user) {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT COUNT(*) AS total, " 
                    + " COUNT(CASE WHEN date_hired >= NOW() - INTERVAL 1 WEEK THEN 1 END) "
                    + " AS new_employees FROM employees e ";
            if(user instanceof HRPersonnel){
                query += "INNER JOIN user_credentials uc "
                    + " ON e.employee_number = uc.employee_number "
                    + "WHERE role_id = 1";
            }
            
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        // Return the result set containing the employee records matching the search query
        return resultSet;
    }
    
    public ResultSet getNewUsers(User user) {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.*, uc.role_id FROM employees e "
                    + "LEFT JOIN user_credentials uc "
                    + " ON e.employee_number = uc.employee_number "
                    + "WHERE date_hired >= CURDATE() - INTERVAL 7 DAY ";
            if(user instanceof HRPersonnel){
                query += "AND role_id = 1 ";
            }
            
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        // Return the result set containing the employee records matching the search query
        return resultSet;
    }
    
    public ResultSet getLeaveRequests(){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT l.*, e.department, e.first_name, e.last_name, \n" +
                "e2.employee_number AS approver_id, \n" +
                "e2.first_name AS approver_first_name, \n" +
                "e2.last_name AS approver_last_name \n" +
                "FROM leave_records l \n" +
                "LEFT JOIN employees e ON e.employee_number = l.employee_number \n" +
                "LEFT JOIN employees e2 ON e2.employee_number = l.employee_number "
                + "ORDER BY date_filed DESC";

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getLeaveRequests(String remarks){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT l.*, e.department, e.first_name, e.last_name, \n" +
                "e2.employee_number AS approver_id, \n" +
                "e2.first_name AS approver_first_name, \n" +
                "e2.last_name AS approver_last_name \n" +
                "FROM leave_records l \n" +
                "LEFT JOIN employees e ON e.employee_number = l.employee_number \n" +
                "LEFT JOIN employees e2 ON e2.employee_number = l.employee_number ";
            
            if(!remarks.equals("All")){
                query += "WHERE remarks = ? ";
            }
            query += " ORDER BY date_filed DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            if(!remarks.equals("All")){
                preparedStatement.setString(1, remarks);
            }

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getPayslipList(Date startDate, Date endDate, String status) {
        System.out.println("getPayslipList");
        try {
            // Define the query
            String query = "SELECT p.id, e.*, "
                    + "p.period_start, p.period_end, "
                    + "p.payment_date, "
                    + "COALESCE(p.take_home_pay, 0) AS take_home_pay,\n" +
                    "    CASE \n" +
                    "        WHEN p.payment_date IS NOT NULL THEN 'Completed'\n" +
                    "        ELSE 'Pending'\n" +
                    "    END AS payslip_status\n" +
                    "FROM employees e\n" +
                    "INNER JOIN user_credentials uc "
                    + "ON e.employee_number = uc.employee_number\n" +
                    "INNER JOIN roles r ON uc.role_id = r.role_id\n" +
                    "LEFT JOIN payslip p ON e.employee_number = p.employee_id\n" +
                    "    AND p.period_start = ? " + 
                    "    AND p.period_end = ? " + 
                    "WHERE r.role_name = 'Employee'";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return resultSet;
    }
    
    public ResultSet getPayslipListForCalculate(int year, int month) {
        System.out.println("getPayslipList");
        try {
            // Define the query
            String query = "SELECT p.id, e.*, "
                    + "p.period_start, p.period_end, "
                    + "p.payment_date, COALESCE(SUM(p.hours_worked), 0) AS total_hours_worked, "
                    + "COALESCE(p.take_home_pay, 0) AS take_home_pay,\n" +
                    "    CASE \n" +
                    "        WHEN p.payment_date IS NOT NULL THEN 'Completed'\n" +
                    "        ELSE 'Pending'\n" +
                    "    END AS payslip_status\n" +
                    "FROM employees e\n" +
                    "INNER JOIN user_credentials uc "
                    + "ON e.employee_number = uc.employee_number\n" +
                    "INNER JOIN roles r ON uc.role_id = r.role_id\n" +
                    "LEFT JOIN payslip p ON e.employee_number = p.employee_id\n" +
                    "    AND YEAR(p.period_end) = ? " + 
                    "    AND MONTH(p.period_end) = ? " + 
                    "WHERE r.role_name = 'Employee' GROUP BY e.employee_number";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return resultSet;
    }
    
    public ResultSet getAttendanceByPeriod(Date startDate, Date endDate) {
        try {
            // Define the query
            String query = "SELECT a.* FROM attendance_record a "
                    + "INNER JOIN employees e "
                    + "ON e.employee_number = a.employee_number "
                    + "WHERE date BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return resultSet;
    }
    
    /**
    * Retrieves User credentials from the database based on the employee number.
    * 
    * @param employeeNumber The employee number used for retrieval.
    * @return A ResultSet containing the employee records matching the provided employee number.
    */
    public ResultSet getUserCredentialsByNumber(int employeeNumber) {
        System.out.println("getEmployeeByNumber");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT employee_number, password, role_name "
                    + "FROM user_credentials uc " 
                    + "JOIN roles r ON r.role_id = uc.role_id WHERE employee_number = "
                    + employeeNumber ;

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return resultSet;
    }
    
    public ResultSet getPermissions(int employeeNumber) {
        System.out.println("getEmployeeByNumber");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT CONCAT(\n" +
                "        p.module, \n" +
                "        CASE \n" +
                "            WHEN p.submodule IS NOT NULL THEN CONCAT('-', p.submodule)\n" +
                "            ELSE ''\n" +
                "        END\n" +
                "    ) AS permission FROM user_credentials uc \n" +
                "JOIN roles r ON r.role_id = uc.role_id\n" +
                "JOIN role_permissions rp ON r.role_id = rp.role_id\n" +
                "JOIN permissions p ON p.permission_id = rp.permission_id\n" +
                "WHERE employee_number = " + employeeNumber;;

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return resultSet;
    }
    
    /**
    * Retrieves detailed employee information from the database based on the employee number.
    * 
    * @param employeeNumber The employee number used for retrieval.
    * @return A ResultSet containing the detailed employee information matching the provided employee number.
    */
    public ResultSet getEmployeeDetailsByNumber(int employeeNumber) {
        System.out.println("getEmployeeByNumber");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT * FROM employees WHERE employee_number = " + employeeNumber;

            // Execute the query
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return resultSet;
    }
    
    public ResultSet getAttendanceRecords() {
        System.out.println("getAttendanceRecordByEmpNumber");
        ResultSet resultSet = null;
        try {
            // Define the query
            String query = "SELECT * FROM attendance_record ar LEFT JOIN "
                    + "employees e ON e.employee_number = ar.employee_number ORDER BY date DESC";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getAttendanceRecordsBySearch(String employeeNumber) {
        System.out.println("getAttendanceRecordByEmpNumber");
        ResultSet resultSet = null;
        try {
            // Define the query
            String query = "SELECT * FROM attendance_record ar LEFT JOIN "
                    + "employees e ON e.employee_number = ar.employee_number "
                    + "WHERE ar.employee_number LIKE ? ORDER BY date DESC";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setString(1, "%" + employeeNumber + "%");
            

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getAttendanceRecordsByDate(LocalDate startDate, LocalDate endDate) {
        System.out.println("getAttendanceRecordsByDate");
        ResultSet resultSet = null;
        try {
            // Define the query
            String query = "SELECT * FROM attendance_record ar LEFT JOIN "
                    + "employees e ON e.employee_number = ar.employee_number "
                    + "WHERE date BETWEEN ? AND ? ORDER BY date DESC";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getAttendanceRecordsBySearchAndDate(String employeeNumber, 
            LocalDate startDate, LocalDate endDate) {
        System.out.println("getAttendanceRecordsBySearchAndDate");
        ResultSet resultSet = null;
        try {
            // Define the query
            String query = "SELECT * FROM attendance_record ar LEFT JOIN "
                    + "employees e ON e.employee_number = ar.employee_number "
                    + "WHERE ar.employee_number LIKE ? AND date BETWEEN ? AND ? "
                    + "ORDER BY date DESC";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setString(1, "%" + employeeNumber + "%");
            preparedStatement.setDate(2, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(endDate));

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    /**
    * Retrieves employee attendance records from the database based on the employee number and date.
    * 
    * @param employeeNumber The employee number used for retrieval.
    * @param date The date used for filtering attendance records.
    * @return A ResultSet containing the employee attendance records matching the provided employee number and date.
    */
    public ResultSet getEmployeeAttendanceByNumber(int employeeNumber, String date) {
        System.out.println("getEmployeeAttendanceByNumber");
        try {

            // Define the query
            String query = "SELECT * FROM attendance_record "
                    + "WHERE employee_number = ? AND date = ?";
            
            preparedStatement = connection.prepareStatement(query);

            // Setting parameters
            preparedStatement.setInt(1, employeeNumber);
            preparedStatement.setString(2, date);

            // Execute the query
            resultSet = preparedStatement.executeQuery();
            System.out.println("Executing Query: " + preparedStatement.toString());
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        // Return the result set containing the employee attendance records
        // matching the provided employee number and date
        return resultSet;
    }
    /**
    * Creates a new employee record in the database with the provided details.
    * 
    * @param lastName The last name of the employee.
    * @param firstName The first name of the employee.
    * @param birthday The birthdate of the employee.
    * @param address The address of the employee.
    * @param phoneNumber The phone number of the employee.
    * @param sssNumber The SSS number of the employee.
    * @param philhealthNumber The PhilHealth number of the employee.
    * @param tinNumber The TIN number of the employee.
    * @param pagibigNumber The Pag-IBIG number of the employee.
    * @param status The employment status of the employee.
    * @param position The position of the employee.
    * @param immediateSupervisor The immediate supervisor of the employee.
    * @param basicSalary The basic salary of the employee.
    * @param grossSemiMonthlyRate The gross semi-monthly rate of the employee.
    * @param hourlyRate The hourly rate of the employee.
    * @param riceSubsidy The rice subsidy of the employee.
    * @param phoneAllowance The phone allowance of the employee.
    * @param clothingAllowance The clothing allowance of the employee.
    * @param employeeType The type of the employee (e.g., regular, contractual).
    * @return The number of rows affected by the insertion operation.
    */
    public boolean createEmployee(Employee employee){
        // Print "createEmployee" to indicate the method execution
        System.out.println("createEmployee");
        int rowsAffected = 0, empNum = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "INSERT INTO employees ("
                + "last_name, first_name, birthdate, address, phone_number, "
                + "sss_number, philhealth_number, tin_number, pagibig_number, "
                + "status, position, immediate_supervisor, basic_salary, "
                + "gross_semi_monthly_rate, hourly_rate, rice_subsidy, "
                + "phone_allowance, clothing_allowance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, employee.getLastName());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setDate(3, 
                    new java.sql.Date(employee.getBirthday().getTime()));
            preparedStatement.setString(4, employee.getAddress());
            preparedStatement.setString(5, employee.getPhoneNumber());
            preparedStatement.setString(6, employee.getSssNumber());
            preparedStatement.setString(7, employee.getPhilhealthNumber());
            preparedStatement.setString(8, employee.getTinNumber());
            preparedStatement.setString(9, employee.getPagibigNumber());
            preparedStatement.setString(10, employee.getStatus());
            preparedStatement.setString(11, employee.getPosition());
            preparedStatement.setString(12, employee.getImmediateSupervisor());
            
            Benefits benefit = employee.getBenefits();
            preparedStatement.setDouble(13, benefit.getBasicSalary());
            preparedStatement.setDouble(14, benefit.getGrossSemiMonthlyRate());
            preparedStatement.setDouble(15, benefit.getHourlyRate());
            preparedStatement.setDouble(16, benefit.getRiceSubsidy());
            preparedStatement.setDouble(17, benefit.getPhoneAllowance());
            preparedStatement.setDouble(18, benefit.getClothingAllowance());

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            rowsAffected = preparedStatement.executeUpdate();
            
            System.out.println(rowsAffected);
            if(rowsAffected > 0){
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if(generatedKeys.next()){
                    System.out.println(generatedKeys.getInt(1));
                    empNum = generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        // If an employee number was generated, insert user credentials with default password
        if(empNum != 0){
            try {
                // Create a statement object
                statement = connection.createStatement();

                // Define the query
                String query = "INSERT INTO user_credentials ("
                    + "employee_number, password, role_id) VALUES (?, ?, ?)";

                // Prepare the statement
                PreparedStatement preparedStatement2 = connection.prepareStatement(query);

                // Set the parameters
                preparedStatement2.setInt(1, empNum);
                preparedStatement2.setString(2, employee.getLastName()+"123");
                preparedStatement2.setInt(3, 1);

                // Execute the query
                System.out.println("Executing Query: " + preparedStatement2.toString());
                rowsAffected = preparedStatement2.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Error while executing SQL query!");
                e.printStackTrace();
            }
        
        }
        return rowsAffected == 1;
    }
    
    public boolean createUser(User employee){
        // Print "createEmployee" to indicate the method execution
        System.out.println("createEmployee");
        int rowsAffected = 0, empNum = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "INSERT INTO employees ("
                + "last_name, first_name, birthdate, address, phone_number, "
                + "sss_number, philhealth_number, tin_number, pagibig_number, position) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, employee.getLastName());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setDate(3, 
                    new java.sql.Date(employee.getBirthday().getTime()));
            preparedStatement.setString(4, employee.getAddress());
            preparedStatement.setString(5, employee.getPhoneNumber());
            preparedStatement.setString(6, employee.getSssNumber());
            preparedStatement.setString(7, employee.getPhilhealthNumber());
            preparedStatement.setString(8, employee.getTinNumber());
            preparedStatement.setString(9, employee.getPagibigNumber());
            preparedStatement.setString(10, employee.getPosition());

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            rowsAffected = preparedStatement.executeUpdate();
            
            if(rowsAffected > 0){
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if(generatedKeys.next()){
                    empNum = generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        int role_id = (employee instanceof HRPersonnel) ? 2 : 3;
        // If an employee number was generated, insert user credentials with default password
        if(empNum != 0){
            try {
                // Create a statement object
                statement = connection.createStatement();

                // Define the query
                String query = "INSERT INTO user_credentials ("
                    + "employee_number, password, role_id) VALUES (?, ?, ?)";

                // Prepare the statement
                PreparedStatement preparedStatement2 = connection.prepareStatement(query);

                // Set the parameters
                preparedStatement2.setInt(1, empNum);
                preparedStatement2.setString(2, employee.getLastName()+"123");
                preparedStatement2.setInt(3, role_id);

                // Execute the query
                System.out.println("Executing Query: " + preparedStatement2.toString());
                rowsAffected = preparedStatement2.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Error while executing SQL query!");
                e.printStackTrace();
            }
        }
        return rowsAffected == 1;
    }
    
    public int generatePayslips(List<Payroll> payslips){
        // Print "createEmployee" to indicate the method execution
        System.out.println("generatePayslips");
        int totalInserted = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "INSERT INTO payslip (employee_id, period_start, "
                    + "period_end, position, monthly_rate, hourly_rate, "
                    + "hours_worked, overtime_hours, gross_income, rice_subsidy,"
                    + " phone_allowance, clothing_allowance, sss_deduction, "
                    + "philhealth_deduction, pagibig_deduction, withholding_tax,"
                    + " take_home_pay, payment_date) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (Payroll payroll : payslips) {
                preparedStatement.setInt(1, payroll.getEmployee().getEmployeeID()); 
                preparedStatement.setDate(2, new java.sql.Date(payroll.getWeekPeriodStart().getTime()));
                preparedStatement.setDate(3, new java.sql.Date(payroll.getWeekPeriodEnd().getTime()));
                preparedStatement.setString(4, payroll.getEmployee().getPosition());
                
                Benefits benefits = payroll.getEmployee().getBenefits();
                preparedStatement.setDouble(5, benefits.getBasicSalary());
                preparedStatement.setDouble(6, benefits.getHourlyRate()); // Daily Rate

                preparedStatement.setDouble(7, payroll.getHoursWorked());
                preparedStatement.setDouble(8, payroll.getOvertimeHours());
                preparedStatement.setDouble(9, payroll.getGrossIncome());

                preparedStatement.setDouble(10, benefits.getRiceSubsidy());
                preparedStatement.setDouble(11, benefits.getPhoneAllowance());
                preparedStatement.setDouble(12, benefits.getClothingAllowance());

                Deductions deductions = payroll.getDeductions();
                preparedStatement.setDouble(13, deductions.getSssDeduction());
                preparedStatement.setDouble(14, deductions.getPhilhealthDeduction());
                preparedStatement.setDouble(15, deductions.getPagIbigDeduction());
                preparedStatement.setDouble(16, deductions.getTaxDeduction());

                preparedStatement.setDouble(17, payroll.getNetPay());
                preparedStatement.setDate(18, new java.sql.Date(System.currentTimeMillis()));

                preparedStatement.addBatch(); // Add to batch
            }
            int[] results = preparedStatement.executeBatch(); // Execute batch insert
            
            for (int result : results) {
                if (result == PreparedStatement.SUCCESS_NO_INFO || result > 0) {
                    totalInserted++;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return totalInserted;
    }
    
    /**
    * Updates the details of an existing employee record in the database.
    * 
    * @param employeeNumber The unique identifier of the employee to be updated.
    * @param lastName The updated last name of the employee.
    * @param firstName The updated first name of the employee.
    * @param birthday The updated birthdate of the employee.
    * @param address The updated address of the employee.
    * @param phoneNumber The updated phone number of the employee.
    * @param sssNumber The updated SSS number of the employee.
    * @param philhealthNumber The updated PhilHealth number of the employee.
    * @param tinNumber The updated TIN number of the employee.
    * @param pagibigNumber The updated Pag-IBIG number of the employee.
    * @param status The updated employment status of the employee.
    * @param position The updated position of the employee.
    * @param immediateSupervisor The updated immediate supervisor of the employee.
    * @param basicSalary The updated basic salary of the employee.
    * @param grossSemiMonthlyRate The updated gross semi-monthly rate of the employee.
    * @param hourlyRate The updated hourly rate of the employee.
    * @param riceSubsidy The updated rice subsidy of the employee.
    * @param phoneAllowance The updated phone allowance of the employee.
    * @param clothingAllowance The updated clothing allowance of the employee.
    * @param employeeType The updated type of the employee (e.g., regular, contractual).
    * @return The number of rows affected by the update operation.
    */
    public boolean updateEmployee(User employee){
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE employees SET "
                + "last_name = ?, first_name = ?, birthdate = ?, address = ?, phone_number = ?, "
                + "sss_number = ?, philhealth_number = ?, tin_number = ?, pagibig_number = ? ";
            if(employee instanceof Employee){
                query += ", status = ?, position = ?, immediate_supervisor = ?, basic_salary = ?, "
                        + "gross_semi_monthly_rate = ?, hourly_rate = ?, rice_subsidy = ?, "
                        + "phone_allowance = ?, clothing_allowance = ? ";
            }
                
            query += "WHERE employee_number = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setString(1, employee.getLastName());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setDate(3, new java.sql.Date(employee.getBirthday().getTime()));
            preparedStatement.setString(4, employee.getAddress());
            preparedStatement.setString(5, employee.getPhoneNumber());
            preparedStatement.setString(6, employee.getSssNumber());
            preparedStatement.setString(7, employee.getPhilhealthNumber());
            preparedStatement.setString(8, employee.getTinNumber());
            preparedStatement.setString(9, employee.getPagibigNumber());
            
            if(employee instanceof Employee){
                preparedStatement.setString(10, ((Employee) employee).getStatus());
                preparedStatement.setString(11, ((Employee) employee).getPosition());
                preparedStatement.setString(12, ((Employee) employee).getImmediateSupervisor());
                
                Benefits benefit = ((Employee) employee).getBenefits();
                preparedStatement.setDouble(13, benefit.getBasicSalary());
                preparedStatement.setDouble(14, benefit.getGrossSemiMonthlyRate());
                preparedStatement.setDouble(15, benefit.getHourlyRate());
                preparedStatement.setDouble(16, benefit.getRiceSubsidy());
                preparedStatement.setDouble(17, benefit.getPhoneAllowance());
                preparedStatement.setDouble(18, benefit.getClothingAllowance());
                preparedStatement.setInt(19, employee.getEmployeeID());
            } else {
                preparedStatement.setInt(10, employee.getEmployeeID());
            }
            System.out.println(query);
            System.out.println("Executing Query: " + preparedStatement.toString());
            
            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        int role_id = (employee instanceof HRPersonnel) ? 2 : 
                (employee instanceof SystemAdministrator) ? 3 : 1;
        // If an employee number was generated, insert user credentials with default password
        
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE user_credentials "
                    + "SET role_id = ? WHERE employee_number = ?";

            // Prepare the statement
            PreparedStatement preparedStatement2 = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement2.setInt(1, role_id);
            preparedStatement2.setInt(2, employee.getEmployeeID());

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement2.toString());
            rowsAffected = preparedStatement2.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return rowsAffected == 1;
    }
    
    public int updateEmployeeSelf(
            int employeeNumber,
            String address,
            String phoneNumber
    ){
        System.out.println("createEmployee");
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE employees SET address = ?, phone_number = ? "
                    + "WHERE employee_number = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setString(1, address);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setInt(3, employeeNumber);
            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return rowsAffected;
    }
    
    public int createLeaveRequest(LeaveRequest leave){
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();
            

            // Define the query
            String query = "INSERT INTO leave_records("
                    + "employee_number, leave_type, "
                    + "date_from, date_until, number_of_days, reason, "
                    + "remarks) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, leave.getEmployee().getEmployeeID());
            preparedStatement.setString(2, leave.getLeaveType());
            preparedStatement.setDate(
                    3, new java.sql.Date(leave.getStartDate().getTime()));
            preparedStatement.setDate(
                    4, new java.sql.Date(leave.getEndDate().getTime()));
            preparedStatement.setInt(5, leave.getDuration());
            preparedStatement.setString(6, leave.getReason());
            preparedStatement.setString(7, "PENDING");
            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return rowsAffected;
    }
    
    public boolean approveLeaveRequest(LeaveRequest leave){
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE leave_records "
                    + "SET approved_by = ?, remarks = 'APPROVED' WHERE id = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, leave.getApprovedBy().getEmployeeID());
            preparedStatement.setInt(2, leave.getLeaveID());
            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return rowsAffected == 1;
    }
    
    public boolean declineLeaveRequest(LeaveRequest leave){
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE leave_records "
                    + "SET approved_by = ?, remarks = 'DECLINED' WHERE id = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, leave.getApprovedBy().getEmployeeID());
            preparedStatement.setInt(2, leave.getLeaveID());
            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return rowsAffected == 1;
    }
    
    /**
    * Deletes an employee record from the database based on the employee number.
    * 
    * @param employeeNumber The unique identifier of the employee to be deleted.
    * @return The number of rows affected by the deletion operation.
    */
    public int deleteEmployeeByNumber(int employeeNumber) {
        int rowsAffected = 0;
        System.out.println("deleteEmployeeByNumber");
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE employees SET is_deleted = ? WHERE employee_number = ?";
            
            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, employeeNumber);
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return rowsAffected;
    }
    
    public ResultSet getAttendanceRecordByEmpNumber(int employeeNumber, LocalDate date) {
        System.out.println("getAttendanceRecordByEmpNumber");
        ResultSet resultSet = null;
        try {
            // Define the query
            String query = "SELECT * FROM attendance_record WHERE employee_number = ? AND date = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, employeeNumber);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            System.out.println(java.sql.Date.valueOf(date));
            

            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public int createAttendanceRecord(
        int employeeNumber,
        LocalDate date,
        LocalDateTime timeIn
    ){
        // Print "createAttendanceRecord" to indicate the method execution
        System.out.println("createAttendanceRecord");
        int rowsAffected = 0;
        try {
            // Define the query
            String query = "INSERT INTO attendance_record ("
                    + "employee_number, date, time_in) VALUES (?, ?, ?)";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters for the prepared statement
            preparedStatement.setInt(1, employeeNumber);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(timeIn));

            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected);
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public int updateAttendanceRecord(
            int employeeNumber,
            LocalDate date,
            LocalDateTime timeOut
    ){
        // Print "updateAttendanceRecord" to indicate the method execution
        System.out.println("updateAttendanceRecord");
        int rowsAffected = 0;
        try {
            // Define the query
            String query = "UPDATE attendance_record SET "
                    + "time_out = ? "
                    + "WHERE employee_number = ? AND date = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setTimestamp(1, Timestamp.valueOf(timeOut));
            preparedStatement.setInt(2, employeeNumber);
            preparedStatement.setDate(3, java.sql.Date.valueOf(date));

            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected);
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return rowsAffected;
    }
    
    public ResultSet getLeaveRequests(int month, int year) {
        System.out.println("getLeaveRequests");
        ResultSet resultSet = null;
        try {
            // Define the query
            String query = "SELECT * FROM leave_records "
                    + "LEFT JOIN employees ON leave_records.employee_number = "
                    + "employees.employee_number WHERE "
                    + "MONTH(date_filed) = ? AND YEAR(date_filed) = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);
            
            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getLeaveRequestByEmpNumberAndYear(int employeeNumber, int year) {
        System.out.println("getLeaveRequests");
        ResultSet resultSet = null;
        try {
            // Define the query
            String query = "SELECT " +
                "COALESCE(SUM(CASE WHEN leave_type = 'EL - Emergency Leave' THEN 1 ELSE 0 END), "
                + " 0) AS emergency_leave_count, " +
                "COALESCE(SUM(CASE WHEN leave_type = 'SL - Sick Leave' THEN 1 ELSE 0 END), "
                + "0) AS sick_leave_count, " +
                "COALESCE(SUM(CASE WHEN leave_type = 'VL - Vacation Leave' THEN 1 ELSE 0 END), "
                + "0) AS vacation_leave_count " +
                "FROM leave_records " +
                "WHERE YEAR(date_filed) = ? AND employee_number = ? AND remarks = 'APPROVED'";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, employeeNumber);
            
            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public ResultSet getLeaveRequestByEmpNumber(int employeeNumber) {
        System.out.println("getLeaveRequests");
        ResultSet resultSet = null;
        try {
            // Define the query
            String query = "SELECT * FROM leave_records LEFT JOIN employees "
                    + "ON employees.employee_number = "
                    + "leave_records.approved_by "
                    + "WHERE leave_records.employee_number = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, employeeNumber);
            
            // Execute the query
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        // Return the result set containing the employee records
        return resultSet;
    }
    
    public int createLeaveRecord(
            int employeeNumber,
            int numberOfDaysLeave,
            Date leaveDateFrom,
            Date leaveDateUntil,
            String leaveType,
            String reasonForLeave
    ){
        // Print "createEmployee" to indicate the method execution
        System.out.println("createLeaveRecord");
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "INSERT INTO leave_records ("
                + "employee_number, number_of_days, leave_type, date_from, "
                + "date_until, reason) VALUES (?, ?, ?, ?, ?, ?)";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters for the prepared statement
            preparedStatement.setInt(1, employeeNumber);
            preparedStatement.setInt(2, numberOfDaysLeave);
            preparedStatement.setString(3, leaveType);
            preparedStatement.setDate(4, leaveDateFrom);
            preparedStatement.setDate(5, leaveDateUntil);
            preparedStatement.setString(6, reasonForLeave);

            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return rowsAffected;
    }
    
    public int approveRejectLeaveRecord(
            int id,
            String remarks,
            int approvedBy
    ){
        // Print "createEmployee" to indicate the method execution
        System.out.println("approveRejectLeaveRecord");
        int rowsAffected = 0;
        try {
            // Define the query
            String query = "UPDATE leave_records SET remarks = ?, approved_by = ?"
                    + " WHERE id = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, remarks);
            preparedStatement.setInt(2, approvedBy);
            preparedStatement.setInt(3, id);
            

            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return rowsAffected;
    }
}

    
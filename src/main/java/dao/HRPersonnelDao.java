/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import models.Address;
import models.AttendanceRecord;
import models.Benefits;
import models.Deductions;
import models.Employee;
import models.HRPersonnel;
import models.LeaveRequest;
import models.Payroll;
import models.SystemAdministrator;
import models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.DbConnection;

/**
 *
 * @author Alex Resurreccion
 */
public class HRPersonnelDao {
    private Connection connection;
    private Statement statement = null;
    private ResultSet result = null;
    private PreparedStatement preparedStatement = null;

    // Constructor with custom connection
    public HRPersonnelDao(Connection connection) {
        this.connection = connection;
    }

    // Constructor with default connection
    public HRPersonnelDao() {
        this.connection = DbConnection.getConnection();
    }
    
    /**
     * Approves a leave request for an employee.
     * 
     * @param leaveRequest The leave request to approve.
     * @return true if leave request is approved, false otherwise.
     */
    public boolean approveLeaveRequest(LeaveRequest leaveRequest, HRPersonnel hrPersonnel) {
        
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE leave_records " +
               "SET approved_by = ?, remarks = 'APPROVED' " +
               "WHERE id = ? AND remarks = 'PENDING'";

            // Prepare the statement
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, hrPersonnel.getEmployeeID());
            preparedStatement.setInt(2, leaveRequest.getLeaveID());
            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 1) {
                leaveRequest.setApprovedBy(hrPersonnel);
                leaveRequest.setLeaveStatus("APPROVED");
                EmailSenderDao emailSenderDao = new EmailSenderDao();
                emailSenderDao.newLeaveUpdateNotification(leaveRequest);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return rowsAffected == 1;
    }
    
     /**
     * Declines a leave request for an employee.
     * 
     * @param leaveRequest The leave request to decline.
     * @return true if leave request is declined, false otherwise.
     */
    public boolean declineLeaveRequest(LeaveRequest leaveRequest, HRPersonnel hrPersonnel) {
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "UPDATE leave_records "
                    + "SET approved_by = ?, remarks = 'DECLINED' WHERE id = ? AND remarks = 'PENDING'";

            // Prepare the statement
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, hrPersonnel.getEmployeeID());
            preparedStatement.setInt(2, leaveRequest.getLeaveID());
            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 1){
                leaveRequest.setApprovedBy(hrPersonnel);
                leaveRequest.setLeaveStatus("DECLINED");
                EmailSenderDao emailSenderDao = new EmailSenderDao();
                emailSenderDao.newLeaveUpdateNotification(leaveRequest);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return rowsAffected == 1;
    }
    
    /**
     * Adds a new employee to the system.
     * 
     * @param employee The employee to add.
     * @return true if employee is added successfully, false otherwise.
     */
    public boolean addNewEmployee(Employee employee) {
        int employeeId = 0;
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
            psEmp.executeUpdate();

            ResultSet rsEmp = psEmp.getGeneratedKeys();
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
            psGov.executeUpdate();

            // 5. Insert into user_credentials
            String credQuery = "INSERT INTO user_credentials (employee_id, password, role_id) VALUES (?, ?, ?)";
            PreparedStatement psCred = connection.prepareStatement(credQuery);
            psCred.setInt(1, employeeId);
            psCred.setString(2, employee.getLastName() + "123");
            psCred.setInt(3, getEmployeeRoleId());
            rowsAffected = psCred.executeUpdate();
            
            if(initialCommit){
                connection.commit(); // Commit if all succeeded
                connection.setAutoCommit(initialCommit);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            System.out.println("SQL Error Message: " + e.getMessage());
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
    
    private int getEmployeeRoleId() throws SQLException {
        String query = "SELECT role_id FROM roles WHERE role_name = 'Employee'";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("role_id") : 0;
    }
    
    /**
     * Updates an existing employee's details in the system.
     * 
     * @param employee The employee to update.
     * @return true if employee is updated successfully, false otherwise.
     */
    public boolean updateEmployee(Employee employee) {
        int rowsAffected = 0;

        try {
            // Update employees table
            String query = "UPDATE employees SET last_name = ?, first_name = ?, birthdate = ?, "
                         + "phone_number = ?, status_id = ?, position_id = ?, supervisor_id = ? "
                         + " WHERE employee_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employee.getLastName());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setDate(3, new java.sql.Date(employee.getBirthday().getTime()));
            preparedStatement.setString(4, employee.getPhoneNumber());
            preparedStatement.setInt(5, getStatusId(employee.getStatus()));
            preparedStatement.setInt(6, getPositionId(employee.getPosition()));
            preparedStatement.setObject(7, employee.getImmediateSupervisor(), java.sql.Types.INTEGER); // Nullable
            preparedStatement.setInt(8, employee.getEmployeeID());

            System.out.println("Executing Query: " + preparedStatement.toString());
            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while executing employee update SQL!");
            System.out.println("SQL Error Message: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected == 1;
    }
    
    /**
     * Deletes an employee from the system based on their employee ID.
     * 
     * @param employee The employee to delete.
     * @return true if employee is deleted successfully, false otherwise.
     */
    public boolean deleteEmployee(Employee employee) {
        int rowsAffected = 0;
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
    
    /**
     * Processes payroll for all employees in a specified date range.
     * 
     * @param startDate The start date for payroll processing.
     * @param endDate The end date for payroll processing.
     * @return true if payroll is successfully processed, false otherwise.
     */
    public boolean processPayroll(Date startDate, Date endDate, HRPersonnel hrPersonnel) {
        try {
            String checkSql = "SELECT id FROM payroll WHERE period_start = ? AND period_end = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(checkSql);
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
            result = preparedStatement.executeQuery();

            if (result.next()) {
                // Payroll already exists
                return false;
            }

            // Insert new payroll record
            String insertSql = "INSERT INTO payroll ("
                    + "processed_by_employee_id, period_start, period_end, date_generated, status) "
                    + "VALUES (?, ?, ?, ?, 'PENDING')";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setInt(1, hrPersonnel.getEmployeeID());
            insertStmt.setDate(2, new java.sql.Date(startDate.getTime()));
            insertStmt.setDate(3, new java.sql.Date(endDate.getTime()));
            insertStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));

            int rowsInserted = insertStmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error fetching employee details:");
            e.printStackTrace();
        }

        return false;
    }

    
    /**
     * Loads the list of employees from the database.
     * 
     * @return A list of employees.
     */
    public List<User> loadEmployeeList(){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.* FROM employee_view e "
                    + "INNER JOIN user_credentials uc "
                    + "ON e.employee_id = uc.employee_id "
                    + "WHERE uc.role_id = 1 AND e.is_deleted = False";

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
    
    /**
     * Loads a filtered list of employees based on a search query.
     * 
     * @param search The search query for employee filtering.
     * @return A list of filtered employees.
     */
    public List<User> loadEmployeeList(String search){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT * FROM employee_view e INNER JOIN user_credentials uc "
                    + "ON e.employee_id = uc.employee_id WHERE uc.role_id = 1 "
                    + "AND is_deleted = False AND "
                    + "(e.employee_id LIKE ? OR first_name LIKE ? OR "
                    + "last_name LIKE ?)";
            
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
    
    public List<User> loadNewEmployees(){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT e.*, uc.role_id FROM employee_view e "
                    + "LEFT JOIN user_credentials uc "
                    + " ON e.employee_id = uc.employee_id "
                    + "WHERE date_hired >= CURDATE() - INTERVAL 7 DAY "
                    + "AND role_id = 1 ";
            
            result = statement.executeQuery(query);
            List<User> employees = mapEmployees(result);
        return employees;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Loads the list of attendance records from the database.
     * 
     * @return A list of attendance records.
     */
    public List<AttendanceRecord> loadAttendanceList(){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT a.*, e.first_name, e.last_name, p.name AS position " +
               "FROM attendance_record a " +
               "INNER JOIN employees e ON e.employee_id = a.employee_id " +
               "INNER JOIN positions p ON p.position_id = e.position_id " +
               "ORDER BY a.date DESC";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            result = preparedStatement.executeQuery();
            List<AttendanceRecord> attendances = mapAttendance(result);
            return attendances;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Loads the attendance records for a specific date range.
     * 
     * @param startDate The start date for the attendance records.
     * @param endDate The end date for the attendance records.
     * @return A list of attendance records for the given date range.
     */
    public List<AttendanceRecord> loadAttendanceList(
            Date startDate, Date endDate){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT a.*, e.first_name, e.last_name, p.name AS position " +
               "FROM attendance_record a " +
               "INNER JOIN employees e ON e.employee_id = a.employee_id " +
               "INNER JOIN positions p ON p.position_id = e.position_id " +
               "WHERE a.date BETWEEN ? AND ?";
            query += "ORDER BY date DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));

            result = preparedStatement.executeQuery();
            List<AttendanceRecord> attendances = mapAttendance(result);
            return attendances;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
    
     /**
     * Loads the summary payroll report for a specific month and year.
     * 
     * @param month The month for the payroll summary.
     * @param year The year for the payroll summary.
     * @return A list of payroll summary records for the given month and year.
     */
    public ResultSet loadSummaryReport(int month, int year){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT * FROM payroll_summary_report_view WHERE `Month` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String period = String.format("%04d-%02d", year, month);
            preparedStatement.setString(1, period);
            
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            System.out.println("SQL Error Message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Maps a ResultSet containing employee data to a List of User objects.
    private List<User> mapEmployees(ResultSet result){
        List<User> employees = new ArrayList<>();
        try {
            while (result.next()) {
                Employee emp = mapEmployee(result);
                
                employees.add(emp);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return employees;
    }
    
    // Maps a single row of ResultSet to an Employee object
    private Employee mapEmployee(ResultSet result){
        Employee emp = new Employee();
        try { 
            emp.setEmployeeID(result.getInt("employee_id"));
            emp.setLastName(result.getString("last_name"));
            emp.setFirstName(result.getString("first_name"));
            emp.setBirthday(result.getDate("birthdate"));
            emp.setPhoneNumber(result.getString("phone_number"));
            emp.setSssNumber(result.getString("sss_number"));
            emp.setPhilhealthNumber(result.getString("philhealth_number"));
            emp.setTinNumber(result.getString("tin_number"));
            emp.setPagibigNumber(result.getString("pagibig_number"));
            emp.setStatus(result.getString("status"));
            emp.setPosition(result.getString("position"));
            emp.setImmediateSupervisor(result.getInt("immediate_supervisor"));
            emp.setDateHired(result.getDate("date_hired"));

            Benefits benefit = new Benefits(
                    result.getInt("basic_salary"),
                    result.getInt("gross_semi_monthly_rate"),
                    result.getInt("hourly_rate"),
                    result.getInt("rice_subsidy"),
                    result.getInt("phone_allowance"),
                    result.getInt("clothing_allowance")
                );
            emp.setBenefits(benefit);

            Address address = new Address();
            address.setBarangay(result.getString("barangay"));
            address.setCity(result.getString("date_hired"));
            address.setProvince(result.getString("province"));
            address.setStreet(result.getString("street"));
            address.setZipcode(result.getString("date_hired"));
            emp.setAddress(address);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return emp;
    }
    
    // Maps a single row of ResultSet to a Payroll object
    private Payroll mapPayroll(ResultSet result){
        Payroll pr = new Payroll();
        try { 
            pr.setGrossIncome(result.getDouble("gross_income"));
            pr.setHoursWorked(result.getDouble("hours_worked"));
            pr.setNetPay(result.getDouble("take_home_pay"));
            pr.setOvertimeHours(result.getDouble("overtime_hours"));
//            pr.setPaymentDate(result.getDate("payment_date"));
            pr.setPayrollID(result.getString("payslip_no"));
            pr.setWeekPeriodEnd(result.getDate("period_end"));
            pr.setWeekPeriodStart(result.getDate("period_start"));
            
            Deductions deduction = new Deductions();
            deduction.setPagIbigDeduction(result.getDouble("pagibig_deduction"));
            deduction.setPhilhealthDeduction(result.getDouble("philhealth_deduction"));
            deduction.setSssDeduction(result.getDouble("sss_deduction"));
            deduction.setTaxDeduction(result.getDouble("withholding_deduction"));
            pr.setDeductions(deduction);
            
            Employee emp = new Employee();
            String fullName = result.getString("employee_name"); 
            String[] parts = fullName.split(", ", 2); 
            String lastName = parts[0].trim();
            String firstName = parts[1].trim();

            emp.setLastName(lastName);
            emp.setFirstName(firstName);
            
            String positionDepartment = result.getString("position_department"); 
            parts = positionDepartment.split(" / ", 2); 
            String position = parts[0].trim();
            String department = parts[1].trim();
            emp.setEmployeeID(result.getInt("employee_id"));
            emp.setPosition(position);
            emp.setDepartment(department);

            Benefits benefit = new Benefits(
                    result.getDouble("basic_salary"),
                    result.getDouble("gross_income"),
                    result.getDouble("hourly_rate"),
                    result.getDouble("rice_subsidy"),
                    result.getDouble("phone_allowance"),
                    result.getDouble("clothing_allowance")
                );
            emp.setBenefits(benefit);
            pr.setEmployee(emp);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return pr;
    }
    
    // Maps a ResultSet containing attendance data to a List of AttendanceRecord objects
    private List<AttendanceRecord> mapAttendance(ResultSet result){
        List<AttendanceRecord> attendance = new ArrayList<>();
        try { 
            while (result.next()) {
                AttendanceRecord record = new AttendanceRecord(
                        result.getInt("employee_id"),
                        result.getDate("date"),
                        result.getTime("time_in"),
                        result.getTime("time_out"));
                attendance.add(record);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return attendance;
    }
    
    // Loads payroll list within a date range and specific status into a list of Maps
    public List<Map<String, Object>> loadPayrollList(Date startDate, Date endDate){
        List payroll = new ArrayList<>();
        try {
            // Define the query
            String query = "SELECT * " +
                    "FROM payslip_view p\n" +
                    "INNER JOIN user_credentials uc "
                    + "ON p.employee_id = uc.employee_id\n" +
                    "INNER JOIN roles r ON uc.role_id = r.role_id\n" +
                    "WHERE r.role_name = 'Employee' " +
                    "    AND period_start = ? " + 
                    "    AND period_end = ? " ;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            result = preparedStatement.executeQuery();
            while (result.next()) {
                // Store data in a Map instead of a separate class
                Map<String, Object> payrollData = new HashMap<>();
                payrollData.put("payslip_no", result.getString("payslip_no"));
                payrollData.put("employee_id", result.getString("employee_id"));
                payrollData.put("employee_name", result.getString("employee_name"));
                payrollData.put("position_department", result.getString("position_department"));
                payrollData.put("payPeriod", result.getDate("period_start")
                        + " - " + result.getDate("period_end"));
                payrollData.put("take_home_pay", result.getDouble("take_home_pay"));
//                payrollData.put("status", result.getString("payslip_status"));

                payroll.add(payrollData);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return payroll;
    }
    
    // Loads all leave requests
    public List<LeaveRequest> loadLeaveRequest(){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT " +
               "l.*, " +
               "lt.type_name AS leave_type, " +
               "d.name AS department, " +
               "e.first_name, " +
               "e.last_name, " +
               "e2.employee_id AS approver_id, " +
               "e2.first_name AS approver_first_name, " +
               "e2.last_name AS approver_last_name " +
               "FROM leave_records l " +
               "LEFT JOIN employees e ON e.employee_id = l.employee_id " +
               "LEFT JOIN employees e2 ON e2.employee_id = l.approved_by " +
               "LEFT JOIN positions p ON e.position_id = p.position_id " +
               "LEFT JOIN department d ON p.department_id = d.department_id " +
               "LEFT JOIN leave_types lt ON l.leave_type_id = lt.leave_type_id " +
               "ORDER BY date_filed DESC";

            // Execute the query
            result = statement.executeQuery(query);
            List<LeaveRequest> leaves = mapLeaveRequest(result);
            return leaves;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
    
    // Loads leave requests based on a search filter
    public List<LeaveRequest> loadLeaveRequest(String search){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT " +
               "l.*, " +
               "lt.type_name AS leave_type, " +
               "d.name AS department, " +
               "e.employee_id, " +
               "e.first_name, " +
               "e.last_name, " +
               "e2.employee_id AS approver_id, " +
               "e2.first_name AS approver_first_name, " +
               "e2.last_name AS approver_last_name " +
               "FROM leave_records l " +
               "LEFT JOIN employees e ON e.employee_id = l.employee_id " +
               "LEFT JOIN employees e2 ON e2.employee_id = l.approved_by " +
               "LEFT JOIN positions p ON e.position_id = p.position_id " +
               "LEFT JOIN department d ON p.department_id = d.department_id " +
               "LEFT JOIN leave_types lt ON l.leave_type_id = lt.leave_type_id ";
            
            if(!search.equals("All")){
                query += "WHERE remarks = ? ";
            }
            query += " ORDER BY date_filed DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            if(!search.equals("All")){
                preparedStatement.setString(1, search);
            }

            // Execute the query
            result = preparedStatement.executeQuery();
            List<LeaveRequest> leaves = mapLeaveRequest(result);
            return leaves;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
    
    // Maps a ResultSet containing leave requests to a List of LeaveRequest objects
    private List<LeaveRequest> mapLeaveRequest(ResultSet result){
        List<LeaveRequest> leaves = new ArrayList<>();
        try { 
            while (result.next()) {
                Employee emp = new Employee();
                emp.setEmployeeID(result.getInt("employee_id"));
                emp.setLastName(result.getString("last_name"));
                emp.setFirstName(result.getString("first_name"));
                emp.setDepartment(result.getString("department"));
                
                LeaveRequest leave = new LeaveRequest();
                leave.setLeaveID(result.getInt("id"));
                leave.setEmployee(emp);
                
                leave.setDateCreated(result.getDate("date_filed"));
                leave.setStartDate(result.getDate("date_from"));
                leave.setEndDate(result.getDate("date_until"));
                leave.setLeaveType(result.getString("leave_type"));
                leave.setDuration(result.getInt("number_of_days"));
                leave.setReason(result.getString("reason"));
                leave.setLeaveStatus(result.getString("remarks"));
                
                if(result.getInt("approver_id") != 0) {
                    HRPersonnel approver = new HRPersonnel();
                    approver.setEmployeeID(result.getInt("approver_id"));
                    approver.setFirstName(result.getString("approver_first_name"));
                    approver.setLastName(result.getString("approver_last_name"));
                    
                    leave.setApprovedBy(approver);
                }
                
                leaves.add(leave);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return leaves;
    }
    
    // Loads a specific payslip for an employee within a date range
    public Payroll loadPayslip(Date startDate, Date endDate, Employee employee){
        Payroll payslip = new Payroll();
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT * FROM payslip_view p "
                    + "WHERE period_start = ? AND period_end = ? "
                    + "AND employee_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
            
            preparedStatement.setInt(3, employee.getEmployeeID());

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            result = preparedStatement.executeQuery();
            if(result.next()){
                payslip = mapPayroll(result);
                return payslip;
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
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
                    + " AS new_employees FROM employees e "
                    + " INNER JOIN user_credentials uc "
                    + " ON e.employee_id = uc.employee_id"
                    + " WHERE role_id = 1";

            result = statement.executeQuery(query);
            List<Integer> dashboardInfo = new ArrayList<>();
            if (result.next()) {
                dashboardInfo.add(result.getInt("total"));
                dashboardInfo.add(result.getInt("new_employees"));
            }
            return dashboardInfo;
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
}

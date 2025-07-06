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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class EmployeeDao {
    private Connection conn;
    private Statement statement = null;
    private ResultSet rs = null;
    private PreparedStatement preparedStatement = null;

    // Constructor with custom connection
    public EmployeeDao(Connection conn) {
        this.conn = conn;
    }

    // Constructor with default connection
    public EmployeeDao() {
        this.conn = DbConnection.getConnection();
    }
    
    
    public boolean save(
            String street, String barangay, String city,  
            String province, String zipcode, String phoneNumber, int employeeNumber){
        try {
            // Step 1: Get employee_id and address_id
            String selectQuery = "SELECT employee_id, address_id FROM employees "
                    + "WHERE employee_id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setInt(1, employeeNumber);
            
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                int addressId = rs.getInt("address_id");

                // Step 2: Update address
                String addressQuery = "UPDATE address SET street = ?, "
                        + " barangay = ?, city = ?, province = ?, zipcode = ? "
                        + "WHERE address_id = ?";
                PreparedStatement addressStmt = conn.prepareStatement(addressQuery);
                addressStmt.setString(1, street);
                addressStmt.setString(2, barangay);
                addressStmt.setString(3, city);
                addressStmt.setString(4, province);
                addressStmt.setString(5, zipcode);
                addressStmt.setInt(6, addressId);

                int addressRows = addressStmt.executeUpdate();

                // Step 3: Update phone number in employees
                String employeeQuery = "UPDATE employees SET phone_number = ? "
                        + "WHERE employee_id = ?";
                PreparedStatement empStmt = conn.prepareStatement(employeeQuery);
                empStmt.setString(1, phoneNumber);
                empStmt.setInt(2, employeeId);

                int employeeRows = empStmt.executeUpdate();
                
                return addressRows == 1 && employeeRows == 1;
            } else {
                System.out.println("Employee not found.");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return false;
    }
    
    // Create a new leave request for the employee
    public boolean createLeaveRequest(LeaveRequest leave){
        int rowsAffected = 0;
        try {
            // Create a statement object
            statement = conn.createStatement();
            

            // Define the query
            String typeQuery = "SELECT leave_type_id FROM leave_types WHERE type_name = ?";
            PreparedStatement typeStmt = conn.prepareStatement(typeQuery);
            typeStmt.setString(1, leave.getLeaveType());
            ResultSet rs = typeStmt.executeQuery();

            int leaveTypeId = -1;
            if (rs.next()) {
                leaveTypeId = rs.getInt("leave_type_id");
            } else {
                throw new SQLException("Invalid leave type: " + leave.getLeaveType());
            }

            // Step 2: Insert into leave_records
            String query = "INSERT INTO leave_records ("
                         + "employee_id, leave_type_id, date_filed, "
                         + "date_from, date_until, number_of_days, reason, approved_by, remarks) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, leave.getEmployee().getEmployeeID());
            preparedStatement.setInt(2, leaveTypeId);
            preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            preparedStatement.setDate(4, new java.sql.Date(leave.getStartDate().getTime()));
            preparedStatement.setDate(5, new java.sql.Date(leave.getEndDate().getTime()));
            preparedStatement.setInt(6, leave.getDuration());
            preparedStatement.setString(7, leave.getReason());
            preparedStatement.setObject(8, null);
            preparedStatement.setString(9, "PENDING");
            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 1){
                EmailSenderDao emailSenderDao = new EmailSenderDao();
                emailSenderDao.newLeaveNotification(leave);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return rowsAffected == 1;
    }
    
    public List<LeaveRequest> loadLeaveRequests(int employeeNumber){
        List<LeaveRequest> leaveRequests = new ArrayList<>();   
        try {
            // Define the query
            String query = "SELECT lr.*, lt.type_name, emp_approver.employee_id AS approver_id, " +
                   "emp_approver.first_name AS approver_first_name, emp_approver.last_name AS approver_last_name " +
                   "FROM leave_records lr " +
                   "LEFT JOIN employees emp_approver ON emp_approver.employee_id = lr.approved_by " +
                   "JOIN leave_types lt ON lt.leave_type_id = lr.leave_type_id " +
                   "WHERE lr.employee_id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, employeeNumber);  // use employeeId instead of employeeNumber

            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                HRPersonnel approver = new HRPersonnel();
                approver.setEmployeeID(rs.getInt("approver_id"));
                approver.setLastName(rs.getString("approver_last_name"));
                approver.setFirstName(rs.getString("approver_first_name"));

                Employee employee = new Employee();  // Assuming you need this filled elsewhere
                LeaveRequest leaveRequest = new LeaveRequest(
                    employee,
                    rs.getInt("id"),
                    rs.getString("type_name"),
                    rs.getDate("date_from"),
                    rs.getDate("date_until"),
                    rs.getInt("number_of_days"),
                    rs.getString("remarks"),
                    approver
                );
                leaveRequests.add(leaveRequest);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return leaveRequests;
    }
    
    public List<Map<String, Object>> loadLeaveCredits(int employeeNumber){
        int maxVacation = 20, maxSick = 15, maxEmergency = 5; 
        List<Map<String, Object>> leaveCredits = new ArrayList<>();

        // Create first dictionary (map)
        
        int currentYear = LocalDate.now().getYear();
    
        try {
            // Define the query
            String query = "SELECT " +
                "COALESCE(SUM(CASE WHEN lt.type_name = 'Emergency Leave' "
                    + "THEN 1 ELSE 0 END), 0) AS emergency_leave_count, " +
                "COALESCE(SUM(CASE WHEN lt.type_name = 'Sick Leave' "
                    + "THEN 1 ELSE 0 END), 0) AS sick_leave_count, " +
                "COALESCE(SUM(CASE WHEN lt.type_name = 'Vacation Leave' "
                    + "THEN 1 ELSE 0 END), 0) AS vacation_leave_count " +
                "FROM leave_records lr " +
                "JOIN leave_types lt ON lt.leave_type_id = lr.leave_type_id " +
                "WHERE YEAR(lr.date_filed) = ? AND lr.employee_id = ? "
                    + "AND lr.remarks = 'APPROVED'";

            // Prepare the statement
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            // Set the parameters
            preparedStatement.setInt(1, currentYear);
            preparedStatement.setInt(2, employeeNumber);
            
            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            rs = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        try { 
            if (rs.next()) {
                System.out.println(rs.getInt("emergency_leave_count"));
                Map<String, Object> el = new HashMap<>();
                el.put("Type", "Emergency Leave");
                el.put("Allowable", maxEmergency); // Hardcoded allowable limit
                el.put("Available", maxEmergency - rs.getInt("emergency_leave_count"));
                leaveCredits.add(el);

                // Map for SL
                Map<String, Object> sl = new HashMap<>();
                sl.put("Type", "Sick Leave");
                sl.put("Allowable", maxSick);
                sl.put("Available", maxSick - rs.getInt("sick_leave_count"));
                leaveCredits.add(sl);

                // Map for VL
                Map<String, Object> vl = new HashMap<>();
                vl.put("Type", "Vaction Leave");
                vl.put("Allowable", maxVacation);
                vl.put("Available", maxVacation - rs.getInt("vacation_leave_count"));
                leaveCredits.add(vl);
            } else {
                Map<String, Object> el = new HashMap<>();
                el.put("Type", "Emergency Leave");
                el.put("Allowable", maxEmergency); 
                el.put("Available", maxEmergency);
                leaveCredits.add(el);
                
                // Map for SL
                Map<String, Object> sl = new HashMap<>();
                sl.put("Type", "Sick Leave");
                sl.put("Allowable", maxSick);
                sl.put("Available", maxSick);
                leaveCredits.add(sl);

                // Map for VL
                Map<String, Object> vl = new HashMap<>();
                vl.put("Type", "Vacation Leave");
                vl.put("Allowable", maxVacation);
                vl.put("Available", maxVacation);
                leaveCredits.add(vl);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return leaveCredits;
    }
    
    // Load employee's attendance records from the database
    public List<AttendanceRecord> loadAttendanceList(int employeeId){
        List<AttendanceRecord> attendances = null;
        try {
            // Create a statement object
            statement = conn.createStatement();

            // Define the query
            String query = "SELECT a.*, e.first_name, e.last_name, p.name AS position " +
               "FROM attendance_record a " +
               "INNER JOIN employees e ON e.employee_id = a.employee_id " +
               "INNER JOIN positions p ON p.position_id = e.position_id ";
            if(employeeId != 0){
                query += "WHERE a.employee_id = ? ";
            }
            query += "ORDER BY a.date DESC";
            
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            if(employeeId != 0){
                preparedStatement.setInt(1, employeeId);
            }

            rs = preparedStatement.executeQuery();
            attendances = mapAttendance(rs);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return attendances;
    }
    
    // Load attendance records for a specific date range
    public List<AttendanceRecord> loadAttendanceList(
            Date startDate, Date endDate, int employeeId){
        List<AttendanceRecord> attendances = null;
        try {
            // Create a statement object
            statement = conn.createStatement();

            // Define the query
            String query = "SELECT a.*, e.first_name, e.last_name, p.name AS position " +
               "FROM attendance_record a " +
               "INNER JOIN employees e ON e.employee_id = a.employee_id " +
               "INNER JOIN positions p ON p.position_id = e.position_id " +
               "WHERE a.date BETWEEN ? AND ? " +
               "AND a.employee_id = ? " +
               "ORDER BY a.date DESC";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
            preparedStatement.setInt(3, employeeId);

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            rs = preparedStatement.executeQuery();
            attendances = mapAttendance(rs);

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return attendances;
    }
    
    // Helper method to map attendance records from the ResultSet
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
    
    // Method to load the employee's payslip based on a date range
    public Payroll loadPayslip(int employeeId, Date startDate, Date endDate){
        // Query the database to retrieve payslip details based on the employee and date range
        try {
            // Create a statement object
            statement = conn.createStatement();

            // Define the query
            String query = "SELECT * FROM payslip_view "
                    + "WHERE period_start = ? AND period_end = ? "
                    + "AND employee_id = ? ";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
            preparedStatement.setInt(3, employeeId);

            // Execute the query
            System.out.println("Executing Query: " + preparedStatement.toString());
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                return mapPayroll(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
    
    // Helper method to map the ResultSet data to a Payroll object
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
 
    // Methods for Time-In and Time-Out (attendance management)
    public void timeIn(int employeeNumber) {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
        
        try {
            // Define the query
            String query = "INSERT INTO attendance_record ("
                    + "employee_id, date, time_in) VALUES (?, ?, ?)" +
                      "ON DUPLICATE KEY UPDATE time_in = VALUES(time_in)";;

            // Prepare the statement
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            // Set the parameters for the prepared statement
            preparedStatement.setInt(1, employeeNumber);
            preparedStatement.setDate(2, java.sql.Date.valueOf(currentDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(currentTime));

            // Execute the query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
    }
    
    public void timeOut(int employeeNumber) {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
       int rowsAffected = 0;
        try {
            // Define the query
            String query = "UPDATE attendance_record SET "
                    + "time_out = ? "
                    + "WHERE employee_id = ? AND date = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            // Set the parameters
            preparedStatement.setTimestamp(1, Timestamp.valueOf(currentTime));
            preparedStatement.setInt(2, employeeNumber);
            preparedStatement.setDate(3, java.sql.Date.valueOf(currentDate));

            // Execute the query
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected);
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
    }
    
    public Employee getEmployeeDetailsByNumber (int employeeNumber) {
        try {
            // Create a statement object
            statement = conn.createStatement();

            // Define the query
            String query = "SELECT * FROM employee_view WHERE employee_id = ?";
            
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, employeeNumber);

            // Execute the query
            rs = preparedStatement.executeQuery();
            
            if(rs.next()){
                Employee emp = new Employee();
                emp.setEmployeeID(rs.getInt("employee_id"));
                emp.setLastName(rs.getString("last_name"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setBirthday(rs.getDate("birthdate"));
                emp.setPhoneNumber(rs.getString("phone_number"));
                emp.setSssNumber(rs.getString("sss_number"));
                emp.setPhilhealthNumber(rs.getString("philhealth_number"));
                emp.setTinNumber(rs.getString("tin_number"));
                emp.setPagibigNumber(rs.getString("pagibig_number"));
                emp.setStatus(rs.getString("status"));
                emp.setPosition(rs.getString("position"));
                emp.setImmediateSupervisor(rs.getInt("immediate_supervisor"));
                emp.setDateHired(rs.getDate("date_hired"));

                Benefits benefit = new Benefits(
                        rs.getInt("basic_salary"),
                        rs.getInt("gross_semi_monthly_rate"),
                        rs.getInt("hourly_rate"),
                        rs.getInt("rice_subsidy"),
                        rs.getInt("phone_allowance"),
                        rs.getInt("clothing_allowance")
                    );
                emp.setBenefits(benefit);

                Address address = new Address();
                address.setBarangay(rs.getString("barangay"));
                address.setCity(rs.getString("city"));
                address.setProvince(rs.getString("province"));
                address.setStreet(rs.getString("street"));
                address.setZipcode(rs.getString("zipcode"));
                emp.setAddress(address);

                return emp;
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        return null;
    }
}



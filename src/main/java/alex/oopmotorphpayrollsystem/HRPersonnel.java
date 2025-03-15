/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;

import alex.oopmotorphpayrollsystem.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author Alex Resurreccion
 */
public class HRPersonnel extends User implements PayrollProcessor, LeaveApprover {
    private List<Employee> employees;
    
    public HRPersonnel(int employeeID) {
        super(employeeID);
    }
    public HRPersonnel() {}
    
    public boolean approveLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequest.setApprovedBy(this);
        MySQL mySQL = new MySQL();
        boolean result = mySQL.approveLeaveRequest(leaveRequest);
        mySQL.close();
        leaveRequest.setLeaveStatus("APPROVED");
        return result;
        
    }
    
    public boolean declineLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequest.setApprovedBy(this);
        MySQL mySQL = new MySQL();
        boolean result = mySQL.declineLeaveRequest(leaveRequest);
        mySQL.close();
        leaveRequest.setLeaveStatus("DECLINED");
        return result;
    }
    
    public boolean addNewEmployee(Employee employee) {
        MySQL mySQL = new MySQL();
        boolean result = mySQL.createEmployee(employee);
        mySQL.close();
        return result;
    }
    
    public boolean updateEmployee(Employee employee) {
        MySQL mySQL = new MySQL();
        boolean result = mySQL.updateEmployee(employee);
        
        mySQL.close();
        return result;
    }
    
    public boolean deleteEmployee(Employee employee) {
        MySQL mySQL = new MySQL();
        int result = mySQL.deleteEmployeeByNumber(employee.getEmployeeID());
        mySQL.close();
        return result == 1;
    }
    
    public boolean processPayroll(Date startDate, Date endDate) {
        MySQL mySQL = new MySQL();
        ResultSet rs = mySQL.getPayslipList(
                new java.sql.Date(startDate.getTime()), 
                new java.sql.Date(endDate.getTime()),
                "All");
        List<User> employees = mapEmployees(rs);  

        rs = mySQL.getAttendanceByPeriod(
                new java.sql.Date(startDate.getTime()), 
                new java.sql.Date(endDate.getTime()));
        List<AttendanceRecord> allAttendanceRecords = mapAttendance(rs);   

        // Map the attendance records to employees for easy access
        Map<Integer, List<AttendanceRecord>> attendanceMap = new HashMap<>();
        for (AttendanceRecord record : allAttendanceRecords) {
            attendanceMap
                .computeIfAbsent(record.getEmployeeId(), k -> new ArrayList<>())
                .add(record);
        }
        
        List<Payroll> payslips = new ArrayList<>();
        for(User emp: employees){
            List<AttendanceRecord> employeeAttendanceRecords = attendanceMap.get(emp.getEmployeeID());
            // Calculate total work hours for the employee
            int totalWorkHours = 0;
            if (employeeAttendanceRecords != null) {
                for (AttendanceRecord attendance : employeeAttendanceRecords) {
                    totalWorkHours += attendance.calculateWorkHours(); // Add work hours
                }
            }
            
            Payroll pr = new Payroll((Employee) emp, totalWorkHours, startDate, endDate);
            payslips.add(pr);
            // generate all payslip
        }
        
        mySQL.close();
        return generatePayslip(payslips);
        // You can add logic to update database, generate payslip, etc.
    }
    
    private boolean generatePayslip(List<Payroll> payroll){
        MySQL mySQL = new MySQL();
        int result = mySQL.generatePayslips(payroll);
        mySQL.close();
        return result > 0;
    }
    
    @Override
    public void approveLeave(LeaveRequest leaveRequest) {
        approveLeaveRequest(leaveRequest);
    }
    
    public List<User> loadEmployeeList(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getEmployeeUsers();
        List<User> employees = mapEmployees(result);
        
        mySQL.close();
        return employees;
    }
    
    public List<User> loadEmployeeList(String search){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getEmployeesWithSearch(search);
        List<User> employees = mapEmployees(result);
        mySQL.close();
        return employees;
    }
    
    public List<AttendanceRecord> loadAttendanceList(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getAttendanceList(0);
        List<AttendanceRecord> attendances = mapAttendance(result);
        mySQL.close();
        return attendances;
    }
    
    public List<AttendanceRecord> loadAttendanceList(
            Date startDate, Date endDate){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getAttendanceList(
                0, new java.sql.Date(startDate.getTime()), 
                new java.sql.Date(endDate.getTime()));
        List<AttendanceRecord> attendances = mapAttendance(result);
        mySQL.close();
        return attendances;
    }
    
    public List<Payroll> loadSummaryReport(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getSummaryList();
        List<Payroll> payrolls = mapPayrolls(result);
        mySQL.close();
        return payrolls;
    }
    
    public List<Payroll> loadSummaryReport(int month, int year){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getSummaryList(month, year);
        List<Payroll> payrolls = mapPayrolls(result);
        mySQL.close();
        return payrolls;
    }
    
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
    
    private Employee mapEmployee(ResultSet result){
        Employee emp = new Employee();
        try { 
            emp.setEmployeeID(result.getInt("employee_number"));
            emp.setLastName(result.getString("last_name"));
            emp.setFirstName(result.getString("first_name"));
            emp.setAddress(result.getString("address"));
            emp.setBirthday(result.getDate("birthdate"));
            emp.setPhoneNumber(result.getString("phone_number"));
            emp.setSssNumber(result.getString("sss_number"));
            emp.setPhilhealthNumber(result.getString("philhealth_number"));
            emp.setTinNumber(result.getString("tin_number"));
            emp.setPagibigNumber(result.getString("pagibig_number"));
            emp.setStatus(result.getString("status"));
            emp.setPosition(result.getString("position"));
            emp.setImmediateSupervisor(result.getString("immediate_supervisor"));

            Benefits benefit = new Benefits(
                    result.getInt("basic_salary"),
                    result.getInt("gross_semi_monthly_rate"),
                    result.getInt("hourly_rate"),
                    result.getInt("rice_subsidy"),
                    result.getInt("phone_allowance"),
                    result.getInt("clothing_allowance")
                );
            emp.setBenefits(benefit);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return emp;
    }
    
    private List<Payroll> mapPayrolls(ResultSet result){
        List<Payroll> payroll = new ArrayList<>();
        try {
            while (result.next()) {
                Payroll pr = mapPayroll(result);
                
                payroll.add(pr);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return payroll;
    }
    
    private Payroll mapPayroll(ResultSet result){
        Payroll pr = new Payroll();
        try { 
            pr.setGrossIncome(result.getInt("gross_income"));
            pr.setHoursWorked(result.getInt("hours_worked"));
            pr.setNetPay(result.getInt("take_home_pay"));
            pr.setOvertimeHours(result.getInt("overtime_hours"));
            pr.setPaymentDate(result.getDate("payment_date"));
            pr.setPayrollID(result.getInt("id"));
            pr.setWeekPeriodEnd(result.getDate("period_end"));
            pr.setWeekPeriodStart(result.getDate("period_start"));
            
            Deductions deduction = new Deductions();
            deduction.setPagIbigDeduction(result.getInt("pagibig_deduction"));
            deduction.setPhilhealthDeduction(result.getInt("philhealth_deduction"));
            deduction.setSssDeduction(result.getInt("sss_deduction"));
            deduction.setTaxDeduction(result.getInt("withholding_tax"));
            pr.setDeductions(deduction);
            
            Employee emp = new Employee();
            emp.setEmployeeID(result.getInt("employee_number"));
            emp.setLastName(result.getString("last_name"));
            emp.setFirstName(result.getString("first_name"));
            emp.setPosition(result.getString("position"));
            emp.setDepartment(result.getString("department"));

            Benefits benefit = new Benefits(
                    result.getInt("monthly_rate"),
                    result.getInt("gross_income"),
                    result.getInt("hourly_rate"),
                    result.getInt("rice_subsidy"),
                    result.getInt("phone_allowance"),
                    result.getInt("clothing_allowance")
                );
            emp.setBenefits(benefit);
            pr.setEmployee(emp);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return pr;
    }
    
    private List<AttendanceRecord> mapAttendance(ResultSet result){
        List<AttendanceRecord> attendance = new ArrayList<>();
        try { 
            while (result.next()) {
                AttendanceRecord record = new AttendanceRecord(
                        result.getInt("employee_number"),
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
    
    public List<Map<String, Object>> loadPayrollList(Date startDate, Date endDate, String status){
        List payroll = new ArrayList<>();
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getPayslipList(
                new java.sql.Date(startDate.getTime()), 
                new java.sql.Date(endDate.getTime()), 
                status);
        try { 
            while (result.next()) {
                // Store data in a Map instead of a separate class
                Map<String, Object> payrollData = new HashMap<>();
                payrollData.put("id", result.getInt("id"));
                payrollData.put("employeeNumber", result.getString("employee_number"));
                payrollData.put("name", result.getString("first_name")
                        + " " + result.getString("last_name"));
                payrollData.put("department", result.getString("department"));
                payrollData.put("payPeriod", result.getDate("period_start")
                        + " - " + result.getDate("period_end"));
                payrollData.put("netPay", result.getDouble("take_home_pay"));
                payrollData.put("status", result.getString("payslip_status"));

                payroll.add(payrollData);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        mySQL.close();
        
        return payroll;
    }
    
    public List<LeaveRequest> loadLeaveRequest(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getLeaveRequests();
        List<LeaveRequest> leaves = mapLeaveRequest(result);
        
        mySQL.close();
        return leaves;
    }
    
    public List<LeaveRequest> loadLeaveRequest(String search){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getLeaveRequests(search);
        List<LeaveRequest> leaves = mapLeaveRequest(result);
        mySQL.close();
        return leaves;
    }
    
    private List<LeaveRequest> mapLeaveRequest(ResultSet result){
        List<LeaveRequest> leaves = new ArrayList<>();
        try { 
            while (result.next()) {
                Employee emp = new Employee();
                emp.setEmployeeID(result.getInt("employee_number"));
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
    
    public Payroll loadPayslip(Date startDate, Date endDate, Employee employee){
        Payroll payslip = new Payroll();
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.loadPayslip(
                new java.sql.Date(startDate.getTime()), 
                new java.sql.Date(endDate.getTime()), 
                employee);
        try{
            if(result.next()){
                payslip = mapPayroll(result);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        mySQL.close();
        return payslip;
    }
}

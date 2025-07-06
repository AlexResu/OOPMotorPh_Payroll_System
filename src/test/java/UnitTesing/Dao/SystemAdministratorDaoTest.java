/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing.Dao;

import models.Address;
import models.Benefits;
import models.Employee;
import models.HRPersonnel;
import models.SystemAdministrator;
import models.User;
import dao.SystemAdministratorDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DbConnection;

/**
 *
 * @author Alex Resurreccion
 */
public class SystemAdministratorDaoTest {
    SystemAdministratorDao systemAdministratorDao = null;
    Connection connection;
    
    @BeforeEach
    public void setUp() throws SQLException {
        connection = DbConnection.getConnectionWithTransaction();
        systemAdministratorDao = new SystemAdministratorDao(connection);
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.rollback();  // Roll back everything done in the test
            connection.close();
        }
    }
    
    @Test
    public void addNewEmployeeSuccess() {
        System.out.println("Test for Add New Employee - Success");
        LocalDate localDate = LocalDate.of(1995, 6, 10);
        Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Employee employee = new Employee();
        Address address = new Address();
        address.setStreet("Kalayaan street");
        address.setBarangay("Barangay Uno");
        address.setCity("Calamba City");
        address.setProvince("Laguna");
        address.setZipcode("4027");
        employee.setAddress(address);
        Benefits benefit = new Benefits();
        benefit.setBasicSalary(30000);
        benefit.setGrossSemiMonthlyRate(15000);
        benefit.setHourlyRate(125);
        employee.setBenefits(benefit);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setPhoneNumber("099-123-456");
        employee.setBirthday(birthday);
        employee.setSssNumber("123456789");
        employee.setTinNumber("22222222");
        employee.setPhilhealthNumber("321-4535-312");
        employee.setPagibigNumber("9999321");
        employee.setStatus("Regular");
        employee.setPosition("Sales & Marketing");
        employee.setImmediateSupervisor(10001);
        boolean result = systemAdministratorDao.addNewEmployee(employee);
        assertTrue(result);
        System.out.println("Employee has been created successfully");
    }
    
    @Test
    public void addNewHRPersonnelSuccess() {
        System.out.println("Test for Add New HR Personnel - Success");
        LocalDate localDate = LocalDate.of(1995, 6, 10);
        Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        HRPersonnel employee = new HRPersonnel();
        Address address = new Address();
        address.setStreet("Kalayaan street");
        address.setBarangay("Barangay Uno");
        address.setCity("Calamba City");
        address.setProvince("Laguna");
        address.setZipcode("4027");
        employee.setAddress(address);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setPhoneNumber("099-123-456");
        employee.setBirthday(birthday);
        employee.setSssNumber("123456789");
        employee.setTinNumber("22222222");
        employee.setPhilhealthNumber("321-4535-312");
        employee.setPagibigNumber("9999321");
        employee.setPosition("Sales & Marketing");
        boolean result = systemAdministratorDao.addNewEmployee(employee);
        assertTrue(result);
        System.out.println("Employee has been created successfully");
    }
    
    @Test
    public void addNewSystemAdministratorSuccess() {
        System.out.println("Test for Add New SystemAdmin - Success");
        LocalDate localDate = LocalDate.of(1995, 6, 10);
        Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        SystemAdministrator employee = new SystemAdministrator();
        Address address = new Address();
        address.setStreet("Kalayaan street");
        address.setBarangay("Barangay Uno");
        address.setCity("Calamba City");
        address.setProvince("Laguna");
        address.setZipcode("4027");
        employee.setAddress(address);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setPhoneNumber("099-123-456");
        employee.setBirthday(birthday);
        employee.setSssNumber("123456789");
        employee.setTinNumber("22222222");
        employee.setPhilhealthNumber("321-4535-312");
        employee.setPagibigNumber("9999321");
        employee.setPosition("Sales & Marketing");
        boolean result = systemAdministratorDao.addNewEmployee(employee);
        assertTrue(result);
        System.out.println("Employee has been created successfully");
    }
    
    @Test
    public void addNewEmployeeIncomplete() {
        System.out.println("Test for Add New Employee - Incomplete");
        LocalDate localDate = LocalDate.of(1995, 6, 10);
        Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Employee employee = new Employee();
        Address address = new Address();
        address.setStreet("Kalayaan street");
        address.setBarangay("Barangay Uno");
        address.setCity("Calamba City");
        address.setProvince("Laguna");
        address.setZipcode("4027");
        employee.setAddress(address);
        Benefits benefit = new Benefits();
        benefit.setBasicSalary(30000);
        benefit.setGrossSemiMonthlyRate(15000);
        benefit.setHourlyRate(125);
        employee.setBenefits(benefit);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setBirthday(birthday);
        boolean result = systemAdministratorDao.addNewEmployee(employee);
        assertFalse(result);
        System.out.println("Incomple employee has not been created");
    }
    
    @Test
    public void addNewEmployeeDuplicateGovId() {
        System.out.println("Test for Add New Employee - DuplicateGovId");
        LocalDate localDate = LocalDate.of(1995, 6, 10);
        Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Employee employee = new Employee();
        Address address = new Address();
        address.setStreet("Kalayaan street");
        address.setBarangay("Barangay Uno");
        address.setCity("Calamba City");
        address.setProvince("Laguna");
        address.setZipcode("4027");
        employee.setAddress(address);
        Benefits benefit = new Benefits();
        benefit.setBasicSalary(30000);
        benefit.setGrossSemiMonthlyRate(15000);
        benefit.setHourlyRate(125);
        employee.setBenefits(benefit);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setPhoneNumber("099-123-456");
        employee.setBirthday(birthday);
        employee.setSssNumber("123456789");
        employee.setTinNumber("22222222");
        employee.setPhilhealthNumber("321-4535-312");
        employee.setPagibigNumber("9999321");
        boolean result = systemAdministratorDao.addNewEmployee(employee);
        assertFalse(result);
        System.out.println("Employee with duplicate gov id is not created.");
    }
    
    @Test
    public void updateEmployeeSuccess() {
        System.out.println("Test for Add New Employee - Success");
        LocalDate localDate = LocalDate.of(1995, 6, 10);
        Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Employee employee = new Employee(10034);
        Address address = new Address();
        address.setStreet("Kalayaan street");
        address.setBarangay("Barangay Uno");
        address.setCity("Calamba City");
        address.setProvince("Laguna");
        address.setZipcode("4027");
        employee.setAddress(address);
        Benefits benefit = new Benefits();
        benefit.setBasicSalary(30000);
        benefit.setGrossSemiMonthlyRate(15000);
        benefit.setHourlyRate(125);
        employee.setBenefits(benefit);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setPhoneNumber("099-123-456");
        employee.setBirthday(birthday);
        employee.setSssNumber("123456789");
        employee.setTinNumber("22222222");
        employee.setPhilhealthNumber("321-4535-312");
        employee.setPagibigNumber("9999321");
        employee.setStatus("Regular");
        employee.setPosition("Sales & Marketing");
        boolean result = systemAdministratorDao.updateEmployee(employee);
        assertTrue(result);
        System.out.println("Employee has been updated successfully");
    }
    
    @Test
    public void deleteEmployeeSuccess() {
        System.out.println("Test for Delete Employee - Success");
        boolean result = false;
        User employeeToDelete = new Employee(10030);
        result = systemAdministratorDao.deleteEmployee(employeeToDelete);
        assertTrue(result);
        System.out.println("Employee has been deleted");
    }
    
    @Test
    public void deleteEmployeeInvalidEmployee() {
        System.out.println("Test for Delete Employee - Invalid Employee");
        boolean result = false;
        User employeeToDelete = new Employee(999999999);
        result = systemAdministratorDao.deleteEmployee(employeeToDelete);
        assertFalse(result);
        System.out.println("Invalid Employee is not deleted");
    }
    
    @Test
    public void deleteEmployeeAlreadyDeleted() {
        System.out.println("Test for Delete Employee - Invalid Employee");
        boolean result = false;
        User employeeToDelete = new Employee(10030);
        result = systemAdministratorDao.deleteEmployee(employeeToDelete);
        assertTrue(result);
        result = systemAdministratorDao.deleteEmployee(employeeToDelete);
        assertFalse(result);
        System.out.println("Fail to delete already deleted employee");
    }
    
    @Test
    public void testLoadEmployeeListSuccess() {
        System.out.println("Test for Load Employee List - Success");
        List<User> employees = systemAdministratorDao.loadEmployeeList();
        assertFalse(employees.isEmpty());
        for (User employee : employees) {
            System.out.println(employee.getEmployeeID() + " - " + employee.getFirstName() + " " + employee.getLastName());
        }
    }
    
    @Test
    public void testLoadEmployeeListSearchSuccess() {
        System.out.println("Test for Load Employee List - Success");
        List<User> employees = systemAdministratorDao.loadEmployeeList("10015");
        assertFalse(employees.isEmpty());
        for (User employee : employees) {
            System.out.println(employee.getEmployeeID() + " - " + employee.getFirstName() + " " + employee.getLastName());
        }
        
        employees = systemAdministratorDao.loadEmployeeList("Josie");
        assertFalse(employees.isEmpty());
        for (User employee : employees) {
            System.out.println(employee.getEmployeeID() + " - " + employee.getFirstName() + " " + employee.getLastName());
        }
        
        employees = systemAdministratorDao.loadEmployeeList("Del");
        assertFalse(employees.isEmpty());
        for (User employee : employees) {
            System.out.println(employee.getEmployeeID() + " - " + employee.getFirstName() + " " + employee.getLastName());
        }
    }
    
    @Test
    public void testLoadEmployeeListSearchInvalid() {
        System.out.println("Test for Load Employee List Search - Invalid");
        List<User> employees = systemAdministratorDao.loadEmployeeList("2sdfa1239asdfz");
        assertTrue(employees.isEmpty());
        System.out.println("No employee found for search word 2sdfa1239asdfz");
    }
    
    @Test
    public void testLoadEmployeeListSearchDeletedEmployeeNotInList() {
        System.out.println("Test for Load Employee List Search - Deleted employee not in list");
        List<User> employees = systemAdministratorDao.loadEmployeeList("10032");
        assertFalse(employees.isEmpty());
        for (User employee : employees) {
            if(employee.getEmployeeID() == 10032){
                System.out.println(employee.getEmployeeID() + " - " + employee.getFirstName() + " " + employee.getLastName());
            }
        }
        
        // delete employee
        boolean result = systemAdministratorDao.deleteEmployee(new Employee(10032));
        assertTrue(result);
        
        employees = systemAdministratorDao.loadEmployeeList("10032");
        assertTrue(employees.isEmpty());
        System.out.println("Employee is not found on the list anymore after being deleted");
    }
    
    @Test
    public void testLoadNewEmployeeSuccess() {
        System.out.println("Test for Load New Employee - Success");
        LocalDate localDate = LocalDate.of(1995, 6, 10);
        Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Employee employee = new Employee();
        Address address = new Address();
        address.setStreet("Kalayaan street");
        address.setBarangay("Barangay Uno");
        address.setCity("Calamba City");
        address.setProvince("Laguna");
        address.setZipcode("4027");
        employee.setAddress(address);
        Benefits benefit = new Benefits();
        benefit.setBasicSalary(30000);
        benefit.setGrossSemiMonthlyRate(15000);
        benefit.setHourlyRate(125);
        employee.setBenefits(benefit);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setPhoneNumber("099-123-456");
        employee.setBirthday(birthday);
        employee.setSssNumber("123456789");
        employee.setTinNumber("22222222");
        employee.setPhilhealthNumber("321-4535-312");
        employee.setPagibigNumber("9999321");
        employee.setStatus("Regular");
        employee.setPosition("Sales & Marketing");
        employee.setImmediateSupervisor(10001);
        boolean result = systemAdministratorDao.addNewEmployee(employee);
        assertTrue(result);
        
        List<User> newUsers = systemAdministratorDao.loadNewUsers();
        assertFalse(newUsers.isEmpty());
        for (User user : newUsers) {
            System.out.println(user.getEmployeeID() + " - " + user.getFirstName() + " " + user.getLastName());
        }
    }
    
    @Test
    public void loadDashboardSuccess() {
        System.out.println("Test for Load Dashboard - Success");
        LocalDate localDate = LocalDate.of(1995, 6, 10);
        Date birthday = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Employee employee = new Employee();
        Address address = new Address();
        address.setStreet("Kalayaan street");
        address.setBarangay("Barangay Uno");
        address.setCity("Calamba City");
        address.setProvince("Laguna");
        address.setZipcode("4027");
        employee.setAddress(address);
        Benefits benefit = new Benefits();
        benefit.setBasicSalary(30000);
        benefit.setGrossSemiMonthlyRate(15000);
        benefit.setHourlyRate(125);
        employee.setBenefits(benefit);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setPhoneNumber("099-123-456");
        employee.setBirthday(birthday);
        employee.setSssNumber("123456789");
        employee.setTinNumber("22222222");
        employee.setPhilhealthNumber("321-4535-312");
        employee.setPagibigNumber("9999321");
        employee.setStatus("Regular");
        employee.setPosition("Sales & Marketing");
        employee.setImmediateSupervisor(10001);
        boolean result = systemAdministratorDao.addNewEmployee(employee);
        assertTrue(result);
        
        List<Integer> dashboard = systemAdministratorDao.loadDashboard();
        assertTrue(dashboard.get(0) >= 1, "Value should be at least 1");
        assertTrue(dashboard.get(0) >= 1, "Value should be at least 1");
        System.out.println("Total Employees: " + dashboard.get(0));
        System.out.println("New Employees: " + dashboard.get(1));
    }
}

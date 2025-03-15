/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import alex.oopmotorphpayrollsystem.Employee;
import alex.oopmotorphpayrollsystem.Benefits;

/**
 *
 * @author Alex Resurreccion
 */
public class EmployeeTest {
    private Employee employee;
    private Benefits benefits;

    @BeforeEach
    void setUp() {
        employee = new Employee(1001);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setStatus("Active");
        employee.setImmediateSupervisor("Jane Smith");
        employee.setDepartment("IT");
        
        benefits = new Benefits(30000, 15000, 200, 2000, 1000, 500);
        employee.setBenefits(benefits);
    }

    @Test
    void testGetAndSetDepartment() {
        assertEquals("IT", employee.getDepartment());
        employee.setDepartment("HR");
        assertEquals("HR", employee.getDepartment());
    }

    @Test
    void testGetAndSetStatus() {
        assertEquals("Active", employee.getStatus());
        employee.setStatus("Inactive");
        assertEquals("Inactive", employee.getStatus());
    }

    @Test
    void testGetAndSetImmediateSupervisor() {
        assertEquals("Jane Smith", employee.getImmediateSupervisor());
        employee.setImmediateSupervisor("Mark Johnson");
        assertEquals("Mark Johnson", employee.getImmediateSupervisor());
    }

    @Test
    void testGetBenefits() {
        assertNotNull(employee.getBenefits());
        assertEquals(30000, employee.getBenefitsBasicSalary());
        assertEquals(15000, employee.getBenefitsGrossSemiMonthlyRate());
        assertEquals(200, employee.getBenefitsHourlyRate());
        assertEquals(2000, employee.getBenefitsRiceSubsidy());
        assertEquals(1000, employee.getBenefitsPhoneAllowance());
        assertEquals(500, employee.getBenefitsClothingAllowance());
    }

    @Test
    void testSetAndCalculateTotalBenefits() {
        assertEquals(3500, employee.calculateTotalBenefits());
    }

    @Test
    void testSubmitLeaveRequest() {
        Date startDate = new Date();
        Date endDate = new Date();
        employee.submitLeaveRequest("Vacation", startDate, endDate, 5);
        assertNotNull(employee);
    }
}


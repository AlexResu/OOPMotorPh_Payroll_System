/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing;

import alex.oopmotorphpayrollsystem.Employee;
import alex.oopmotorphpayrollsystem.Payroll;
import alex.oopmotorphpayrollsystem.Benefits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Alex Resurreccion
 */
public class PayrollTest {
    private Payroll payroll;
    private Employee mockEmployee;
    private Benefits mockBenefits;

    @BeforeEach
    void setUp() {
        // Creating a mock Employee with sample benefits
        mockEmployee = new Employee();
        mockBenefits = new Benefits();
        mockBenefits.setHourlyRate(100.0);
        mockBenefits.setBasicSalary(50000);
        mockBenefits.setClothingAllowance(1000);
        mockBenefits.setPhoneAllowance(500);
        mockBenefits.setRiceSubsidy(2000);
        mockEmployee.setBenefits(mockBenefits);

        payroll = new Payroll(mockEmployee, 45, new Date(), new Date());
    }

    @Test
    void testSetAndGetPayrollID() {
        payroll.setPayrollID(101);
        assertEquals(101, payroll.getPayrollID());
    }

    @Test
    void testSetAndGetEmployee() {
        assertEquals(mockEmployee, payroll.getEmployee());
    }

    @Test
    void testSetAndGetWeekPeriodStart() {
        Date startDate = new Date();
        payroll.setWeekPeriodStart(startDate);
        assertEquals(startDate, payroll.getWeekPeriodStart());
    }

    @Test
    void testSetAndGetWeekPeriodEnd() {
        Date endDate = new Date();
        payroll.setWeekPeriodEnd(endDate);
        assertEquals(endDate, payroll.getWeekPeriodEnd());
    }

    @Test
    void testSetAndGetHoursWorked() {
        payroll.setHoursWorked(40);
        assertEquals(40, payroll.getHoursWorked());
    }

    @Test
    void testSetAndGetOvertimeHours() {
        payroll.setOvertimeHours(5);
        assertEquals(5, payroll.getOvertimeHours());
    }

    @Test
    void testSetAndGetGrossIncome() {
        payroll.calculateGrossPay();
        assertEquals(4500.0, payroll.getGrossIncome(), 0.01);
    }

    @Test
    void testSetAndGetNetPay() {
        payroll.calculateNetPay();
        assertTrue(payroll.getNetPay() >= 0);
    }

    @Test
    void testSetAndGetPaymentDate() {
        Date paymentDate = new Date();
        payroll.setPaymentDate(paymentDate);
        assertEquals(paymentDate, payroll.getPaymentDate());
    }

    @Test
    void testCheckBenefits() {
        payroll.checkBenefits();
        assertEquals(1000, mockEmployee.getBenefitsClothingAllowance());
        assertEquals(500, mockEmployee.getBenefitsPhoneAllowance());
        assertEquals(2000, mockEmployee.getBenefitsRiceSubsidy());
    }

    @Test
    void testLoadAllDeductions() {
        assertTrue(payroll.loadAllDeductions());
    }
}

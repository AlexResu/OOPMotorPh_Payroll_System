/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing;

import alex.oopmotorphpayrollsystem.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Payroll class.
 */
public class PayrollTest {

    private Payroll payroll;
    private Employee mockEmployee;
    private Benefits mockBenefits;

    @BeforeEach
    void setUp() {
        // Set up employee with dummy benefits
        mockEmployee = new Employee();
        mockBenefits = new Benefits();
        mockBenefits.setBasicSalary(21000);
        mockBenefits.setHourlyRate(125);
        mockBenefits.setPhoneAllowance(1000);
        mockBenefits.setClothingAllowance(800);
        mockBenefits.setRiceSubsidy(1500);
        mockEmployee.setBenefits(mockBenefits);

        // Set up a Payroll object for a range ending on the last Sunday of the current month
        Date[] weekDates = getCurrentWeekEndingOnLastSunday();
        payroll = new Payroll(mockEmployee, 45, weekDates[0], weekDates[1]);
    }

    @Test
    void testConstructorWithWorkedHoursSetsOvertime() {
        assertEquals(5, payroll.getOvertimeHours(), 0.1);
        assertEquals(45, payroll.getHoursWorked(), 0.1);
    }

    @Test
    void testCalculateGrossPay() {
        payroll.calculateGrossPay();
        assertEquals(5625.0, payroll.getGrossIncome(), 0.1); // 45 * 125
    }

    @Test
    void testGenerateDeductions() {
        payroll.generateDeductions(160);
        assertNotNull(payroll.getDeductions());
        assertTrue(payroll.getDeductions().getSssDeduction() >= 0); // placeholder check
    }

    @Test
    void testLoadAllDeductionsTrue() {
        assertTrue(payroll.loadAllDeductions());
    }

    @Test
    void testCheckBenefitsOnLastSunday() {
        payroll.checkBenefits();
        assertEquals(1000, mockEmployee.getBenefitsPhoneAllowance());
    }

    @Test
    void testCalculateNetPay() {
        payroll.calculateGrossPay();
        payroll.generateDeductions(160);
        payroll.calculateNetPay();
        double expectedNetPay = payroll.getGrossIncome()
                + mockEmployee.calculateTotalBenefits()
                - payroll.getDeductions().calculateTotalDeductions();
        assertEquals(expectedNetPay, payroll.getNetPay(), 0.1);
    }

    // Helper method to return week start and end dates where end is the last Sunday of the current month
    private Date[] getCurrentWeekEndingOnLastSunday() {
        Calendar end = Calendar.getInstance();
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        while (end.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            end.add(Calendar.DAY_OF_MONTH, -1);
        }

        Calendar start = (Calendar) end.clone();
        start.add(Calendar.DAY_OF_MONTH, -6); // One week before

        return new Date[]{start.getTime(), end.getTime()};
    }
}


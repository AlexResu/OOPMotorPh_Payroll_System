/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import alex.oopmotorphpayrollsystem.HRPersonnel;
import alex.oopmotorphpayrollsystem.LeaveRequest;
import alex.oopmotorphpayrollsystem.Payroll;
import alex.oopmotorphpayrollsystem.User;

/**
 *
 * @author Alex Resurreccion
 */
public class HRPersonneTest {
    private HRPersonnel hrPersonnel;
    
    @BeforeEach
    void setUp() {
        hrPersonnel = new HRPersonnel();
    }

    @Test
    void testProcessPayrollReturnsTrue() {
        Date startDate = new Date();
        Date endDate = new Date();
        boolean result = hrPersonnel.processPayroll(startDate, endDate);
        assertTrue(result, "Payroll should return true as default implementation.");
    }

    @Test
    void testApproveLeaveSetsStatusAndApprover() {
        LeaveRequest leaveRequest = new LeaveRequest();
        hrPersonnel.approveLeave(leaveRequest);

        assertEquals("APPROVED", leaveRequest.getLeaveStatus());
        assertEquals(hrPersonnel, leaveRequest.getApprovedBy());
    }

    @Test
    void testHRPersonnelInheritsUser() {
        assertTrue(hrPersonnel instanceof User, "HRPersonnel should be a subclass of User");
    }
}


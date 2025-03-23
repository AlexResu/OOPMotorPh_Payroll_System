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
    void testApproveLeaveRequest() {
        LeaveRequest leaveRequest = new LeaveRequest();
        hrPersonnel.approveLeaveRequest(leaveRequest);
        assertEquals("APPROVED", leaveRequest.getLeaveStatus());
        assertEquals(hrPersonnel, leaveRequest.getApprovedBy());
    }
    
    @Test
    void testDeclineLeaveRequest() {
        LeaveRequest leaveRequest = new LeaveRequest();
        hrPersonnel.declineLeaveRequest(leaveRequest);
        assertEquals("DECLINED", leaveRequest.getLeaveStatus());
        assertEquals(hrPersonnel, leaveRequest.getApprovedBy());
    }
}


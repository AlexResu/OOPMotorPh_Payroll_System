/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing;

import alex.oopmotorphpayrollsystem.SystemAdministrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Alex Resurreccion
 */
public class SystemAdminTest {
    private SystemAdministrator admin;

    @BeforeEach
    void setUp() {
        admin = new SystemAdministrator(1001);
    }

    @Test
    void testSetAndGetEmployeeID() {
        assertEquals(1001, admin.getEmployeeID());
    }

    @Test
    void testAddRole() {
        admin.addRole("Admin");
        admin.addRole("HR Manager");

        List<String> roles = admin.getRoles();
        assertTrue(roles.contains("Admin"));
        assertTrue(roles.contains("HR Manager"));
    }

    @Test
    void testAddDuplicateRole() {
        admin.addRole("Admin");
        admin.addRole("Admin"); // Attempt to add duplicate

        List<String> roles = admin.getRoles();
        assertEquals(1, roles.size()); // Should only contain one instance
    }

    @Test
    void testUpdateRole() {
        admin.addRole("HR Manager");
        admin.updateRole("HR Manager", "Payroll Manager");

        List<String> roles = admin.getRoles();
        assertFalse(roles.contains("HR Manager"));
        assertTrue(roles.contains("Payroll Manager"));
    }

    @Test
    void testDeleteRole() {
        admin.addRole("HR Manager");
        admin.deleteRole("HR Manager");

        List<String> roles = admin.getRoles();
        assertFalse(roles.contains("HR Manager"));
    }

}

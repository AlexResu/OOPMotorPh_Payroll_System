/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private TestUser user; // Concrete subclass for testing

    // Concrete subclass since User is abstract
    static class TestUser extends User {
        public TestUser(int employeeID) {
            super(employeeID);
        }
    }

    @BeforeEach
    void setUp() {
        user = new TestUser(101);
        user.setFirstName("Juan");
        user.setLastName("Dela Cruz");
        user.setBirthday(new Date());
        user.setPosition("Software Developer");
        user.setAddress("Manila, Philippines");
        user.setPhoneNumber("09123456789");
        user.setSssNumber("12-3456789-0");
        user.setPhilhealthNumber("1234-5678-9012");
        user.setTinNumber("123-456-789");
        user.setPagibigNumber("1234-5678-9012");
    }

    @Test
    void testSetAndGetEmployeeID() {
        assertEquals(101, user.getEmployeeID());
    }

    @Test
    void testSetAndGetFirstName() {
        assertEquals("Juan", user.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        assertEquals("Dela Cruz", user.getLastName());
    }

    @Test
    void testSetAndGetBirthday() {
        Date newDate = new Date();
        user.setBirthday(newDate);
        assertEquals(newDate, user.getBirthday());
    }

    @Test
    void testSetAndGetPosition() {
        assertEquals("Software Developer", user.getPosition());
    }

    @Test
    void testSetAndGetAddress() {
        assertEquals("Manila, Philippines", user.getAddress());
    }

    @Test
    void testSetAndGetPhoneNumber() {
        assertEquals("09123456789", user.getPhoneNumber());
    }

    @Test
    void testSetAndGetSssNumber() {
        assertEquals("12-3456789-0", user.getSssNumber());
    }

    @Test
    void testSetAndGetPhilhealthNumber() {
        assertEquals("1234-5678-9012", user.getPhilhealthNumber());
    }

    @Test
    void testSetAndGetTinNumber() {
        assertEquals("123-456-789", user.getTinNumber());
    }

    @Test
    void testSetAndGetPagibigNumber() {
        assertEquals("1234-5678-9012", user.getPagibigNumber());
    }
}

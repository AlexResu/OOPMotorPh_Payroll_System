/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing.Dao;

import alex.oopmotorphpayrollsystem.User;
import dao.UserDao;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DbConnection;

/**
 *
 * @author Alex Resurreccion
 */
public class UserDaoTest {
    UserDao userDao = null;
    Connection connection;
    
    @BeforeEach
    public void setUp() throws SQLException {
        connection = DbConnection.getConnectionWithTransaction();
        userDao = new UserDao(connection);
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.rollback();  // Roll back everything done in the test
            connection.close();
        }
    }
    
    @Test
    public void getRoleNameEmployeeSuccess() {
        System.out.println("Test for Get Role Name Employee - Success");
        String roleName = userDao.getRoleName(10008);
        assertEquals("Employee", roleName);
        System.out.println("Successfully get role name for employee");
    }
    
    @Test
    public void getRoleNameHRPersonnelSuccess() {
        System.out.println("Test for Get Role HR Personnel - Success");
        String roleName = userDao.getRoleName(10007);
        assertEquals("HR Personnel", roleName);
        System.out.println("Successfully get role name for employee");
    }
    
    @Test
    public void getRoleNameSystemAdminSuccess() {
        System.out.println("Test for Get Role Name System Admin - Success");
        String roleName = userDao.getRoleName(10001);
        assertEquals("System Admin", roleName);
        System.out.println("Successfully get role name for employee");
    }
    
    @Test
    public void getUserInfoSuccess() {
        System.out.println("Test for Get User Info - Success");
        User user = userDao.getUserInfo(10008, "Employee", true);
        assertNotNull(user);
        System.out.println(
                user.getEmployeeID() + " - " 
                + user.getFirstName() + " " 
                + user.getLastName());
        System.out.println("Successfully get user info for employee 10008");
    }
    
    @Test
    public void getUserInfoUnauthenticated() {
        System.out.println("Test for Get User Info - Unauthenticated");
        User user = userDao.getUserInfo(10008, "Employee", false);
        assertNull(user);
        System.out.println("Can't retrieve user info for unauthenticated user");
    }
}

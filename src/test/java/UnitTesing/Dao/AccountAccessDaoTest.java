/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing.Dao;

import models.AccountAccess;
import dao.AccountAccessDao;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import dao.AccountAccessDao;
import models.User;
import dao.UserDao;

/**
 *
 * @author Alex Resurreccion
 */
public class AccountAccessDaoTest {
    AccountAccessDao accountAccessDao = new AccountAccessDao();
    
    @Test
    public void testLoginSuccess() {
        System.out.println("Test for login - success");
        AccountAccess account = accountAccessDao.login(10001, "Garcia01");

        assertNotNull(account);
        System.out.println("Account is not null");
        assertTrue(account.getIsAuthenticated());
        assertEquals(10001, account.getEmployeeNumber());
        assertEquals("System Admin", account.getRole());
        assertEquals(Arrays.asList("Dashboard", "User Management"), account.getPermissions());
    }
    
    @Test
    public void testLoginIncorrectPassword() {
        System.out.println("Test for login - incorrect password");
        AccountAccess account = accountAccessDao.login(10001, "wrong-password");

        assertNull(account);
    }
    
    @Test
    public void testLoginFailure_UserNotFound() {
        System.out.println("Test for login - invalid user");
        AccountAccess account = accountAccessDao.login(99999, "wrong-password");

        assertNull(account);
    }
}



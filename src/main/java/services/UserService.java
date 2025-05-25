/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import dao.UserDAO;
import models.User;

/**
 *
 * @author Alex Resurreccion
 */
public class UserService {
    private UserDAO userDAO = new UserDAO();

    public boolean login(int employeeNumber, String password) {
        User user = userDAO.getUserByEmployeeNumber(employeeNumber);

        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }

//        setRole(user.getRole());
        return true;
    }

//    private void setRole(String role) {
//        // This could set a session variable, or be passed back to a controller
//        System.out.println("Logged in with role: " + role);
//    }
}

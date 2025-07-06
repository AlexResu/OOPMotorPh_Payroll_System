/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DbConnection;
import models.AccountAccess;
import java.sql.PreparedStatement;
import java.sql.Statement;
/**
 *
 * @author Alex Resurreccion
 */
public class AccountAccessDao {
    private Connection connection;
    private Statement statement = null;
    private ResultSet result = null;
    private PreparedStatement preparedStatement = null;

    // Constructor with custom connection
    public AccountAccessDao(Connection connection) {
        this.connection = connection;
    }

    // Constructor with default connection
    public AccountAccessDao() {
        this.connection = DbConnection.getConnection();
    }
    
    /**
     * This method handles user login by verifying the employee number and password.
     * If the credentials match, it retrieves the user's role and permissions.
     * 
     * @param employeeNumber The employee number of the user trying to log in
     * @param password The password entered by the user
     * @return true if login is successful, false otherwise
     */
    public AccountAccess login(int employeeNumber, String password) {
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT employee_id, password, role_name " +
               "FROM user_credentials uc " +
               "JOIN roles r ON r.role_id = uc.role_id " +
               "WHERE employee_id = ? AND password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employeeNumber);
            preparedStatement.setString(2, password);

            // Execute the query
            result = preparedStatement.executeQuery();
            AccountAccess account = new AccountAccess();
            if (result.next()) {
                // Populate model object from DB data
                System.err.println("Error while executing SQL query!");
                account.setEmployeeNumber(result.getInt("employee_id"));
                account.setRole(result.getString("role_name"));
            } else {
                return null;
            }
            
            // Get list of permissions
            try {
                // Create a statement object
                statement = connection.createStatement();

                // Define the query
                String permQuery = "SELECT CONCAT(\n" +
                    "        p.module, \n" +
                    "        CASE \n" +
                    "            WHEN p.submodule IS NOT NULL THEN CONCAT('-', p.submodule)\n" +
                    "            ELSE ''\n" +
                    "        END\n" +
                    "    ) AS permission FROM user_credentials uc \n" +
                    "JOIN roles r ON r.role_id = uc.role_id\n" +
                    "JOIN role_permissions rp ON r.role_id = rp.role_id\n" +
                    "JOIN permissions p ON p.permission_id = rp.permission_id\n" +
                    "WHERE employee_id = " + employeeNumber;

                // Execute the query
                result = statement.executeQuery(permQuery);
                List<String> resultPermissions = new ArrayList<>();
                while (result.next()) {
                    resultPermissions.add(result.getString("permission"));
                }
                account.setIsAuthenticated(true);
                account.setPermissions(resultPermissions);
                return account;
            } catch (SQLException e) {
                System.err.println("Error while executing SQL query!");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return null;
    }
    
    public AccountAccess getAccountEmail(int employeeId){
        try {
            // Create a statement object
            statement = connection.createStatement();

            // Define the query
            String query = "SELECT email FROM user_credentials " +
               "WHERE employee_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employeeId);

            // Execute the query
            result = preparedStatement.executeQuery();
            AccountAccess account = new AccountAccess();
            if (result.next()) {
                account.setEmail(result.getString("email"));
                return account;
            }
        } catch (SQLException e) {
            System.err.println("Error while executing SQL query!");
            e.printStackTrace();
        }
        
        return null;
    }
}

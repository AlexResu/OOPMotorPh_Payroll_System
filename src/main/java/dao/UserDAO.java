/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import models.User;
import org.hibernate.Session;
import util.HibernateUtility;
import org.hibernate.query.Query;

/**
 *
 * @author Alex Resurreccion
 */
public class UserDAO {
    public List<User> getAllUsers() 
    {   
        List<User> list;
        try(Session session = HibernateUtility.getSessionFactory().openSession()) 
        {
            list = session.createQuery("FROM user_credentials", User.class).getResultList();
        }
        
        return list;
    }     
    
    public User getUserByEmployeeNumber(int employeeNumber) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.employeeNumber = :employeeNumber", User.class);
            query.setParameter("employeeNumber", employeeNumber);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

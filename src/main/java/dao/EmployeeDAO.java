/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.util.List;
import org.hibernate.Session;
import util.HibernateUtility;
import models.Employee;

/**
 *
 * @author Alex Resurreccion
 */
public class EmployeeDAO {
    public List<Employee> getAllEmployees() 
    {   
        List<Employee> list;
        try(Session session = HibernateUtility.getSessionFactory().openSession()) 
        {
            list = session.createQuery("FROM Employee", Employee.class).getResultList();
        }
        
        return list;
    }      
    
}

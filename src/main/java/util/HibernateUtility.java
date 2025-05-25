/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;

/**
 *
 * @author Alex Resurreccion
 */
public class HibernateUtility {
    private static final SessionFactory sessionFactory;
    
    static
    {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }
        catch(HibernateException ex)
        {
            ex.getMessage();
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public  static SessionFactory getSessionFactory() 
    {
        return sessionFactory;
    }
    
    
}

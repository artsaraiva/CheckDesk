/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ConfigurationManager;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author MNicaretta
 */
public class HibernateUtil
{
    private static final SessionFactory sessionFactory;

    static
    {
        try
        {
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(ConfigurationManager.getInstance().getProperties()).build();
            
            sessionFactory = ConfigurationManager.getInstance().buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex)
        {
            // Log the exception. 
            ApplicationController.logException(ex);
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.service;

import com.checkdesk.model.data.User;
import com.checkdesk.model.db.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author MNicaretta
 */
public class UserService
{
    public static UserService getInstance() throws Exception
    {
        if ( defaultInstance == null )
        {
            defaultInstance = new UserService();
        }
        
        return defaultInstance;
    }
    
    private static UserService defaultInstance = null;
    
    private UserService()
    {
    }
    
    public User login(String login, String password) throws Exception
    {
        Session session = getSession();
        
        try
        {
            String column = login.contains("@") ? "email" : "login";
            
            Query query = session.createQuery("from User where lower(" + column + ") like :l and password = :p");
            query.setParameter("l", login.toLowerCase());
            query.setParameter("p", password);
            
            return (User) query.uniqueResult();
        }
        
        finally
        {
            session.close();
        }
    }
    
    private Session getSession() throws Exception
    {
        return HibernateUtil.getSessionFactory().openSession();
    }
}

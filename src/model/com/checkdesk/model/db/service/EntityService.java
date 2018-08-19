/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.service;

import com.checkdesk.model.db.HibernateUtil;
import java.io.Serializable;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author MNicaretta
 */
public class EntityService
{
    public static EntityService getInstance() throws Exception
    {
        if (defaultInstance == null)
        {
            defaultInstance = new EntityService();
        }

        return defaultInstance;
    }

    private static EntityService defaultInstance = null;

    private EntityService() throws Exception
    {
        initialTransaction();
    }

    private void initialTransaction() throws Exception
    {
        Session session = getSession();

        try
        {
            session.beginTransaction();
            Query q = session.createSQLQuery("select current_date");
            q.uniqueResult();
        }
        finally
        {
            session.close();
        }
    }

    public void save(Serializable entity) throws Exception
    {
        Session session = getSession();

        try
        {
            Transaction t = session.beginTransaction();

            session.save(entity);

            t.commit();
        }
        finally
        {
            session.close();
        }
    }

    public void update(Serializable entity) throws Exception
    {
        Session session = getSession();

        try
        {
            Transaction t = session.beginTransaction();

            session.update(entity);

            t.commit();
        }
        finally
        {
            session.close();
        }
    }

    public void delete(Serializable entity) throws Exception
    {
        Session session = getSession();

        try
        {
            Transaction t = session.beginTransaction();

            session.delete(entity);

            t.commit();
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

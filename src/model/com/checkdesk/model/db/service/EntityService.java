/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.service;

import com.checkdesk.model.db.HibernateUtil;
import com.checkdesk.model.util.Parameter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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

    public Object getValue(Class type, int id) throws Exception
    {
        Session session = getSession();

        try
        {
            Query query = session.createQuery("from " + type.getName() + " where id = :id");

            query.setParameter("id", id);

            return query.uniqueResult();
        }

        finally
        {
            session.close();
        }
    }

    public Object getValue(Class type, List<Parameter> parameters) throws Exception
    {
        Session session = getSession();

        try
        {
            return composeQuery(session, type, parameters).uniqueResult();
        }

        finally
        {
            session.close();
        }
    }

    public List getValues(Class type) throws Exception
    {
        return getValues(type, new ArrayList<>());
    }

    public List getValues(Class type, List<Parameter> parameters) throws Exception
    {
        Session session = getSession();

        try
        {
            return (List) composeQuery(session, type, parameters).list();
        }

        finally
        {
            session.close();
        }
    }

    public List getFieldValues(Field field, Class type) throws Exception
    {
        return getFieldValues(field, type, null);
    }

    public List getFieldValues(Field field, Class type, List<Parameter> parameters) throws Exception
    {
        Session session = getSession();

        try
        {
            return (List) composeQuery(session, type, parameters).list();
        }

        finally
        {
            session.close();
        }
    }
    
    private Query composeQuery(Session session, Class type, List<Parameter> parameters) throws Exception
    {
        return composeQuery(null, session, type, parameters);
    }

    private Query composeQuery(Field field, Session session, Class type, List<Parameter> parameters) throws Exception
    {
        String conditions = "";

        for (Parameter parameter : parameters)
        {
            if (parameter.getCondition() != null)
            {
                conditions += (conditions.isEmpty() ? " where " : " and ") + parameter.getCondition();
            }
        }

        Query query = session.createQuery((field != null ? field.getName() + " " : "") + "from " + type.getName() + conditions);

        for (Parameter parameter : parameters)
        {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        
        return query;
    }

    public void close() throws Exception
    {
        HibernateUtil.getSessionFactory().close();
    }

    private Session getSession() throws Exception
    {
        return HibernateUtil.getSessionFactory().openSession();
    }

}

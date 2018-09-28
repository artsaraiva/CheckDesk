/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.service;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Log;
import com.checkdesk.model.db.HibernateUtil;
import com.checkdesk.model.util.Parameter;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javassist.Modifier;
import org.hibernate.CacheMode;
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

    private static Session getSession() throws Exception
    {
        if (defaultSession == null)
        {
            defaultSession = HibernateUtil.getSessionFactory().openSession();
            defaultSession.setCacheMode(CacheMode.IGNORE);
        }

        return defaultSession;
    }

    private static EntityService defaultInstance = null;
    private static Session defaultSession = null;

    private EntityService() throws Exception
    {
        initialTransaction();
    }

    private void initialTransaction() throws Exception
    {
        Session session = getSession();

        Query q = session.createSQLQuery("select current_date");
        q.uniqueResult();
    }

    public void save(Serializable entity) throws Exception
    {
        logEvent(Log.EVENT_ADD, entity);

        Session session = getSession();

        Transaction t = session.beginTransaction();

        session.save(entity);
        t.commit();
    }

    public void update(Serializable entity) throws Exception
    {
        logEvent(Log.EVENT_UPDATE, entity);

        Session session = getSession();

        Transaction t = session.beginTransaction();

        session.update(entity);
        t.commit();
    }

    public void delete(Serializable entity) throws Exception
    {
        logEvent(Log.EVENT_DELETE, entity);

        Session session = getSession();

        Transaction t = session.beginTransaction();

        session.delete(getValue(entity.getClass(), entity));
        t.commit();
    }

    public Object getValue(Class type, int id) throws Exception
    {
        Session session = getSession();

        return session.get(type, id);
    }

    public Object getValue(Class type, Serializable value) throws Exception
    {
        try
        {
            Method getter = new PropertyDescriptor("id", value.getClass()).getReadMethod();

            if (getter != null)
            {
                value = (Integer) getter.invoke(value);
            }
        }

        catch (Exception e)
        {
            //NADA
        }

        return getValue(type, (int) value);
    }

    public Object getViewValue(List<String> columns, String viewName, Map<String, Object> parameters) throws Exception
    {
        Session session = getSession();

        String sql = "select ";
        
        for (String column : columns)
        {
            sql += column + ", ";
        }
        
        sql = sql.substring(0, sql.lastIndexOf(", "));
        
        sql += " from " + viewName + " where ";
        
        for (String key : parameters.keySet())
        {
            sql += key + " = :" + key + " and ";
        }
        
        Query query = session.createSQLQuery(sql.substring(0, sql.lastIndexOf(" and ")));
        
        for (Map.Entry entry : parameters.entrySet())
        {
            query.setParameter(entry.getKey().toString(), entry.getValue());
        }
        
        return query.uniqueResult();
    }

    public Object loadValue(Class type, Serializable value) throws Exception
    {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            try
            {
                value = (Integer) new PropertyDescriptor("id", value.getClass()).getReadMethod().invoke(value);
            }

            catch (Exception e)
            { /*NADA*/ }

            Object result = session.load(type, value);

            for (Field field : result.getClass().getDeclaredFields())
            {
                try
                {
                    new PropertyDescriptor(field.getName(), result.getClass()).getReadMethod().invoke(result).toString();
                }

                catch (Exception e)
                {
                    /*NADA*/ }
            }

            return result;
        }

        finally
        {
            session.close();
        }
    }

    public Object getValue(Class type, List<Parameter> parameters) throws Exception
    {
        Session session = getSession();

        return composeQuery(session, type, parameters).uniqueResult();
    }

    public List getValues(Class type) throws Exception
    {
        return getValues(type, new ArrayList<>());
    }

    public List getValues(Class type, List<Parameter> parameters) throws Exception
    {
        Session session = getSession();

        return (List) composeQuery(session, type, parameters).list();
    }

    public List getFieldValues(Field field, Class type) throws Exception
    {
        return getFieldValues(field, type, new ArrayList<>());
    }

    public List getFieldValues(Field field, Class type, List<Parameter> parameters) throws Exception
    {
        Session session = getSession();

        return (List) composeQuery(field, session, type, parameters).list();
    }

    public List loadValues(Class type, List<Parameter> parameters) throws Exception
    {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            List result = (List) composeQuery(session, type, parameters).list();

            for (Object o : result)
            {
                for (Field field : o.getClass().getDeclaredFields())
                {
                    try
                    {
                        new PropertyDescriptor(field.getName(), o.getClass()).getReadMethod().invoke(o).toString();
                    }

                    catch (Exception e)
                    {
                        /*NADA*/ }
                }
            }

            return result;
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

        Query query = session.createQuery((field != null ? "select " + field.getName() + " " : "") + "from " + type.getName()
                + conditions);

        for (Parameter parameter : parameters)
        {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }

        return query;
    }

    public void close() throws Exception
    {
        getSession().close();
        HibernateUtil.getSessionFactory().close();
    }

    private void logEvent(int event, Serializable entity) throws Exception
    {
        if (ApplicationController.isActiveLog() && !(entity instanceof Log))
        {
            Log log = new Log();
            log.setTimestamp(new Timestamp(System.currentTimeMillis()));
            log.setUser(ApplicationController.getInstance().getActiveUser());
            log.setEvent(event);
            log.setObjectName(entity.toString());
            log.setObjectClass(entity.getClass().toString());
            log.setCommand(getCommand(event, entity));

            save(log);
        }
    }

    private String getCommand(int event, Serializable entity) throws Exception
    {
        StringBuilder builder = new StringBuilder();
        Serializable oldValue = null;

        builder.append("<table class=\"log-command\">")
                .append("    <tr>")
                .append("        <th>")
                .append("            Campo:")
                .append("        </th>")
                .append("        <th>")
                .append(event == Log.EVENT_UPDATE ? "Valores Anteriores:" : "Valores:")
                .append("        </th>");

        if (event == Log.EVENT_UPDATE)
        {
            getSession().get(entity.getClass(), (Integer) new PropertyDescriptor("id", entity.getClass()).getReadMethod().invoke(entity));
            oldValue = (Serializable) loadValue(entity.getClass(), entity);
            builder.append("    <th> --> </th>")
                    .append("    <th>")
                    .append("        Novos Valores:")
                    .append("    </th>");
        }

        builder.append("    </tr>");

        for (Field field : entity.getClass().getDeclaredFields())
        {
            if (!Modifier.isStatic(field.getModifiers()))
            {
                Method getter = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod();

                builder.append("<tr>")
                        .append("    <td>")
                        .append(field.getName()).append(": ")
                        .append("    </td>");

                if (event == Log.EVENT_UPDATE)
                {
                    builder.append("<td>")
                            .append(getter.invoke(oldValue))
                            .append("</td>")
                            .append("<td> --> </td>");
                }

                builder.append("    <td>")
                        .append(getter.invoke(entity))
                        .append("    </td>")
                        .append("</tr>");
            }
        }

        return builder.toString();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.service;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Entity;
import com.checkdesk.model.data.Log;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.model.util.ServerRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 *
 * @author MNicaretta
 */
public class EntityService
{
    private static final int REQUEST_INSERT        = 0;
    private static final int REQUEST_UPDATE        = 1;
    private static final int REQUEST_DELETE        = 2;
    private static final int REQUEST_SELECT_UNIQUE = 3;
    private static final int REQUEST_SELECT_LIST   = 4;
    
    private static EntityService defaultInstance = null;

    public static EntityService getInstance() throws Exception
    {
        if (defaultInstance == null)
        {
            defaultInstance = new EntityService();
        }

        return defaultInstance;
    }

    private EntityService() throws Exception
    {
    }

    public Object handleRequest(ApplicationController controller, ServerRequest request) throws Exception
    {
        Object result = null;
        
        switch ((int) request.getParameter("request"))
        {
            case REQUEST_INSERT:
            {
                result = insert((Entity) request.getParameter("object"));
            }
            break;
                
            case REQUEST_UPDATE:
            {
                result = update((Entity) request.getParameter("object"));
            }
            break;
                
            case REQUEST_DELETE:
            {
                delete((Entity) request.getParameter("object"));
            }
            break;
                
            case REQUEST_SELECT_UNIQUE:
            {
                List<String> parameters = request.getParameters();
                
                if (parameters.contains("viewName"))
                {
                    result = getViewValue((ArrayList) request.getParameter("columns"),
                                          (String) request.getParameter("viewName"),
                                          (HashMap) request.getParameter("parameters"));
                }
                
                else if (parameters.contains("function"))
                {
                    result = executeFunction((String) request.getParameter("function"),
                                             (HashMap) request.getParameter("parameters"));
                }
                
                else 
                {
                    Class clazz = (Class) request.getParameter("type");
                    
                    if (parameters.contains("id"))
                    {
                        result = getValue(clazz, (int) request.getParameter("id"));
                    }

                    else if (parameters.contains("value"))
                    {
                        result = getValue(clazz, (Entity) request.getParameter("value"));
                    }

                    else if (parameters.contains("parameters"))
                    {
                        result = getValue(clazz, (List) request.getParameter("parameters"));
                    }
                }
            }
            break;
                
            case REQUEST_SELECT_LIST:
            {
                List<String> parameters = request.getParameters();
                
                Class clazz = (Class) request.getParameter("type");

                if (parameters.contains("field"))
                {
                    result = getFieldValues(clazz.getDeclaredField((String) request.getParameter("field")),
                                            clazz,
                                            (ArrayList) request.getParameter("parameters"));
                }
                
                else
                {
                    result = getValues(clazz, (ArrayList) request.getParameter("parameters"));
                }
            }
            break;
        }
        
        return result;
    }

    public Entity insert(Entity entity) throws Exception
    {
        logEvent(Log.EVENT_ADD, entity);
        
        Database db = getDatabase();

        try
        {
            Schema schema = Schemas.getSchema(entity.getClass());
            
            if (schema != null)
            {
                entity.setId(db.queryInt(schema.fetcher.insert(db, schema, entity)));

                return entity;
            }

            return null;
        }
        
        finally
        {
            db.release();
        }
    }

    public Entity update(Entity entity) throws Exception
    {
        logEvent(Log.EVENT_UPDATE, entity);

        Database db = getDatabase();

        try
        {
            Schema schema = Schemas.getSchema(entity.getClass());
            
            if (schema != null)
            {
                db.executeCommand(schema.fetcher.update(db, schema, entity));

                return entity;
            }

            return null;
        }
        
        finally
        {
            db.release();
        }
    }

    public void delete(Entity entity) throws Exception
    {
        logEvent(Log.EVENT_DELETE, entity);

        Database db = getDatabase();

        try
        {
            Schema schema = Schemas.getSchema(entity.getClass());
            
            if (schema != null)
            {
                db.executeCommand(schema.fetcher.delete(db, schema, entity));
            }
        }
        
        finally
        {
            db.release();
        }
    }

    public Entity getValue(Class type, int id) throws Exception
    {
        return getValue(type, Arrays.asList(new Parameter(Entity.class.getDeclaredField("id"),
                                            id,
                                            Parameter.COMPARATOR_EQUALS)));
    }

    public Entity getValue(Class type, Entity value) throws Exception
    {
        return getValue(type, value.getId());
    }
    
    public Entity getValue(Class type, List<Parameter> parameters) throws Exception
    {
        Database db = getDatabase();

        try
        {
            Schema schema = Schemas.getSchema(type);
            
            if (schema != null)
            {
                return db.fetchOne(composeQuery(db, schema, parameters), schema.fetcher);
            }
            
            return null;
        }
        
        finally
        {
            db.release();
        }
    }

    public List<Object[]> getViewValue(List<String> columns, String viewName, Map<String, Object> parameters) throws Exception
    {
        Database db = getDatabase();
        
        try
        {
            StringJoiner columnJoin = new StringJoiner(", ");
            StringJoiner paramJoin = new StringJoiner(" and ");

            for (String column : columns)
            {
                columnJoin.add(column);
            }

            for (Map.Entry<String, Object> entry : parameters.entrySet())
            {
                paramJoin.add(entry.getKey() + " = " + db.quote(entry.getValue()));
            }

            String sql = "select " + columnJoin.toString() +
                         " from " + viewName +
                         " where " + paramJoin.toString();

            return db.queryMatrix(sql, columns.size());
        }
        
        finally
        {
            db.release();
        }
    }

    public Object executeFunction(String function, Map<String, Object> parameters) throws Exception
    {
        Database db = getDatabase();

        try
        {

            StringJoiner paramJoin = new StringJoiner(" and ");

            for (Map.Entry<String, Object> entry : parameters.entrySet())
            {
                paramJoin.add(entry.getKey() + " = " + db.quote(entry.getValue()));
            }

            String sql = "select " + function +
                         " where " + paramJoin.toString();
            
            return db.query(sql);
        }
        
        finally
        {
            db.release();
        }
    }

    public List getValues(Class type) throws Exception
    {
        return getValues(type, new ArrayList<>());
    }

    public List getValues(Class type, List<Parameter> parameters) throws Exception
    {
        Database db = getDatabase();

        try
        {
            Schema schema = Schemas.getSchema(type);
            
            if (schema != null)
            {
                return db.fetchAll(composeQuery(db, schema, parameters), schema.fetcher);
            }
            
            return null;
        }
        
        finally
        {
            db.release();
        }
    }

    public List getFieldValues(Field field, Class type) throws Exception
    {
        return getFieldValues(field, type, new ArrayList<>());
    }

    public List getFieldValues(Field field, Class type, List<Parameter> parameters) throws Exception
    {
        Database db = getDatabase();

        try
        {
            Schema schema = Schemas.getSchema(type);
            
            if (schema != null)
            {
                return db.fetchAll(composeQuery(db, schema, field, parameters), schema.fetcher);
            }
            
            return null;
        }
        
        finally
        {
            db.release();
        }
    }

    private String composeQuery(Database db, Schema schema, List<Parameter> parameters) throws Exception
    {
        return composeQuery(db, schema, null, parameters);
    }

    private String composeQuery(Database db, Schema schema, Field field, List<Parameter> parameters) throws Exception
    {
        StringJoiner paramJoiner = new StringJoiner(" and ");

        for (Parameter parameter : parameters)
        {
            paramJoiner.add(getCondition(db, schema, parameter));
        }
        
        if (field == null)
        {
            return schema.select +
                   (!paramJoiner.toString().isEmpty() ? " where " : "" ) + paramJoiner;
        }
        
        else
        {
            return "select " + schema.getField(field.getName()) +
                   " from " + schema.name +
                   " where " + paramJoiner;
        }
    }

    private void logEvent(int event, Entity entity) throws Exception
    {
        if (ApplicationController.isActiveLog() && !(entity instanceof Log))
        {
            Log log = new Log();
            log.setTimestamp(new Timestamp(System.currentTimeMillis()));
            log.setUserId(ApplicationController.getInstance().getActiveUser().getId());
            log.setEvent(event);
            log.setObjectName(entity.toString());
            log.setObjectClass(entity.getClass().toString());
            log.setCommand(getCommand(event, entity));

            insert(log);
        }
    }

    private String getCommand(int event, Entity entity) throws Exception
    {
        StringBuilder builder = new StringBuilder();
        Entity oldValue = null;

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
            oldValue = getValue(entity.getClass(), entity);
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
    
    private String getValue(Database db, Parameter parameter)
    {
        Object result = parameter.getValue();

        switch (parameter.getComparator())
        {
            case Parameter.COMPARATOR_LOWER_CASE:
                result = parameter.getValue().toString().toLowerCase();
                break;
        }

        return db.quote(result);
    }

    private String getCondition(Database db, Schema schema, Parameter parameter)
    {
        String result = null;
        String fieldName = schema.getField(parameter.getField());
        
        switch (parameter.getComparator())
        {
            case Parameter.COMPARATOR_EQUALS:
                result = fieldName + " = " + getValue(db, parameter);
                break;

            case Parameter.COMPARATOR_LOWER_CASE:
                result = "lower(" + fieldName + ") like " + getValue(db, parameter);
                break;

            case Parameter.COMPARATOR_DATE:
                result = fieldName + " = " + getValue(db, parameter);
                break;

            case Parameter.COMPARATOR_DATE_FROM:
                result = fieldName + " >= " + getValue(db, parameter);
                break;

            case Parameter.COMPARATOR_DATE_UNTIL:
                result = fieldName + " <= " + getValue(db, parameter);
                break;
                
            case Parameter.COMPARATOR_UNLIKE:
                result = fieldName + " <> " + getValue(db, parameter);
                break;
                
            case Parameter.COMPARATOR_MAX_VALUE:
                result = fieldName + " = max(" + fieldName + ")";
                break;
                
            case Parameter.COMPARATOR_MIN_VALUE:
                result = fieldName + " = min(" + fieldName + ")";
                break;
        }
        
        return result;
    }
    
    private Database getDatabase() throws Exception
    {
        return Database.getInstance();
    }
}

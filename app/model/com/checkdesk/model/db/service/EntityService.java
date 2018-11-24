/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.service;

import com.checkdesk.control.ServerConnection;
import com.checkdesk.model.data.Entity;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.model.util.ServerRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final int REQUEST_SELECT_COUNT  = 5;
    
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
    }

    public Entity insert(Entity entity) throws Exception
    {
        return (Entity) ServerConnection.getInstance().say(newRequest(REQUEST_INSERT).addParameter("object", entity));
    }

    public Entity update(Entity entity) throws Exception
    {
        return (Entity) ServerConnection.getInstance().say(newRequest(REQUEST_UPDATE).addParameter("object", entity));
    }

    public void delete(Entity entity) throws Exception
    {
        ServerConnection.getInstance().say(newRequest(REQUEST_DELETE).addParameter("object", entity));
    }

    public Object getValue(Class type, int id) throws Exception
    {
        return ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_UNIQUE).addParameter("type", type)
                                                                                   .addParameter("id", id));
    }

    public Object getValue(Class type, Serializable value) throws Exception
    {
        return ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_UNIQUE).addParameter("type", type)
                                                                                   .addParameter("object", value));
    }

    public Object getViewValue(List<String> columns, String viewName, Map<String, Object> parameters) throws Exception
    {
        return ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_UNIQUE).addParameter("viewName", viewName)
                                                                                   .addParameter("columns", new ArrayList(columns))
                                                                                   .addParameter("parameters", new HashMap(parameters)));
    }

    public void executeFunction(String function, List parameters) throws Exception
    {
        ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_UNIQUE).addParameter("function", function)
                                                                            .addParameter("parameters", new ArrayList(parameters)));
    }

    public Object getValue(Class type, Parameter... parameters) throws Exception
    {
        return ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_UNIQUE).addParameter("type", type)
                                                                                   .addParameter("parameters", parameters));
    }

    public List getValues(Class type) throws Exception
    {
        return getValues(type, false);
    }

    public List getValues(Class type, boolean showInactive) throws Exception
    {
        return getValues(type, showInactive, new Parameter[0]);
    }

    public List getValues(Class type, Parameter... parameters) throws Exception
    {
        return getValues(type, false, parameters);
    }

    public List getValues(Class type, boolean showInactive, Parameter... parameters) throws Exception
    {
        ArrayList<Parameter> p = new ArrayList(Arrays.asList(parameters));
        
        if (!showInactive)
        {
            p.add(Parameter.NOT_INACTIVE_STATE());
        }
        
        return (List) ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_LIST).addParameter("type", type)
                                                                                        .addParameter("parameters", p.toArray(new Parameter[0])));
    }

    public List getFieldValues(Field field, Class type) throws Exception
    {
        return getFieldValues(field, type, new Parameter[0]);
    }

    public List getFieldValues(Field field, Class type, Parameter... parameters) throws Exception
    {
        return (List) ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_LIST).addParameter("field", field.getName())
                                                                                        .addParameter("type", type)
                                                                                        .addParameter("parameters", parameters));
    }
    
    public int countValues(Class type) throws Exception
    {
        return countValues(type, false, new Parameter[0]);
    }

    public int countValues(Class type, boolean showInactive) throws Exception
    {
        return countValues(type, showInactive, new Parameter[0]);
    }

    public int countValues(Class type, Parameter... parameters) throws Exception
    {
        return countValues(type, false, parameters);
    }

    public int countValues(Class type, boolean showInactive, Parameter... parameters) throws Exception
    {
        ArrayList<Parameter> p = new ArrayList(Arrays.asList(parameters));
        
        if (!showInactive)
        {
            p.add(Parameter.NOT_INACTIVE_STATE());
        }
        
        return (int) ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_COUNT).addParameter("type", type)
                                                                                        .addParameter("parameters", p.toArray(new Parameter[0])));
    }

    private ServerRequest newRequest(int request)
    {
        return new ServerRequest().setRequest(ServerRequest.DATABASE)
                                  .addParameter("request", request)
                                  .setWaitResponse(true);
    }
}

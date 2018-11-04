/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.service;

import com.checkdesk.control.ServerConnection;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.model.util.ServerRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
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

    public Serializable save(Serializable entity) throws Exception
    {
        return (Serializable) ServerConnection.getInstance().say(newRequest(REQUEST_INSERT).addParameter("object", entity));
    }

    public Serializable update(Serializable entity) throws Exception
    {
        return (Serializable) ServerConnection.getInstance().say(newRequest(REQUEST_UPDATE).addParameter("object", entity));
    }

    public void delete(Serializable entity) throws Exception
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

    public void executeFunction(String function, Map<String, Object> parameters) throws Exception
    {
        ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_UNIQUE).addParameter("function", function)
                                                                            .addParameter("parameters", new HashMap(parameters)));
    }

    public Object getValue(Class type, List<Parameter> parameters) throws Exception
    {
        return ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_UNIQUE).addParameter("type", type)
                                                                                   .addParameter("parameters", new ArrayList(parameters)));
    }

    public List getValues(Class type) throws Exception
    {
        return getValues(type, new ArrayList<>());
    }

    public List getValues(Class type, List<Parameter> parameters) throws Exception
    {
        return (List) ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_LIST).addParameter("type", type)
                                                                                        .addParameter("parameters", new ArrayList(parameters)));
    }

    public List getFieldValues(Field field, Class type) throws Exception
    {
        return getFieldValues(field, type, new ArrayList<>());
    }

    public List getFieldValues(Field field, Class type, List<Parameter> parameters) throws Exception
    {
        return (List) ServerConnection.getInstance().say(newRequest(REQUEST_SELECT_LIST).addParameter("field", field.getName())
                                                                                        .addParameter("type", type)
                                                                                        .addParameter("parameters", new ArrayList(parameters)));
    }

    private ServerRequest newRequest(int request)
    {
        return new ServerRequest().setRequest(ServerRequest.DATABASE)
                                  .addParameter("request", request)
                                  .setWaitResponse(true);
    }
}

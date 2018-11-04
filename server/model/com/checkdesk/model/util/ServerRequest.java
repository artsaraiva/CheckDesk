/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author MNicaretta
 */
public class ServerRequest
        implements Serializable
{
    public static final int DATABASE    = 0;
    public static final int NOTIFY      = 1;
    public static final int DOWNLOAD    = 2;
    public static final int UPLOAD      = 3;
    public static final int FINISH_FILE = 4;
    public static final int ACTIVE_LOG  = 5;
    
    private int request;
    private HashMap<String, Serializable> parameters = new HashMap<>();
    private boolean waitResponse;

    public ServerRequest()
    {
    }

    public int getRequest()
    {
        return request;
    }

    public ServerRequest setRequest(int request)
    {
        this.request = request;
        
        return this;
    }

    public ServerRequest addParameter(String key, Serializable value)
    {
        this.parameters.put(key, value);
        
        return this;
    }
    
    public Serializable getParameter(String key)
    {
        return this.parameters.get(key);
    }

    public boolean isWaitResponse()
    {
        return waitResponse;
    }

    public ServerRequest setWaitResponse(boolean waitResponse)
    {
        this.waitResponse = waitResponse;
        
        return this;
    }
    
    public List<String> getParameters()
    {
        return new ArrayList<>(parameters.keySet());
    }
}

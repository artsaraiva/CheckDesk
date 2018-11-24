/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.model.util.ServerRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author MNicaretta
 */
public class ConfigurationManager
{
    private static ConfigurationManager instance;

    private static Properties global;

    public static ConfigurationManager getInstance()
    {
        if (instance == null)
        {
            instance = new ConfigurationManager();
        }

        return instance;
    }

    private ConfigurationManager()
    {
        loadProperties();
    }

    private void loadProperties()
    {
        try
        {
            if (global == null)
            {
                global = new Properties();

                File file = new File("config");

                if (!file.exists() || !file.isDirectory())
                {
                    file.mkdirs();
                }
                
                file = new File(file.getAbsolutePath() + File.separator + "global.properties");
                file.createNewFile();

                InputStream fileInputStream = new FileInputStream(file.getAbsolutePath());

                global.load(fileInputStream);

                fileInputStream.close();
            }
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
    
    public void setString(String key, String value)
    {
        try
        {
            ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.CONFIGURATION)
                                                                  .addParameter("key", key)
                                                                  .addParameter("value", value != null ? value : "")
                                                                  .setWaitResponse(true));
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }

    public void setFlag(String key, Boolean value)
    {
        String v = null;
        
        if (value != null)
        {
            v = String.valueOf(value);
        }
        
        setString(key, v);
    }

    public String getString(String key)
    {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue)
    {
        String result = defaultValue;
        
        try
        {
            result = (String) ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.CONFIGURATION)
                                                                                    .addParameter("key", key)
                                                                                    .addParameter("defaultValue", defaultValue)
                                                                                    .setWaitResponse(true));
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }

    public Boolean getFlag(String key)
    {
        return getFlag(key, null);
    }

    public Boolean getFlag(String key, Boolean defaultValue)
    {
        Boolean result = defaultValue;
        
        String value = getString(key);
        
        if (value != null && !value.isEmpty())
        {
            result = Boolean.valueOf(value);
        }
        
        return result;
    }

    public Integer getInteger(String key)
    {
        return getInteger(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue)
    {
        Integer result = defaultValue;
        
        String value = getString(key);
        
        if (value != null && !value.isEmpty())
        {
            result = Integer.valueOf(value);
        }
        
        return result;
    }
    
    public void setUserPropertie(String key, String value)
    {
        global.setProperty(key, value);
        store();
    }
    
    public String getUserPropertie(String key)
    {
        return getUserPropertie(key, null);
    }
    
    public String getUserPropertie(String key, String defaultValue)
    {
        return global.getProperty(key, defaultValue);
    }
    
    private void store()
    {
        try
        {
            global.store(new FileOutputStream("config" + File.separator + "global.properties"), "");
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
}

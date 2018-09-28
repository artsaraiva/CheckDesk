/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

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
    
    public void setProperty(String key, String value)
    {
        global.setProperty(key, value);
        store();
    }

    public void setFlag(String key, Boolean value)
    {
        String v = null;
        
        if (value != null)
        {
            v = String.valueOf(value);
        }
        
        setProperty(key, v);
    }

    public String getProperty(String key)
    {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue)
    {
        return global.getProperty(key, defaultValue);
    }

    public boolean getFlag(String key)
    {
        return getFlag(key, false);
    }

    public boolean getFlag(String key, boolean defaultValue)
    {
        boolean result = defaultValue;
        
        String value = global.getProperty(key);
        
        if (value != null && !value.isEmpty())
        {
            result = Boolean.valueOf(value);
        }
        
        return result;
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

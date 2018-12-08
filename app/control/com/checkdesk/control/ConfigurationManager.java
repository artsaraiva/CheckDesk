/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.model.util.ServerRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Properties getRelease()
    {
        File lastRelease = null;
        File dir = new File("releases");

        if (!dir.exists() || !dir.isDirectory())
        {
            dir.mkdirs();
        }

        for (File fileEntry : dir.listFiles())
        {
            if (!fileEntry.isDirectory())
            {
                lastRelease = fileEntry;
            }
        }

        return getRelease(lastRelease);
    }

    public Properties getRelease(String fileName)
    {
        File lastRelease = null;

        File[] result = new File("releases").listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.startsWith(fileName);
            }
        });

        if (result != null && result.length > 0)
        {
            return getRelease(result[0]);
        }

        return new Properties();
    }

    public Properties getRelease(File file)
    {
        Properties release = new Properties();

        if (file != null && file.exists())
        {
            try
            {
                try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8"))
                {
                    release.load(isr);
                }
            }
            catch (IOException e)
            {
                ApplicationController.logException(e);
            }
        }

        return release;
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

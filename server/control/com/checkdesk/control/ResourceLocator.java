/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import java.io.InputStream;

/**
 *
 * @author arthu
 */
public class ResourceLocator
{
    private static ResourceLocator instance;

    public static ResourceLocator getInstance()
    {
        if ( instance == null )
        {
            instance = new ResourceLocator();
        }

        return instance;
    }

    private String configPath;

    private ResourceLocator()
    {
        load();
    }

    private void load()
    {
        configPath = getClass().getClassLoader().getResource( "config" ).toString() + "/";
    }

    public String getConfigResource( String name )
    {
        return configPath + name;
    }
    
    public InputStream getConfigStream(String name)
    {
        return getClass().getClassLoader().getResourceAsStream("config/" + name);
    }
}

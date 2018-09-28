/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

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

    private String imagesPath;
    private String stylePath;
    private String configPath;

    private ResourceLocator()
    {
        load();
    }

    private void load()
    {
        imagesPath = getClass().getClassLoader().getResource( "images" ).toString() + "/";
        stylePath = getClass().getClassLoader().getResource( "styles" ).toString() + "/";
        configPath = getClass().getClassLoader().getResource( "config" ).toString() + "/";
    }

    public String getImageResource( String name )
    {
        if (name == null)
        {
            name = "";
        }
        
        if(!name.endsWith(".png"))
        {
            name += ".png";
        }

        return imagesPath + name;
    }

    public String getStyleResource( String name )
    {
        return stylePath + name;
    }

    public String getConfigResource( String name )
    {
        return configPath + name;
    }
}

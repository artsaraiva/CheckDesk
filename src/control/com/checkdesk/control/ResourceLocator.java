/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import java.io.File;

/**
 *
 * @author arthu
 */
public class ResourceLocator
{

    private static ResourceLocator instance;

    public static ResourceLocator getInstance()
    {
        if (instance == null)
        {
            instance = new ResourceLocator();
        }

        return instance;
    }

    private String imagesPath;
    private String stylePath;

    private ResourceLocator()
    {
        load();
    }

    private void load()
    {
        imagesPath = getClass().getClassLoader().getResource("images").toString() + File.separator;
        stylePath = getClass().getClassLoader().getResource("styles").toString() + File.separator;
    }

    public String getImageResource(String name)
    {
        return imagesPath + name;
    }

    public String getStyleResource(String name)
    {
        return stylePath + name;
    }
}

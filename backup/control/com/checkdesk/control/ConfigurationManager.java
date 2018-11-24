/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author MNicaretta
 */
public class ConfigurationManager
{
    public static Properties loadProperties(File file)
    {
        Properties properties = null;

        try
        {
            if ( file == null )
            {
                file = new File("config" + File.separator + "global.properties");
            }

            if (file.exists())
            {
                try (InputStream fileInputStream = new FileInputStream(file.getAbsolutePath()))
                {
                    properties = new Properties();
                    properties.load(fileInputStream);
                }
            }
        }

        catch (IOException e)
        {
            e.printStackTrace(System.out);
        }

        return properties;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.control.util.LogUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.Log;
import com.checkdesk.model.data.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MNicaretta
 */
public class ApplicationController
{

    private static ApplicationController defaultInstance;
    private static boolean activeLog;

    public static ApplicationController getInstance()
    {
        if (defaultInstance == null)
        {
            defaultInstance = new ApplicationController();
        }

        return defaultInstance;
    }

    public static void logException(Throwable e)
    {
        try
        {
            e.printStackTrace();
            
            File file = new File("config" + File.separator + "logs");
            
            if (!file.exists() || !file.isDirectory())
            {
                file.mkdirs();
            }
            
            Date date = new Date();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            file = new File(file.getAbsolutePath() + File.separator + dateFormat.format(date) + ".txt");

            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
            
            try
            {
                e.printStackTrace(printWriter);
            }

            finally
            {
                printWriter.close();
            }
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static String hash(String value)
    {
        try
        {
            if (value == null)
            {
                return null;
            }

            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

            return "<" + String.format("%040x", new BigInteger(messageDigest.digest(value.getBytes())).abs()) + ">";
        }

        catch (Exception e)
        {
            logException(e);
        }

        return value;
    }

    public static boolean isActiveLog()
    {
        return activeLog;
    }

    public static void setActiveLog(boolean activeLog)
    {
        ApplicationController.activeLog = activeLog;
        ConfigurationManager.getInstance().setFlag("logs.monitor", activeLog);
        
        Log log = new Log(0,
                          getInstance().activeUser,
                          Log.EVENT_ACTIVE_LOGS,
                          activeLog ? "Ativar" : "Desativar",
                          "Auditoria",
                          "A auditoria foi " + (activeLog ? "ativada" : "desativada"));
        
        LogUtilities.addLog(log);
    }

    private User activeUser;

    private ApplicationController()
    {
        activeLog = ConfigurationManager.getInstance().getFlag("logs.monitor");
    }

    public User getActiveUser()
    {
        return activeUser;
    }

    public User login(String login, String password)
    {
        try
        {
            activeUser = UserUtilities.login(login, hash(password));
        }

        catch (Exception e)
        {
            logException(e);
        }

        return activeUser;
    }
}

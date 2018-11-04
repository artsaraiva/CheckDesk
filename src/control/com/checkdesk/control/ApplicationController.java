/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.control.util.LogUtilities;
import com.checkdesk.model.data.Attachment;
import com.checkdesk.model.data.Log;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.ServerRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author MNicaretta
 */
public class ApplicationController
{
    private static ApplicationController defaultInstance;

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
        try
        {
            return (boolean) ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.ACTIVE_LOG).setWaitResponse(true));
        }
        
        catch (Exception e)
        {
            logException(e);
        }
        
        return false;
    }

    public static void setActiveLog(boolean activeLog)
    {
        try
        {
            ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.ACTIVE_LOG)
                                                                  .addParameter("newValue", activeLog)
                                                                  .setWaitResponse(false));
        }
        
        catch (Exception e)
        {
            logException(e);
        }
    }

    private User activeUser;
    private Window rootWindow;

    private ApplicationController()
    {
    }
    
    public void close() throws Exception
    {
        defaultInstance = null;
        notify(null);
    }

    public User getActiveUser()
    {
        return activeUser;
    }

    public User login(String login, String password)
    {
        try
        {
            activeUser = (User) ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.DATABASE)
                                                                                      .addParameter("user", login)
                                                                                      .addParameter("password", hash(password))
                                                                                      .setWaitResponse(true));
        }
        
        catch (Exception e)
        {
            logException(e);
        }
        
        return activeUser;
    }

    public Window getRootWindow()
    {
        return rootWindow;
    }

    public void setRootWindow(Window rootWindow)
    {
        this.rootWindow = rootWindow;
    }
    
    public void notify(Serializable object)
    {
        try
        {
            ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.NOTIFY)
                                                                  .addParameter("object", object)
                                                                  .setWaitResponse(false));
        }
        
        catch (Exception e)
        {
            logException(e);
        }
    }
    
    public void saveFile(Attachment attachment)
    {
        try
        {
            if (attachment.getAttachment() != null)
            {
                ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.UPLOAD)
                                                                      .addParameter("object", attachment)
                                                                      .setWaitResponse(false));
                
                ServerConnection.getInstance().sendFile(attachment.getAttachment());
            }
        }
        
        catch (Exception e)
        {
            logException(e);
        }
    }
    
    public void downloadFile(Attachment attachment)
    {
        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName( attachment.toString() );
            File file = fileChooser.showSaveDialog(getRootWindow());
            
            ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.DOWNLOAD)
                                                                  .addParameter("object", attachment)
                                                                  .setWaitResponse(false));
            
            ServerConnection.getInstance().receiveFile(file);
        }
        
        catch (Exception e)
        {
            logException(e);
        }
    }
}

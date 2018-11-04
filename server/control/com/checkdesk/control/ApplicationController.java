/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.control.util.AttachmentUtilities;
import com.checkdesk.control.util.LogUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.Attachment;
import com.checkdesk.model.data.Log;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.ServerRequest;
import com.server.checkdesk.main.HandleClient;
import com.server.checkdesk.main.Main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MNicaretta
 */
public class ApplicationController
{
    private static ThreadLocal<ApplicationController> localController = new ThreadLocal<>();
    
    public static ApplicationController getInstance()
    {
        if (localController.get() == null)
        {
            localController.set(new ApplicationController());
        }
        
        return localController.get();
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
        return ConfigurationManager.getInstance().getFlag("logs.monitor");
    }

    public static void setActiveLog(boolean activeLog)
    {
        ConfigurationManager.getInstance().setFlag("logs.monitor", activeLog);
        
        Log log = new Log();
        log.setUserId(getInstance().activeUser.getId());
        log.setEvent(Log.EVENT_ACTIVE_LOGS);
        log.setObjectName(activeLog ? "Ativar" : "Desativar");
        log.setObjectClass("Auditoria");
        log.setCommand("A auditoria foi " + (activeLog ? "ativada" : "desativada"));
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        
        LogUtilities.addLog(log);
    }

    private User activeUser;

    private ApplicationController()
    {
    }

    public User getActiveUser()
    {
        return activeUser;
    }

    public User login(String login, String password)
    {
        try
        {
            activeUser = UserUtilities.login(login, password);
        }

        catch (Exception e)
        {
            logException(e);
        }

        return activeUser;
    }
    
    public Object handle(ServerRequest request, HandleClient client) throws Exception
    {
        Object result = null;
        
        if (request != null)
        {
            switch (request.getRequest())
            {
                case ServerRequest.DATABASE:
                    if (request.getParameter("request") == null)
                    {
                        result = login((String) request.getParameter("user"), (String) request.getParameter("password"));
                    }
                    
                    else
                    {
                        result = EntityService.getInstance().handleRequest(this, request);
                    }
                    break;
                
                case ServerRequest.NOTIFY:
                    Serializable object = request.getParameter("object");
                    
                    NotificationController.getInstance().sendNotification(object);
                    Main.notify(object);
                    break;
                    
                case ServerRequest.DOWNLOAD:
                    Attachment attachment = (Attachment) request.getParameter("object");
                    AttachmentUtilities.download(attachment, client);
                    break;
                    
                case ServerRequest.UPLOAD:
                    attachment = (Attachment) request.getParameter("object");
                    AttachmentUtilities.upload(attachment, client);
                    break;
                    
                case ServerRequest.FINISH_FILE:
                    client.finishUpload();
                    break;
                    
                case ServerRequest.ACTIVE_LOG:
                    if (request.getParameter("newValue") != null)
                    {
                        setActiveLog((boolean) request.getParameter("newValue"));
                    }
                    
                    else
                    {
                        result = isActiveLog();
                    }
            }
        }
        
        return result;
    }
}

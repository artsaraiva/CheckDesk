/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.model.data.Survey;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.ServerRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author arthu
 */
public class ServerConnection
{
    private static ServerConnection serverConnection;
    
    public static ServerConnection getInstance()
    {
        if (serverConnection == null)
        {
            String serverAddress = ConfigurationManager.getInstance().getString("server.address");
            Integer serverPort = ConfigurationManager.getInstance().getInteger("server.port");
            
            if (serverAddress != null && serverPort != null)
            {
                try
                {
                    serverConnection = new ServerConnection(new Socket(serverAddress, serverPort));
                }
                
                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }
        
        return serverConnection;
    }
    
    public static String getConnectionSettings()
    {
        String result = null;
        
        String serverAddress = ConfigurationManager.getInstance().getString("server.address");
        Integer serverPort = ConfigurationManager.getInstance().getInteger("server.port");

        if (serverAddress != null && serverPort != null)
        {
            result = serverAddress + ":" + serverPort;
        }
        
        return result;
    }
    
    public static void setConnectionSettings(String setting)
    {
        String serverAddress = null;
        String serverPort = null;
        
        String[] split = setting.split(":");
        
        if (split.length == 2)
        {
            serverAddress = split[0];
            serverPort = split[1];
        }

        ConfigurationManager.getInstance().setString("server.address", serverAddress);
        ConfigurationManager.getInstance().setString("server.port", serverPort);
    }
    
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    
    private FileInputStream fis = null;
    private FileOutputStream fos = null;
   
    private ServerConnection(Socket client)
    {
        try
        {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            
            new ConnectionListener(new Socket(client.getInetAddress(), client.getPort())).start();
        }
        catch (Exception ex)
        {
           ApplicationController.logException(ex);
        }
    }
    
    public Object say(ServerRequest request) throws Exception
    {
        Object result = null;
        
        out.writeObject(request);
        
        if (request.isWaitResponse())
        {
            result = in.readObject();
        }
        
        return result;
    }
    
    public void sendFile(File file) throws Exception
    {
        byte[] bytes = new byte[16*1024];
        InputStream in = new FileInputStream(file);
        
        int count = 0;
        
        while((count = in.read(bytes)) > 0)
        {
            out.write(bytes, 0, count);
        }
        
        out.writeObject(new ServerRequest().setRequest(ServerRequest.FINISH_FILE));
        in.close();
    }
    
    public void receiveFile(File file) throws Exception
    {
        if (fos == null)
        {
            fos = new FileOutputStream(file);
        }
        
        byte[] bytes = new byte[16 * 1024];

        int contador;
        
        while ((contador = in.read(bytes)) > 0)
        {
            fos.write(bytes, 0, contador);
        }
        
        Object result = in.readObject();
        if (result instanceof ServerRequest && ((ServerRequest) result).getRequest() == ServerRequest.FINISH_FILE)
        {
            finishFile();
        }
    }
    
    private void finishFile() throws Exception
    {
        fos.close();
        fos = null;
    }
}

class ConnectionListener extends Thread
{
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    
    public ConnectionListener(Socket client)
    {
        try
        {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            
            out.flush();
            out.writeObject(null);
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
    
    @Override
    public void run()
    {
        Object mens = null;
        
        try
        {
            while((mens = in.readObject()) != null)
            {
                handle(mens);
            }
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
    
    private void handle(Object mens) throws Exception
    {
        if ( mens instanceof ServerRequest )
        {
            ServerRequest request = (ServerRequest) mens;
            
            switch (request.getRequest())
            {
                case ServerRequest.NOTIFY:
                    notify(request.getParameter("object"));
                    break;
            }
        }
    }
    
    private void notify(final Object object)
    {
        if (object instanceof Survey)
        {
            Platform.runLater( new Runnable()
            {
                @Override
                public void run()
                {
                    Notifications.create()
                                 .title("Nova pesquisa!")
                                 .text("Uma nova pesquisa foi criada: " + ((Survey) object).getTitle())
                                 .graphic(new ImageView(new Image(ResourceLocator.getInstance().getImageResource("mp_survey.png"))))
                                 .position(Pos.BOTTOM_RIGHT)
                                 .hideAfter(Duration.seconds(5))
                                 .owner(ApplicationController.getInstance().getRootWindow())
                                 .show();
                }
            } );
            
        }
    }
}

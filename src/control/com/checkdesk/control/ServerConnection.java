/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arthu
 */
public class ServerConnection
{
    private static final int PORT = 5000;
    private static final String SERVER_IP = "127.0.0.1";
    
    private Socket client;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
   
    public ServerConnection()
    {
        try
        {
            client = new Socket(SERVER_IP, PORT);
            
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            
            new ConnectionListener().start();
            new ConnectionSpeaker().start();
        }
        catch (Exception ex)
        {
           ApplicationController.logException(ex);
        }
    }
    
}

class ConnectionListener extends Thread
{
    @Override
    public void run()
    {
        while(true)
        {
            
        }
    }
}

class ConnectionSpeaker extends Thread
{
    @Override
    public void run()
    {
        
    }
}
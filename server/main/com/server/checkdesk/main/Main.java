/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.checkdesk.main;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ConfigurationManager;
import com.checkdesk.model.db.service.EntityService;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arthu
 */
public class Main extends Thread
{
    public static final int PORT = ConfigurationManager.getInstance().getInteger("address.port", 5000);
    public static List<HandleClient> listners = new ArrayList<>();

    public static void main(String[] args)
    {
        new Main().start();
    }

    public static void notify(Serializable object) throws Exception
    {
        for (HandleClient client : listners)
        {
            client.notify(object);
        }
    }
    
    @Override
    public void run()
    {
        try
        {
            EntityService.getInstance();
            ServerSocket serverSocket = new ServerSocket(PORT);

            System.out.println("--------------------------------\n" +
                               "Server started: " + new Timestamp(System.currentTimeMillis()) + "\n" +
                               "IP Address: " + Inet4Address.getLocalHost().getHostAddress() + ":" + PORT + "\n" +
                               "--------------------------------\n");
            
            while (true)
            {
                new HandleClient(serverSocket.accept()).start();
            }
        }
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
}

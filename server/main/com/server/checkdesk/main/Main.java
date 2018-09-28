/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.checkdesk.main;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author arthu
 */
public class Main extends Thread
{

    public static final int PORT = 5000;

    public static void main(String[] args)
    {
        new Main().start();
    }

    @Override
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true)
            {
                new HandleClient(serverSocket.accept()).start();
            }
        }
        catch (IOException ex)
        {
            ApplicationController.logException(ex);
        }
    }
}

class HandleClient extends Thread
{

    private final Socket client;

    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    private final ApplicationController controller = ApplicationController.getInstance();

    public HandleClient(Socket socket)
    {
        this.client = socket;
    }

    @Override
    public void run()
    {
        try
        {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            JSONObject login = new JSONObject(in.readObject());
            User user = controller.login(login.getString("user"), login.getString("password"));

            out.writeObject(user);

            Object request = in.readObject();

            while (request != null)
            {
                out.writeObject(controller.handle(request));
            }
        }
        catch (Exception ex)
        {
            ApplicationController.logException(ex);
        }
    }
}

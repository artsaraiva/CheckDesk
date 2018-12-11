/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.checkdesk.main;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.util.ServerRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.Timestamp;

/**
 *
 * @author MNicaretta
 */
public class HandleClient extends Thread
{

    private final Socket client;

    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    private FileInputStream fis = null;
    private FileOutputStream fos = null;

    private ApplicationController controller;

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

            Object first = in.readObject();

            if (first == null)
            {
                Main.listners.add(this);
            }

            else
            {
                controller = ApplicationController.getInstance();
                System.out.println("Client connected: " + new Timestamp(System.currentTimeMillis()) + "\n"
                        + "IP Address: " + client.getInetAddress().getHostAddress() + "\n"
                        + "--------------------------------\n");

                ServerRequest request = (ServerRequest) first;

                out.writeObject(controller.handle(request, this));

                while ((request = (ServerRequest) in.readObject()) != null)
                {
                    Object response = null;

                    try
                    {
                        response = controller.handle(request, this);
                    }

                    finally
                    {
                        if (request.isWaitResponse())
                        {
                            out.writeObject(response);
                        }
                    }
                }

                System.out.println("Client disconnected: " + new Timestamp(System.currentTimeMillis()) + "\n"
                        + "IP Address: " + client.getInetAddress().getHostAddress() + "\n"
                        + "--------------------------------\n");

                client.close();
            }
        }

        catch (Exception ex)
        {
            ApplicationController.logException(ex);
        }
    }

    public Object getObject() throws Exception
    {
        return in.readObject();
    }

    public void notify(Serializable object) throws Exception
    {
        if (object != null)
        {
            ServerRequest request = new ServerRequest().setRequest(ServerRequest.NOTIFY)
                    .addParameter("object", object)
                    .setWaitResponse(false);

            out.flush();
            out.writeObject(request);
        }

        else
        {
            try
            {
                out.writeObject(null);
                client.close();
            }
            catch (IOException e) {}
        }
    }

    public void download(File file) throws Exception
    {
        if (fis == null)
        {
            fis = new FileInputStream(file);
        }

        byte[] bytes = new byte[16 * 1024];
        int count = 0;

        while ((count = fis.read(bytes)) > 0)
        {
            out.write(bytes, 0, count);
        }

        out.writeObject(new ServerRequest().setRequest(ServerRequest.FINISH_FILE));
        fis.close();
        fis = null;
    }

    public void upload(File file) throws Exception
    {
        if (fos == null)
        {
            fos = new FileOutputStream(file);
        }

        byte[] bytes = new byte[16 * 1024];

        int count;

        while ((count = in.read(bytes)) > 0)
        {
            fos.write(bytes, 0, count);
        }
    }

    public void finishUpload() throws Exception
    {
        fos.close();
        fos = null;
    }
}

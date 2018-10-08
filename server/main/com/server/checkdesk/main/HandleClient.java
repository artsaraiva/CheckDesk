/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.checkdesk.main;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.User;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author MNicaretta
 */
public class HandleClient extends Thread
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

            Object first = in.readObject();
            
            if (first == null)
            {
                Main.listners.add(this);
            }
            
            else
            {
                JSONObject login = new JSONObject((String) first);
                User user = controller.login(login.getString("user"), login.getString("password"));

                out.flush();
                out.writeObject(user);

                Object request = in.readObject();

                while (request != null)
                {
                    out.writeObject(controller.handle(request, this));
                }
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
    
    public void notify(Object object) throws Exception
    {
        out.flush();
        out.writeObject(ApplicationController.NOTIFY);
        
        out.flush();
        out.writeObject(object);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.UserService;
import java.io.File;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;

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
            File file = new File("C:\\Users\\arthu\\Desktop\\testeLog\\teste.txt");
            file.getParentFile().mkdirs();

            PrintWriter printWriter = new PrintWriter(file);

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
            e.printStackTrace();
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
            activeUser = UserService.getInstance().login(login, hash(password));
        }

        catch (Exception e)
        {
            logException(e);
        }

        return activeUser;
    }
}

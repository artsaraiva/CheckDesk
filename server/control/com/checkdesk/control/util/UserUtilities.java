/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;

/**
 *
 * @author arthu
 */
public class UserUtilities
{

    private static final Item TYPE_SUPER = new Item("Super", User.TYPE_SUPER);
    private static final Item TYPE_ADMIN = new Item("Admin", User.TYPE_ADMIN);
    private static final Item TYPE_OPERATOR = new Item("Operator", User.TYPE_OPERATOR);
    private static final Item TYPE_EXPLORER = new Item("Explorer", User.TYPE_EXPLORER);

    public static void addUser(User user) throws Exception
    {
        EntityService.getInstance().save(user);
    }

    public static void editUser(User user) throws Exception
    {
        EntityService.getInstance().update(user);
    }

    public static void deleteUser(User user) throws Exception
    {
        EntityService.getInstance().delete(user);
    }

    public static ObservableList<Item> getItems()
    {
        return FXCollections.observableArrayList(TYPE_SUPER, TYPE_ADMIN, TYPE_OPERATOR, TYPE_EXPLORER);
    }
    
    public static User getUser(int id)
    {
        User user = null;
        
        try
        {
            user = (User) EntityService.getInstance().getValue(User.class, id);
        }
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return user;
    }

    public static Item getType(int type)
    {
        Item result = null;

        for (Item item : getItems())
        {
            if (item.getValue() == type)
            {
                result = item;
                break;
            }
        }

        return result;
    }

    public static User login(String login, String password) throws Exception
    {
        List<Parameter> parameters = Arrays.asList(new Parameter("login",
                                                                 User.class.getDeclaredField(login.contains("@") ? "email" : "login"),
                                                                 login.toLowerCase(),
                                                                 Parameter.COMPARATOR_LOWER_CASE),
                                                    new Parameter("password",
                                                                  User.class.getDeclaredField("password"),
                                                                  password,
                                                                  Parameter.COMPARATOR_EQUALS));

        return (User) EntityService.getInstance().getValue(User.class, parameters);
    }

    public static List<User> getUsers()
    {
        List<User> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(User.class);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }
}

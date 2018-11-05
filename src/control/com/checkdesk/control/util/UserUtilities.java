/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.model.data.Entity;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.views.editors.UserEditor;
import com.checkdesk.views.util.EditorCallback;
import com.checkdesk.views.parts.Prompts;
import java.util.ArrayList;
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

    public static void addUser()
    {
        User user = new User();

        new UserEditor(new EditorCallback<User>(user)
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    EntityService.getInstance().save(getSource());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }

    public static void editUser(User user)
    {
        new UserEditor(new EditorCallback<User>(user)
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    EntityService.getInstance().update(getSource());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }

    public static void deleteUser(User user)
    {
        if (Prompts.confirm("Exclusão de usuário", "Deseja mesmo excluir o usuário " + user.getName() + "?"))
        {
            try
            {
                EntityService.getInstance().delete(user);
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
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
        return (User) EntityService.getInstance().getValue(User.class, new Parameter(User.class.getDeclaredField(login.contains("@") ? "email" : "login"),
                                                                                     login.toLowerCase(),
                                                                                     Parameter.COMPARATOR_LOWER_CASE),
                    
                                                                       new Parameter(User.class.getDeclaredField("password"),
                                                                                     password,
                                                                                     Parameter.COMPARATOR_EQUALS));
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
    
    public static String getUserIcon(int userId)
    {
        return ResourceLocator.getInstance().getImageResource(getUser(userId).getLogin());
    }
    
    public static String maskPhone( String phone )
    {
        String result = "";
        phone = phone.replaceAll( "[^0-9]", "" );

        // (__)|_____-|____
        //  $1 |  $2  | $3 

        // $1
        int i = 2;
        String split = phone.replaceFirst( "([0-9]{" + i + "})([0-9]{" + Math.max( 1, phone.length() - i ) + "})", "($1) " );
        phone = phone.length() > i ? phone.substring( i ) : "";
        result += split;

        // $2
        i = phone.length() > 8 ? 5 : 4;
        split = phone.replaceFirst( "([0-9]{" + i + "})([0-9]{" + Math.max( 1, phone.length() - i ) + "})", "$1-" );
        phone = phone.length() > i ? phone.substring( i ) : "";
        result += split;

        // $3
        result += phone;
        return result;
    }
    
    public static boolean isUniqueLogin(User user, String login)
    {
        boolean result = false;
        
        try
        {
            Parameter[] parameters = new Parameter[]
            {
                new Parameter(User.class.getDeclaredField("login"), login, Parameter.COMPARATOR_EQUALS),
                new Parameter(Entity.class.getDeclaredField("id"), user.getId(), Parameter.COMPARATOR_UNLIKE)
            };
            
            result = EntityService.getInstance().countValues(User.class, parameters) == 0;
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
    
    public static boolean isUniqueEmail(User user, String email)
    {
        boolean result = false;
        
        try
        {
            Parameter[] parameters = new Parameter[]
            {
                new Parameter(User.class.getDeclaredField("email"), email, Parameter.COMPARATOR_EQUALS),
                new Parameter(Entity.class.getDeclaredField("id"), user.getId(), Parameter.COMPARATOR_UNLIKE)
            };
            
            result = EntityService.getInstance().countValues(User.class, parameters) == 0;
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
}

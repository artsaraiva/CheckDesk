/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.views.editors.UserEditor;
import com.checkdesk.views.util.EditorCallback;
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
                    e.printStackTrace();
                }
            }
        }).show();
    }

    public static ObservableList<Item> getItems()
    {
        return FXCollections.observableArrayList(TYPE_SUPER, TYPE_ADMIN, TYPE_OPERATOR, TYPE_EXPLORER);
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
}

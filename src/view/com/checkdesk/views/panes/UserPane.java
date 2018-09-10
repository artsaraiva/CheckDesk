/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.User;
import com.checkdesk.views.parts.DefaultTable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 *
 * @author arthu
 */
public class UserPane
        extends DefaultPane
{

    public UserPane()
    {
        initComponents();
    }

    @Override
    protected void resize()
    {
        userTable.setPrefHeight(getHeight());
        userTable.setPrefWidth(getWidth());
    }

    @Override
    public void refreshContent()
    {
        userTable.setItems(UserUtilities.getUsers());
    }

    private void removeSelected()
    {
        UserUtilities.deleteUser(userTable.getSelectedItem());
        refreshContent();
    }

    private void editSelected()
    {
        UserUtilities.editUser(userTable.getSelectedItem());
        refreshContent();
    }

    private void addUser()
    {
        UserUtilities.addUser();
        refreshContent();
    }

    private void initComponents()
    {
        userTable.setActions(new MenuItem[]
        {
            edit, delete
        });
        getChildren().add(userTable);

        userTable.addEventHandler(DefaultTable.Events.ON_ADD, new EventHandler()
        {
            @Override
            public void handle(Event event)
            {
                addUser();
            }
        });
    }

    private DefaultTable<User> userTable = new DefaultTable();

    private MenuItem edit = new MenuItem("Editar");
    private MenuItem delete = new MenuItem("Excluir");
    {
        delete.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                removeSelected();
            }
        });

        edit.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                editSelected();
            }
        });
    }
}

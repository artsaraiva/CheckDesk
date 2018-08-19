/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.model.data.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author arthu
 */
public class GroupTable
        extends DefaultTable<User>
{
    
    public GroupTable()
    {
        initComponents();
    }
    
    private void removeSelected()
    {
        getItems().remove(getSelectedItem());
    }
    
    private void addUser()
    {
        getItems().add(new User(0, "Fulano", "teste", "teste", "teste", "teste", 0));
    }
    
    private void initComponents()
    {
        remove.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                removeSelected();
            }
        });
        addEventHandler(Events.ON_ADD, new EventHandler()
        {
            @Override
            public void handle(Event event)
            {
                addUser();
            }
        });
        
        setActions(new javafx.scene.control.MenuItem[]{remove});
    }
    
    private javafx.scene.control.MenuItem remove = new javafx.scene.control.MenuItem( "Remover" );
}

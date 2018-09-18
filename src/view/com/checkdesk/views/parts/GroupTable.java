/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.util.GroupUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.Group;
import com.checkdesk.model.data.User;
import com.checkdesk.views.pickers.ItemPicker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.Event;

/**
 *
 * @author arthu
 */
public class GroupTable
        extends DefaultTable<User>
{
    private Group group;
    private boolean saveOnChange = false;
    private boolean showAll = true;
    
    public GroupTable()
    {
        initComponents();
    }

    public void setSaveOnChange(boolean saveOnChange)
    {
        this.saveOnChange = saveOnChange;
    }

    public void setShowAll(boolean showAll)
    {
        this.showAll = showAll;
    }
    
    private void removeSelected()
    {
        getItems().remove(getSelectedItem());
        setItems(getItems());
        
        if (saveOnChange)
        {
            createGroup();
        }
    }
    
    private void addUser()
    {
        userPicker.setItems(UserUtilities.getUsers());
        userPicker.open("Selecione um Usuário");
        
        if (userPicker.getSelected() != null)
        {
            getItems().remove(GroupUtilities.allUser);
            getItems().add(userPicker.getSelected());
        }
        
        setItems(getItems());
        
        if (saveOnChange)
        {
            createGroup();
        }
    }

    public void setGroup(Group group)
    {
        this.group = group;
        
        List<User> users = new ArrayList<>();
        
        if (group != null)
        {
            users.addAll(group.getUsers());
        }
        
        setItems(users);
    }

    @Override
    public void setItems(List<User> items)
    {
        if (items.isEmpty() && showAll)
        {
            items.add(GroupUtilities.allUser);
            setActions(new javafx.scene.control.MenuItem[0]);
        }
        
        else
        {
            setActions(new javafx.scene.control.MenuItem[]{remove});
        }
        
        super.setItems(items);
    }
    
    public Group createGroup()
    {
        if (group == null)
        {
            group = new Group();
            group.setName("");
        }

        Set<User> users = new HashSet(getItems());
        
        group.setUsers(users);
        
        GroupUtilities.saveGroup(group);
        
        return group.getId() == 0 ? null : group;
    }

    public void setTableDisable(boolean disable)
    {
        setAddButtonDisabled(disable);
        remove.setDisable(disable);
    }
    
    private void initComponents()
    {
        remove.setOnAction((ActionEvent event) ->
        {
            removeSelected();
        });
        
        addEventHandler(Events.ON_ADD, (Event event) ->
        {
            addUser();
        });
    }
    
    private ItemPicker<User> userPicker = new ItemPicker<>();
    private javafx.scene.control.MenuItem remove = new javafx.scene.control.MenuItem( "Remover" );
}

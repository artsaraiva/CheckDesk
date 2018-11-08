/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.pickers;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.PermissionController;
import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.model.data.Option;
import com.checkdesk.views.parts.DefaultTable;
import javafx.scene.control.MenuItem;

/**
 *
 * @author MNicaretta
 */
public class OptionPicker
        extends ItemPicker<Option>
{
    public OptionPicker()
    {
        initComponents();
    }
    
    private void addOption()
    {
        FormUtilities.addOption();
        setItems(FormUtilities.getOptions());
    }
    
    private void editOption()
    {
        FormUtilities.editOption(getSelected());
        setItems(FormUtilities.getOptions());
    }
    
    private void deleteOption()
    {
        FormUtilities.deleteOption(getSelected());
        setItems(FormUtilities.getOptions());
    }
    
    private void initComponents()
    {
        getDialogPane().setPrefHeight(400);
        
        list.setShowAddPane(true);
        
        list.addEventHandler(DefaultTable.Events.ON_ADD, (event) ->
        {
            addOption();
        });
    }
    
    private MenuItem editItem = new MenuItem("Editar");
    private MenuItem deleteItem = new MenuItem("Excluir");
    {
        editItem.setDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance()
                .getActiveUser(), "edit.option"));

        deleteItem.setDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance()
                .getActiveUser(), "delete.option"));
        
        editItem.setOnAction((event) ->
        {
            editOption();
        });
        
        deleteItem.setOnAction((event) ->
        {
            deleteOption();
        });
    }
}

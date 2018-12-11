/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.pickers;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.PermissionController;
import com.checkdesk.control.util.CategoryUtilities;
import com.checkdesk.model.data.Category;
import com.checkdesk.views.parts.DefaultTable;
import javafx.scene.control.MenuItem;

/**
 *
 * @author arthu
 */
public class CategoryPicker
        extends ItemPicker<Category>
{
    public CategoryPicker()
    {
        initComponents();
    }
    
    private void addCategory()
    {
        CategoryUtilities.addCategory();
        setItems(CategoryUtilities.getCategories());
    }
    
    private void editOption()
    {
        CategoryUtilities.editCategory(getSelected());
        setItems(CategoryUtilities.getCategories());
    }
    
    private void deleteOption()
    {
        CategoryUtilities.deleteCategory(getSelected());
        setItems(CategoryUtilities.getCategories());
    }
    
    private void initComponents()
    {
        getDialogPane().setPrefHeight(400);
        
        list.setShowAddPane(true);
        list.setActions(new MenuItem[]
        {
            editItem,
            deleteItem
        });
        
        list.addEventHandler(DefaultTable.Events.ON_ADD, (event) ->
        {
            addCategory();
        });
    }
    
    private MenuItem editItem = new MenuItem("Editar");
    private MenuItem deleteItem = new MenuItem("Excluir");
    {
        editItem.setDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance()
                .getActiveUser(), "edit.category"));

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

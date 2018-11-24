/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.views.parts.PermissionDescription;
import com.checkdesk.views.parts.PermissionTree;
import com.checkdesk.views.util.PermissionItem;
import javafx.event.Event;
import javafx.scene.layout.HBox;

/**
 *
 * @author arthu
 */
public class PermissionPane
        extends DefaultPane
{
    public PermissionPane()
    {
        initComponents();
    }
    
    @Override
    protected void resize()
    {
        permissionTree.setPrefWidth(getWidth()/2);
        permissionDescription.setPrefWidth(getWidth()/2);
        permissionTree.setPrefHeight(getHeight());
        permissionDescription.setPrefHeight(getHeight());
    }

    @Override
    public void refreshContent()
    {
        permissionTree.refreshContent();
    }

    private void initComponents()
    {
        hbox.getChildren().addAll(permissionTree, permissionDescription);
        
        getChildren().add(hbox);

        permissionTree.addEventHandler(PermissionTree.Events.ON_SELECT, (Event event) ->
        {
            PermissionItem selected = permissionTree.getSelectedItem();
            permissionDescription.setSource(selected);
        });
    }

    private HBox hbox = new HBox();
    private PermissionTree permissionTree = new PermissionTree();
    private PermissionDescription permissionDescription = new PermissionDescription();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.model.data.Permission;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author arthu
 */
public class PermissionDescription
        extends VBox
{

    public PermissionDescription()
    {
        initComponents();
    }

    public void setSource(Permission permission)
    {
        if(permission != null)
        {
            permissionLabel.setText(permission.toString());
            userTable.setGroup(permission.getViewers());
        }
    }
    
    private void initComponents()
    {
        permissionLabel.getStyleClass().add("details-title");
                
        setVgrow(userTable, Priority.ALWAYS);
        permissionLabel.prefWidthProperty().bind(widthProperty());
        getChildren().addAll(permissionLabel, userTable);
    }
    private GroupTable userTable = new GroupTable();
    private Label permissionLabel = new Label();
}

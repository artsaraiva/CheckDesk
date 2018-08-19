/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.model.data.Permission;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
        testLabel.setText(permission.toString());
    }
    
    private void initComponents()
    {
        setVgrow(userTable, Priority.ALWAYS);
        getChildren().addAll(testLabel, userTable);
    }
    private GroupTable userTable = new GroupTable();
    private Label testLabel = new Label();
}

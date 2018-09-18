/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.PermissionController;
import com.checkdesk.model.data.Group;
import com.checkdesk.model.data.Permission;
import com.checkdesk.views.details.DetailsPane;
import com.checkdesk.views.details.util.DetailsCaption;
import com.checkdesk.views.details.util.DetailsTable;
import com.checkdesk.views.util.PermissionItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author arthu
 */
public class PermissionDescription
        extends DetailsPane<PermissionItem>
{
    private Permission permission;
    
    public PermissionDescription()
    {
        initComponents();
    }

    @Override
    public void setSource(int sourceId) {}

    @Override
    protected void resize()
    {
        vbox.setPrefSize(getWidth(), getHeight());
    }

    @Override
    public void refreshContent() {}
    
    @Override
    public void setSource(PermissionItem item)
    {
        vbox.getChildren().clear();
        vbox.getChildren().addAll(new DetailsCaption(item.toString()),
                                  new DetailsTable().addItem("Permissão padrão", PermissionController.getInstance().getUserTypes(item.getName())));
        
        permission = PermissionController.getInstance().getPermission(item);
        
        viewersTable.setGroup(permission.getViewers());
        vbox.getChildren().add(viewersTable);
    }
    
    private void initComponents()
    {
        viewersTable.setTableDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance().getActiveUser(), "edit.permission"));
        viewersTable.setSaveOnChange(true);
        viewersTable.setShowAll(false);
        
        VBox.setVgrow(viewersTable, Priority.ALWAYS);
        vbox.getChildren().add(viewersTable);
        
        getChildren().add(vbox);
    }
    
    private VBox vbox = new VBox();
    private GroupTable viewersTable = new GroupTable();
}

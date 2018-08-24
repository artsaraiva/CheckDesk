/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.model.data.Permission;
import com.checkdesk.views.parts.PermissionDescription;
import com.checkdesk.views.parts.PermissionTable;
import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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
        permissionList.setPrefWidth(getWidth()/2);
        permissionDescription.setPrefWidth(getWidth()/2);
        permissionList.setPrefHeight(getHeight());
        permissionDescription.setPrefHeight(getHeight());
    }

    @Override
    public void refreshContent()
    {
        int selectedIndex = permissionList.getSelectionModel().getSelectedIndex();
        permissionList.setPermissions(Arrays.asList(
                new Permission(1, "Cadastrar pesquisas"),
                new Permission(1, "Responder pesquisas"),
                new Permission(1, "Editar pesquisas"),
                new Permission(1, "Inspecionar pesquisas"),
                new Permission(1, "Excluir pesquisas"),
                new Permission(1, "Cadastrar usuários"),
                new Permission(1, "Excluir usuários")
        ));
        permissionList.getSelectionModel().select(selectedIndex < 0 ? 0 : selectedIndex);
    }

    private void initComponents()
    {
        hbox.getChildren().addAll(permissionList, permissionDescription);
        
        getChildren().add(hbox);

        permissionList.addEventHandler(PermissionTable.SELECT, (Event event) ->
        {
            Permission selected = permissionList.getSelectedPermission();
            permissionDescription.setSource(selected);
        });
    }

    private HBox hbox = new HBox();
    private PermissionTable permissionList = new PermissionTable("Permissões");
    private PermissionDescription permissionDescription = new PermissionDescription();
}

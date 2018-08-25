/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.model.data.Permission;
import com.checkdesk.views.parts.DefaultTable;
import com.checkdesk.views.parts.PermissionDescription;
import java.util.Arrays;
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
        permissionList.setPrefWidth(getWidth()/2);
        permissionDescription.setPrefWidth(getWidth()/2);
        permissionList.setPrefHeight(getHeight());
        permissionDescription.setPrefHeight(getHeight());
    }

    @Override
    public void refreshContent()
    {
        int selectedIndex = permissionList.getSelectedIndex();
        
        permissionList.setItems(Arrays.asList(
                new Permission(1, "Cadastrar pesquisas"),
                new Permission(1, "Responder pesquisas"),
                new Permission(1, "Editar pesquisas"),
                new Permission(1, "Inspecionar pesquisas"),
                new Permission(1, "Excluir pesquisas"),
                new Permission(1, "Cadastrar usuários"),
                new Permission(1, "Excluir usuários")
        ));
        
        permissionList.setSelectedIndex(selectedIndex < 0 ? 0 : selectedIndex);
    }

    private void initComponents()
    {
        getStylesheets().add(ResourceLocator.getInstance().getStyleResource("registerview.css"));
        permissionList.setShowAddPane(false);
        
        hbox.getChildren().addAll(permissionList, permissionDescription);
        
        getChildren().add(hbox);

        permissionList.addEventHandler(DefaultTable.Events.ON_SELECT, (Event event) ->
        {
            Permission selected = permissionList.getSelectedItem();
            permissionDescription.setSource(selected);
        });
    }

    private HBox hbox = new HBox();
    private DefaultTable<Permission> permissionList = new DefaultTable<Permission>();
    private PermissionDescription permissionDescription = new PermissionDescription();
}

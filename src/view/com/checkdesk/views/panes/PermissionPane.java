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
        extends HBox
{

    public PermissionPane()
    {
        initComponents();
        refreshContent();
    }

    public void refreshContent()
    {
        permissionList.setPermissions(Arrays.asList(
                new Permission(1, "Cadastrar pesquisas"),
                new Permission(1, "Responder pesquisas"),
                new Permission(1, "Editar pesquisas"),
                new Permission(1, "Inspecionar pesquisas"),
                new Permission(1, "Excluir pesquisas"),
                new Permission(1, "Cadastrar usuários"),
                new Permission(1, "Excluir usuários")
        ));
    }

    private void initComponents()
    {
        HBox.setHgrow(permissionList, Priority.ALWAYS);
        HBox.setHgrow(permissionDescription, Priority.ALWAYS);
        getChildren().addAll(permissionList, permissionDescription);

        permissionList.addEventHandler(PermissionTable.SELECT, new EventHandler<Event>()
        {
            @Override
            public void handle(Event event)
            {
                Permission selected = permissionList.getSelectedPermission();
                permissionDescription.setSource(selected);
            }
        });
    }

    private PermissionTable permissionList = new PermissionTable("Permissões");
    private PermissionDescription permissionDescription = new PermissionDescription();
}

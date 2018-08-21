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
        refreshContent();
    }
    
    @Override
    protected void resize()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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
        getChildren().addAll(permissionList, permissionDescription);

        widthProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) ->
        {
            permissionList.setPrefWidth(getWidth()/2);
            permissionDescription.setPrefWidth(getWidth()/2);
        });
        
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.model.data.Permission;
import com.checkdesk.views.panes.MenuPane;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 *
 * @author arthu
 */
public class PermissionTable
        extends ListView<Permission>
{

    public static final EventType SELECT = new EventType("onSelectpPermission");
    private Permission selected;

    public PermissionTable()
    {
        this(null);
    }

    public PermissionTable(String title)
    {
        initComponents();
    }

    public Permission getSelectedPermission()
    {
        return selected != null ? selected : null;
    }

    public void setPermissions(List<Permission> permissions)
    {
        ObservableList<Permission> items = FXCollections.observableArrayList(permissions);
        this.setItems(items);
    }

    private void selectPermission(Permission permission)
    {
        this.selected = permission;
        fireEvent(new Event(SELECT));
    }

    private void initComponents()
    {
        //this.getStyleClass().add("list-view");
        /*setStyle("-fx-background-insets: 0;-fx-padding: 0;");
        setContent(vbox);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);*/

        this.setCellFactory(new Callback<ListView<Permission>, ListCell<Permission>>()
        {
            @Override
            public ListCell<Permission> call(ListView<Permission> param)
            {
                return new ListCell<Permission>()
                {
                    @Override
                    protected void updateItem(Permission item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                        {
                            setText(null);
                        }
                        else
                        {
                            setText(item.toString());
                            getStyleClass().add("permission-list-cell");
                        }

                    }
                };
            }
        });

        this.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Permission>()
        {
            @Override
            public void changed(ObservableValue<? extends Permission> observable, Permission oldValue, Permission newValue)
            {
                selectPermission(getSelectionModel().getSelectedItem());
            }
        });
    }
}

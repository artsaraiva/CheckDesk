/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.model.data.Permission;
import com.checkdesk.model.data.Survey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author arthu
 */
public class PermissionTable
        extends ScrollPane
{
    public static final EventType SELECT = new EventType("onSelectpPermission");

    private String title;
    
    private PermissionTableItem selected;
    private Set<PermissionTable> bindedTables = new HashSet<>();

    public PermissionTable()
    {
        this(null);
    }

    public PermissionTable(String title)
    {
        initComponents();
        
        this.title = title;
        updateTitle();
    }

    public void setTitle(String title)
    {
        this.title = title;
        updateTitle();
    }

    public Permission getSurvey()
    {
        Permission result = null;

        if (selected != null)
        {
            result = selected.getPermission();
        }

        return result;
    }

    public void setPermissions(List<Permission> permissions)
    {
        getVBoxChildren().clear();

        for (Permission p : permissions)
        {
            getVBoxChildren().add(createItem(p));
        }
        
        updateTitle();
    }

    public void bindSelection(PermissionTable table)
    {
        bindedTables.add(table);
    }

    private PermissionTableItem createItem(Permission permission)
    {
        final PermissionTableItem item = new PermissionTableItem(permission);
        item.getStyleClass().add("home-cell");

        item.setOnMouseClicked((MouseEvent event) ->
        {
            setSelected(item);
        });

        return item;
    }

    protected ObservableList<Node> getVBoxChildren()
    {
        return vbox.getChildren();
    }

    public void setSelected(PermissionTableItem selected)
    {
        this.selected = selected;

        List<Node> nodes = new ArrayList<>(getVBoxChildren());
        
        for (PermissionTable table : bindedTables)
        {
            nodes.addAll(table.getVBoxChildren());
        }
        
        for (Node node : nodes)
        {
            node.getStyleClass().remove("home-cell-selected");
        }

        selected.getStyleClass().add("home-cell-selected");

        fireEvent(new Event(SELECT));
    }
    
    private void updateTitle()
    {
        getVBoxChildren().remove(titleLabel);
        
        if (title != null && !title.isEmpty())
        {
            titleLabel.setText(title);
            
            getVBoxChildren().add(0, titleLabel);
        }
    }
    
    private void initComponents()
    {
        titleLabel.getStyleClass().add("home-table-title");
        setStyle("-fx-background-insets: 0;-fx-padding: 0;");
        setContent(vbox);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);
    }
    
    private VBox vbox = new VBox();
    private Label titleLabel = new Label();
    
}

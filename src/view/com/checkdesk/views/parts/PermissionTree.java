/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.PermissionController;
import com.checkdesk.views.util.PermissionItem;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

/**
 *
 * @author MNicaretta
 */
public class PermissionTree
        extends TreeView<PermissionItem>
{
    public static class Events
    {
        public static final EventType ON_SELECT = new EventType("onPermissionSelect");
    }
    
    public PermissionTree()
    {
        initComponents();
    }
    
    public void refreshContent()
    {
        setRoot(PermissionController.getInstance().getRootItem());
    }
    
    public PermissionItem getSelectedItem()
    {
        return getSelectionModel().getSelectedItem().getValue();
    }
    
    private void initComponents()
    {
        getStyleClass().add("permission-table");
        
        getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<PermissionItem>> ov, TreeItem<PermissionItem> t, TreeItem<PermissionItem> t1) ->
        {
            fireEvent(new Event(Events.ON_SELECT));
        });
        
        setCellFactory((TreeView<PermissionItem> table) ->
        {
            TreeCell<PermissionItem> result = new TreeCell<PermissionItem>()
            {
                @Override
                protected void updateItem(PermissionItem item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setTextFill(Paint.valueOf("#000000"));
                    
                    if (item == null || empty)
                    {
                        setText(null);
                        setTextFill(null);
                        setGraphic(null);
                    }

                    else
                    {
                        setText(item.toString());
                    }
                }

                @Override
                public void updateSelected(boolean selected)
                {
                    super.updateSelected(selected);
                    
                    if (selected)
                    {
                        setBackground(new Background(new BackgroundFill(Paint.valueOf("#B0B0B0"),
                                                     CornerRadii.EMPTY,
                                                     Insets.EMPTY)));
                        
                        setTextFill(Paint.valueOf("#FFFFFF"));
                    }
                    
                    else
                    {
                        setBackground(null);
                        setTextFill(Paint.valueOf("#000000"));
                    }
                }
            };
            
            return result;
        });
    }
}

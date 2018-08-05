/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.views.parts.MenuItem;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 *
 * @author arthu
 */
public class MenuPane
        extends VBox
{

    public static class Events
    {
        public static final EventType<Event> EVENT_SELECT = new EventType<Event>("onSelect");
    }
    
    private MenuItem selectedItem;
    
    public MenuPane()
    {
        initComponents();
    }

    private void selectMenuItem(MenuItem menuItem)
    {
        selectedItem = menuItem;
        
        for (MenuItem item : menuItems)
        {
            item.setSelected(item == menuItem);
        }
        
        fireEvent(new Event(Events.EVENT_SELECT));
    }
    
    public Node getSelectedPane()
    {
        return selectedItem != null ? selectedItem.getPane() : null;
    }

    private void initComponents()
    {
        setBackground(new Background(new BackgroundFill(Paint.valueOf("#333645"), CornerRadii.EMPTY, Insets.EMPTY)));

        for (MenuItem menuItem : menuItems)
        {
            menuItem.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    selectMenuItem(menuItem);
                }
            });
        }

        getChildren().addAll(menuItems);
    }

    private MenuItem[] menuItems = new MenuItem[]
    {
        new MenuItem(null, null, "Cadastro"),
        new MenuItem(null, null, "Pesquisas"),
        new MenuItem(null, null, "Análises"),
    };
}

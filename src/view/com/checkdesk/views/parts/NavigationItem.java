/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.views.panes.DefaultPane;
import javafx.scene.control.Label;

/**
 *
 * @author MNicaretta
 */
public class NavigationItem
        extends Label
{
    private DefaultPane pane;
    private String label;
    private NavigationItem previousItem;
    private Object source;

    public NavigationItem(DefaultPane pane, String label)
    {
        this(pane, label, null, null);
    }

    public NavigationItem(DefaultPane pane, String label, Object source)
    {
        this(pane, label, source, null);
    }

    public NavigationItem(DefaultPane pane, String label, NavigationItem previousItem)
    {
        this(pane, label, null, previousItem);
    }

    public NavigationItem(DefaultPane pane, String label, Object source, NavigationItem previousItem)
    {
        this.pane = pane;
        this.label = label.toUpperCase();
        this.previousItem = previousItem;
        this.source = source;
        
        initComponents();
    }

    public DefaultPane getPane()
    {
        return pane;
    }

    public NavigationItem getPreviousItem()
    {
        return previousItem;
    }

    public void setPreviousItem(NavigationItem previousItem)
    {
        this.previousItem = previousItem;
    }

    public Object getSource()
    {
        return source;
    }
    
    private void initComponents()
    {
        getStyleClass().add("navigation-item");
        setText(label);
    }
}

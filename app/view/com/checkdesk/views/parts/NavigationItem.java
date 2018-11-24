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
    private Object context;

    public NavigationItem(DefaultPane pane, String label)
    {
        this(pane, label, null, null);
    }

    public NavigationItem(DefaultPane pane, String label, Object context)
    {
        this(pane, label, context, null);
    }

    public NavigationItem(DefaultPane pane, String label, NavigationItem previousItem)
    {
        this(pane, label, null, previousItem);
    }

    public NavigationItem(DefaultPane pane, String label, Object context, NavigationItem previousItem)
    {
        this.pane = pane;
        this.label = label.toUpperCase();
        this.previousItem = previousItem;
        this.context = context;
        
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

    public Object getContext()
    {
        return context;
    }
    
    private void initComponents()
    {
        getStyleClass().add("navigation-item");
        setText(label);
    }
}

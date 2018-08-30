/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.views.parts.NavigationItem;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author arthu
 */
public class HeaderPane
        extends HBox
{
    public HeaderPane()
    {
        initComponents();
    }

    public ObservableValue getUserPaneWidth()
    {
        return userPane.widthProperty();
    }
    
    public void refreshContent()
    {
        userPane.refreshContent();
    }
    
    public void setNavigationItem(NavigationItem item)
    {
        navigationPane.setItem(item);
    }
    
    public NavigationItem getNavigationItem()
    {
        return navigationPane.getItem();
    }

    private void initComponents()
    {
        setHgrow(navigationPane, Priority.ALWAYS);
        getChildren().addAll(userPane, navigationPane);
        
        navigationPane.addEventHandler(NavigationPane.Events.ON_SELECT, (Event event) ->
        {
            fireEvent(event);
        });
    }

    private UserPane userPane = new UserPane();
    private NavigationPane navigationPane = new NavigationPane();
}

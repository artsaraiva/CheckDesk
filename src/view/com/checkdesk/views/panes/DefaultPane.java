/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.views.parts.NavigationItem;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.scene.layout.Pane;

/**
 *
 * @author arthu
 */
public abstract class DefaultPane
        extends Pane
{
    public static class Events
    {
        public static final EventType ON_CHANGE = new EventType("onCallChange");
    }
    
    public DefaultPane()
    {
        initComponets();
    }
    
    protected abstract void resize();
    public abstract void refreshContent();
    
    public NavigationItem createNavigationItem(NavigationItem currentItem)
    {
        return null;
    }
    
    public void setContext(Object context)
    {
    }
    
    private void initComponets()
    {
        getStyleClass().add("default-pane");
        
        widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            resize();
        });
        
        heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            resize();
        });
    }
}

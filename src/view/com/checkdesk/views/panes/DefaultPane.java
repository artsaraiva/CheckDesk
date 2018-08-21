/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

/**
 *
 * @author arthu
 */
public abstract class DefaultPane
        extends Pane
{
    public DefaultPane()
    {
        initComponets();
    }
    
    protected abstract void resize();
    public abstract void refreshContent();
    
    private void initComponets()
    {
        getStylesheets().add("default-pane");
        
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.details;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.panes.DefaultPane;

/**
 *
 * @author MNicaretta
 * @param <T>
 */
public abstract class DetailsPane<T>
        extends DefaultPane
{
    public DetailsPane()
    {
        getStylesheets().add(ResourceLocator.getInstance().getStyleResource("details.css"));
    }
    
    protected T source;
    
    public abstract void setSource(int sourceId);
    
    public void setSource(T source)
    {
        this.source = source;
        
        refreshContent();
    }
}

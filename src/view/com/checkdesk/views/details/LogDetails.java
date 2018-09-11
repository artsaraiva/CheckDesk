/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.details;

import com.checkdesk.control.util.LogUtilities;
import com.checkdesk.model.data.Log;
import com.checkdesk.views.details.util.DetailsCaption;
import com.checkdesk.views.details.util.DetailsTable;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author MNicaretta
 */
public class LogDetails
        extends DetailsPane<Log>
{
    public LogDetails()
    {
        initComponents();
    }
    
    @Override
    public void setSource(int sourceId)
    {
        setSource(LogUtilities.getLog(sourceId));
    }

    @Override
    protected void resize()
    {
        vbox.setPrefSize(getWidth(), getHeight());
        
        for (Node node : vbox.getChildren())
        {
            if (node instanceof Region)
            {
                ((Region) node).setMaxWidth(getWidth());
            }
        }
    }

    @Override
    public void refreshContent()
    {
        vbox.getChildren().clear();
        
        if (source != null)
        {
            vbox.getChildren().addAll(
                new DetailsCaption(source.toString()),
                new DetailsTable(75).addItem("Ação", LogUtilities.getEvent(source.getEvent()))
                                    .addItem("Data", source.getTimestamp())
                                    .addItem("Usuário", source.getUser())
                                    .addItem("Objeto", source.getObjectClass() + " - " + source.getObjectName())
                                    .addItemHtml("Mudanças", source.getCommand()));
        }
    }
    
    private void initComponents()
    {
        getChildren().add(vbox);
    }
    
    private VBox vbox = new VBox();
}

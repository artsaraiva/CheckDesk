/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.details.util;

import com.checkdesk.control.ResourceLocator;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author MNicaretta
 */
public class DetailsCaption
        extends HBox
{
    private String title;
    private String icon;

    public DetailsCaption(String title)
    {
        this(title, null);
    }

    public DetailsCaption(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
        
        initComponents();
    }
    
    private void initComponents()
    {
        getStyleClass().add("details-title");
        titleLabel.setText(title);
        
        setHgrow(this, Priority.ALWAYS);
        setHgrow(titleLabel, Priority.ALWAYS);
        
        getChildren().addAll(titleLabel);
        
        if (icon != null)
        {
            iconView.setImage(new Image(ResourceLocator.getInstance().getImageResource(icon)));
            getChildren().add(iconView);
        }
    }
    
    private Label titleLabel = new Label();
    private ImageView iconView = new ImageView();
}

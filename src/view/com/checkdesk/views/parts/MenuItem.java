/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ResourceLocator;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

/**
 *
 * @author arthu
 */
public class MenuItem
        extends HBox
{

    private Node pane;
    private String icon;
    private String name;

    public MenuItem(Node pane, String icon, String name)
    {
        this.pane = pane;
        this.icon = icon;
        this.name = name;

        initComponents();
    }

    public Node getPane()
    {
        return pane;
    }

    public void setSelected(boolean selected)
    {
        if (selected)
        {
            setBackground(new Background(new BackgroundFill(Paint.valueOf("#20202c"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else
        {
            setBackground(null);
        }
    }

    private void initComponents()
    {
        iconView.setFitHeight(20);
        iconView.setFitWidth(20);
        iconView.setImage(new Image(ResourceLocator.getInstance().getImageResource(icon)));

        label.setText(name);
        label.setTextFill(Paint.valueOf("#ffffff"));

        setSpacing(15);
        getStyleClass().add("menu-item");
        getChildren().addAll(iconView, label);

    }

    private ImageView iconView = new ImageView();
    private Label label = new Label();

}

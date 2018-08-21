/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.panes.DefaultPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;

/**
 *
 * @author arthu
 */
public class MenuItem
        extends HBox
{
    private DefaultPane pane;
    private String icon;
    private String name;

    public MenuItem( DefaultPane pane, String icon, String name )
    {
        this.pane = pane;
        this.icon = icon;
        this.name = name;

        initComponents();
    }

    public DefaultPane getPane()
    {
        return pane;
    }

    public void setSelected( boolean selected )
    {
        if ( selected )
        {
            setBackground( new Background( new BackgroundFill( Paint.valueOf( "#20202c" ), CornerRadii.EMPTY, Insets.EMPTY ) ) );
            iconView.setImage( new Image( ResourceLocator.getInstance().getImageResource( icon + "_s" ) ) );
            label.getStyleClass().add("menu-label-selected");
        }
        else
        {
            setBackground( null );
            iconView.setImage( new Image( ResourceLocator.getInstance().getImageResource( icon ) ) );
            label.getStyleClass().remove("menu-label-selected");
        }
    }

    private void initComponents()
    {
        iconView.setFitHeight( 25 );
        iconView.setFitWidth( 25 );

        label.setText( name );
        label.getStyleClass().add( "menu-label" );

        HBox.setHgrow( label, Priority.ALWAYS );

        setSpacing( 20 );
        setAlignment( Pos.CENTER_LEFT );
        getStyleClass().add( "menu-item" );
        getChildren().addAll( iconView, label );

    }

    private ImageView iconView = new ImageView();
    private Label label = new Label();

}

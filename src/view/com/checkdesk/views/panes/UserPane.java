/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ResourceLocator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author arthu
 */
public class UserPane
        extends HBox
{

    public UserPane()
    {
        initComponents();
    }

    private void initComponents()
    {
        VBox vbox = new VBox();

        perfilUser.setText("Administrador");
        nameUser.setText("Arthur");
        perfilUser.getStyleClass().add("header-user-perfil");
        nameUser.getStyleClass().add("header-user-name");
        iconUser.setImage(new Image(ResourceLocator.getInstance().getImageResource("test_user")));
        iconUser.setClip(new Circle(38, 38, 36, Paint.valueOf("#425FA4")));
        
        vbox.getChildren().addAll(perfilUser, nameUser);
        vbox.setAlignment(Pos.CENTER_LEFT);
        
        setBackground(new Background(new BackgroundFill(Paint.valueOf("#9834CA"), CornerRadii.EMPTY, Insets.EMPTY)));
        setSpacing(20);
        getStyleClass().add("menu-item");
        setMinWidth( 250 );
        
        //javafx.scene.text.Font.getFamilies();
        
        getChildren().addAll(iconUser, vbox);
    }

    private Label perfilUser = new Label();
    private Label nameUser = new Label();
    private ImageView iconUser = new ImageView();
}

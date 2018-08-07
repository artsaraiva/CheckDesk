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
public class HeaderPane
        extends HBox
{

    public HeaderPane()
    {
        initComponents();
    }

    private void initComponents()
    {
       getChildren().addAll( userPane );
    }

    VBox vbox = new VBox();
    UserPane userPane = new UserPane();
}

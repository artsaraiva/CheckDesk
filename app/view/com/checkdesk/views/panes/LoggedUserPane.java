/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.User;
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
import javafx.scene.text.Text;

/**
 *
 * @author arthu
 */
public class LoggedUserPane
        extends HBox
{
    public LoggedUserPane()
    {
        initComponents();
    }

    public void refreshContent()
    {
        User activeUser = ApplicationController.getInstance().getActiveUser();

        perfilUser.setText(UserUtilities.getType(activeUser.getType()).getLabel());
        nameUser.setText(activeUser.getName());
        iconUser.setImage(new Image(ResourceLocator.getInstance().getImageResource("test_user")));

        vbox.setMinWidth(iconUser.getFitWidth() +
                         Math.max(new Text(perfilUser.getText()).getLayoutBounds().getWidth(),
                                  new Text(nameUser.getText()).getLayoutBounds().getWidth()));
    }

    private void initComponents()
    {
        perfilUser.getStyleClass().add("user-perfil");
        nameUser.getStyleClass().add("user-name");
        iconUser.setClip(new Circle(40, 40, 40, Paint.valueOf("#425FA4")));
        iconUser.setFitWidth(80);
        iconUser.setFitHeight(80);

        vbox.getChildren().addAll(perfilUser, nameUser);
        vbox.setAlignment(Pos.CENTER_LEFT);

        setBackground(new Background(new BackgroundFill(Paint.valueOf("#9834CA"), CornerRadii.EMPTY, Insets.EMPTY)));
        setSpacing(20);
        getStyleClass().add("menu-item");

        getChildren().addAll(iconUser, vbox);
    }

    private VBox vbox = new VBox();
    private Label perfilUser = new Label();
    private Label nameUser = new Label();
    private ImageView iconUser = new ImageView();
}

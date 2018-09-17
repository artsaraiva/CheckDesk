/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.panes.DefaultPane;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author arthu
 */
public class BrowseButton
        extends VBox
{

    private DefaultPane pane;
    private String title;
    private String icon;
    private String role;

    public BrowseButton(DefaultPane pane, String title, String icon, String role)
    {
        this.pane = pane;
        this.title = title;
        this.icon = icon;
        this.role = role;

        initComponents();
    }

    public DefaultPane getPane()
    {
        return pane;
    }

    public String getTitle()
    {
        return title;
    }

    public String getRole()
    {
        return role;
    }

    private void resize()
    {
        double imageSize = getWidth() * 0.6;
        double labelSize = getWidth() * 0.15;
        double padding = getWidth() * 0.05;

        image.setFitHeight(imageSize);
        image.setFitWidth(imageSize);

        label.setFont(new Font(labelSize));

        setPadding(new Insets(padding));
    }

    private void initComponents()
    {
        setCursor(Cursor.HAND);
        label.setText(title);
        image.setImage(new Image(ResourceLocator.getInstance().getImageResource(icon)));
        setAlignment(Pos.CENTER);
        getChildren().addAll(image, label);

        widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            resize();
        });

        heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            resize();
        });
    }

    private ImageView image = new ImageView();
    private Label label = new Label();
}

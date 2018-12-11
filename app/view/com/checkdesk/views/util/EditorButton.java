/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.util;

import com.checkdesk.control.ResourceLocator;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 *
 * @author MNicaretta
 */
public class EditorButton
        extends HBox
{
    private ButtonType type;

    public EditorButton(ButtonType type)
    {
        this.type = type;

        initComponents();
    }

    public ButtonType getType()
    {
        return type;
    }

    private String getIcon()
    {
        String result = "";

        if (type.getButtonData() == ButtonBar.ButtonData.OK_DONE)
        {
            result = "ei_save.png";
        }

        if (type.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE)
        {
            result = "ei_cancel.png";
        }

        if (type.getButtonData() == ButtonBar.ButtonData.NEXT_FORWARD)
        {
            result = "ei_next.png";
        }

        if (type.getButtonData() == ButtonBar.ButtonData.BACK_PREVIOUS)
        {
            result = "ei_previous.png";
        }

        return ResourceLocator.getInstance().getImageResource(result);
    }

    private void initComponents()
    {
        getStyleClass().add("editor-button");
        
        label.setText(type.getText());
        icon.setImage(new Image(getIcon()));
        icon.setFitHeight(20);
        icon.setFitWidth(20);

        setCursor(Cursor.HAND);
        
        setSpacing(5);
        setAlignment(Pos.CENTER);

        getChildren().add(label);
        getChildren().add(type.getButtonData() == ButtonBar.ButtonData.BACK_PREVIOUS ? 0 : 1, icon);
        
        widthProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) ->
        {
            label.setPrefWidth(getWidth() - 20);
        });
    }

    private ImageView icon = new ImageView();
    private Label label = new Label();
}

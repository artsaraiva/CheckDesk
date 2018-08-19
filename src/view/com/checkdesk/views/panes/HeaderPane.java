/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    public ObservableValue getUserPaneWidth()
    {
        return userPane.widthProperty();
    }

    private void initComponents()
    {
        getChildren().addAll(userPane);
    }

    private VBox vbox = new VBox();
    private UserPane userPane = new UserPane();
}

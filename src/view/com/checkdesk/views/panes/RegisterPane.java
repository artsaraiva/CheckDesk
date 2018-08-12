/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author MNicaretta
 */
public class RegisterPane
        extends BorderPane
{
    public RegisterPane()
    {
        initComponents();
    }
    
    private void initComponents()
    {
        HBox.setHgrow(surveyButton, Priority.ALWAYS);
        VBox.setVgrow(surveyButton, Priority.ALWAYS);
        surveyButton.autosize();
        setRight(surveyButton);
    }
    
    private Button surveyButton = new Button("Pesquisa");
}

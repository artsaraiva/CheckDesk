/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.util.SurveyUtilities;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author MNicaretta
 */
public class RegisterPane
        extends HBox
{
    public RegisterPane()
    {
        initComponents();
    }

    private void initComponents()
    {
        getChildren().addAll(userButton, surveyButton);

        surveyButton.prefHeightProperty().bind(heightProperty());
        userButton.prefHeightProperty().bind(heightProperty());
        surveyButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent t)
            {
                SurveyUtilities.addSurvey();
            }
        });

        widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1)
            {
                surveyButton.setPrefWidth(t1.doubleValue() / 2);
                userButton.setPrefWidth(t1.doubleValue() / 2);
            }
        });
    }

    private Button userButton = new Button("Usuario");
    private Button surveyButton = new Button("Pesquisa");
}

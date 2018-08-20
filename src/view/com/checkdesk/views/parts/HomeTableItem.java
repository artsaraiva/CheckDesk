/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.model.data.Survey;
import java.text.DateFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author arthu
 */
public class HomeTableItem
        extends GridPane
{
    private Survey survey;

    public HomeTableItem(Survey survey)
    {
        setSource(survey);
        initComponents();
    }

    private void setSource(Survey survey)
    {
        this.survey = survey;

        iconUser.setImage(new Image(ResourceLocator.getInstance().getImageResource(survey.getOwner().getLogin())));
        surveyLabel.setText(survey.getTitle());

        DateFormat df = DateFormat.getDateInstance();
        dueLabel.setText(df.format(survey.getCreatedDate()));
        
        double randomValue = Math.random();
        progressIndicator.setProgress(randomValue);
        tooltip.setText(String.format("%.2f%%", randomValue * 100));
    }
    
    public Survey getSurvey()
    {
        return survey;
    }

    private void initComponents()
    {
        iconUser.setClip(new Circle(38, 38, 36, Paint.valueOf("#FFFFFF")));
       // iconUser.getStyleClass().add("home-table-survey");
        surveyLabel.getStyleClass().add("home-table-survey-label");
        //surveyLabel.setPrefWidth(250);
        dueLabel.getStyleClass().add("home-table-item");
        progressIndicator.getStyleClass().add("answer-progress-graph");
        tooltip.getStyleClass().add("tooltip-progress-graph");

        progressIndicator.setTooltip(tooltip);
        addRow(0, iconUser, surveyLabel, dueLabel, progressIndicator);

        ColumnConstraints c1 = new ColumnConstraints();
        ColumnConstraints c2 = new ColumnConstraints();
        ColumnConstraints c3 = new ColumnConstraints();
        ColumnConstraints c4 = new ColumnConstraints();

        c1.setMinWidth(90);
        c2.setHgrow(Priority.ALWAYS);
        c3.setMinWidth(110);
        c4.setMinWidth(90);
        c4.setHalignment(HPos.RIGHT);
        
        getColumnConstraints().addAll(c1, c2, c3, c4);
        setMinWidth(450);
    }

    private ImageView iconUser = new ImageView();
    private Label surveyLabel = new Label();
    private Label dueLabel = new Label();
    private ProgressIndicator progressIndicator = new ProgressIndicator();
    private Tooltip tooltip = new Tooltip();
}

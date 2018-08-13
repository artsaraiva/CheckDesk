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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author arthu
 */
public class HomeTableItem
        extends HBox
{

    private Survey survey;

    public HomeTableItem(Survey survey)
    {
        setSource(survey);
        initComponents();
    }

    @Override
    public void setWidth(double value)
    {
        super.setWidth(value);
    }

    private void setSource(Survey survey)
    {
        this.survey = survey;

        iconUser.setImage(new Image(ResourceLocator.getInstance().getImageResource(survey.getOwner().getLogin())));
        surveyLabel.setText(survey.getTitle());

        DateFormat df = DateFormat.getDateInstance();
        dueLabel.setText(df.format(survey.getCreatedDate()));

    }

    private void initComponents()
    {
        iconUser.setClip(new Circle(38, 38, 36, Paint.valueOf("#FFFFFF")));

        surveyLabel.getStyleClass().add("home-table-survey");
        dueLabel.getStyleClass().add("home-table-item");

        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setHgrow(surveyLabel, Priority.ALWAYS);
        /*ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("", 25),
                        new PieChart.Data("", 75));
        final PieChart chart = new PieChart(pieChartData);
        chart.setLegendVisible(false);
        chart.setLabelsVisible(false);
        chart.setPrefSize(75, 75);*/
        ProgressIndicator pi = new ProgressIndicator(Math.random() * 1);
        getChildren().addAll(iconUser, surveyLabel, dueLabel, pi);

        widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                //surveyLabel.setMinWidth((newValue.intValue()) - 80 - 100);
            }
        });
    }

    private ImageView iconUser = new ImageView();
    private Label surveyLabel = new Label();
    private Label dueLabel = new Label();
    //GRÁFICO
}

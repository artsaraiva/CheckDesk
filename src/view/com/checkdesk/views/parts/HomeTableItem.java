/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.control.data.Survey;
import java.text.DateFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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
        initComponents();
        
        setSource(survey);
    }
    
    @Override
    public void setWidth(double value)
    {
        super.setWidth(value);
    }
    
    private void setSource(Survey survey)
    {
        this.survey = survey;
        
        iconUser.setImage(new Image(ResourceLocator.getInstance().getImageResource("test_user")));
        surveyLabel.setText(survey.getTitle());
        
        DateFormat df = DateFormat.getDateInstance();
        dueLabel.setText(df.format(survey.getCreatedDate()));
    }
    
    private void initComponents()
    {
        iconUser.setClip(new Circle(38, 38, 36, Paint.valueOf("#FFFFFF")));
//        iconUser.setFitHeight( 64 );
        
        surveyLabel.getStyleClass().add("home-table-survey");
        dueLabel.getStyleClass().add("home-table-item");
        
        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(iconUser, surveyLabel, dueLabel);
        
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

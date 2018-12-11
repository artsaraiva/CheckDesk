/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.control.util.AnswerUtilities;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.model.data.Answer;
import com.checkdesk.model.util.AnswerWrapper;
import com.checkdesk.views.parts.HomeTable;
import com.checkdesk.views.util.Callback;
import java.sql.Timestamp;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author arthu
 */
public class HomePane
        extends DefaultPane
{       
    public HomePane()
    {
        initComponents();
    }

    @Override
    protected void resize()
    {
        if (getChildren().get(0) == hbox)
        {
            pendingList.setPrefWidth(getWidth() / 2);
            createdList.setPrefWidth(getWidth() / 2);
            pendingList.setPrefHeight(getHeight());
            createdList.setPrefHeight(getHeight());
        }
        
        else
        {
            ((DefaultPane) getChildren().get(0)).setPrefSize(getWidth(), getHeight());
        }
    }

    @Override
    public void refreshContent()
    {
        getChildren().clear();
        getChildren().add(hbox);
        
        pendingList.setAnswers(AnswerUtilities.getPendingAnswers());
        createdList.setSurveys(SurveyUtilities.getOwnedSurvey());
    }

    private void editAnswer()
    {
        if (pendingList.getSelectedSource() != null && pendingList.getSelectedSource() instanceof Answer)
        {
            Answer answer = (Answer) pendingList.getSelectedSource();
            AnswerPane pane = new AnswerPane(new Callback<AnswerWrapper>(new AnswerWrapper(answer))
            {
                @Override
                public void handle(Event t)
                {
                    refreshContent();
                }
            } );

            pane.setPrefSize(getWidth(), getHeight());
            getChildren().clear();
            getChildren().add(pane);
            pane.refreshContent();
            pane.resize();
        }

    }

    private void initComponents()
    {
        getStylesheets().add(ResourceLocator.getInstance().getStyleResource("homeview.css"));
        pendingList.bindSelection(createdList);
        createdList.bindSelection(pendingList);

        HBox.setHgrow(pendingList, Priority.ALWAYS);
        HBox.setHgrow(createdList, Priority.ALWAYS);
        hbox.getChildren().addAll(pendingList, createdList);
        getChildren().add(hbox);
        
        pendingList.addEventHandler(HomeTable.EDIT, (Event event) ->
        {
            editAnswer();
        });
    }

    private HBox hbox = new HBox();
    private HomeTable pendingList = new HomeTable("Pesquisas pendentes");
    private HomeTable createdList = new HomeTable("Pesquisas criadas");
}

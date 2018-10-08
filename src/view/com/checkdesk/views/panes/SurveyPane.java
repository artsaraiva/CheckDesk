/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.PermissionController;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.model.data.Survey;
import com.checkdesk.views.details.SurveyDetails;
import com.checkdesk.views.parts.DefaultTable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author MNicaretta
 */
public class SurveyPane
        extends DefaultPane
{
    private List<Survey> items = new ArrayList<>();
    
    public SurveyPane()
    {
        initComponents();
    }
    
    @Override
    protected void resize()
    {
        selectionBox.setPrefWidth(getWidth() / 3);
        detailsPane.setPrefWidth(getWidth() * 2 / 3);
        selectionBox.setPrefHeight(getHeight());
        detailsPane.setPrefHeight(getHeight());
    }

    @Override
    public void refreshContent()
    {
        surveyTable.setItems(items = SurveyUtilities.getValues());
        detailsPane.refreshContent();
    }
    
    private void initComponents()
    {
        filterField.setPromptText("Digite para filtrar");

        filterBox.setAlignment(Pos.CENTER);
        filterBox.setSpacing(15);
        filterBox.getChildren().addAll(filterLabel, filterField);

        HBox.setHgrow(filterField, Priority.ALWAYS);
        VBox.setVgrow(surveyTable, Priority.ALWAYS);

        surveyTable.setActions(new MenuItem[]
        {
            editItem, deleteItem
        });
        selectionBox.getChildren().addAll(filterBox, surveyTable);

        selectionBox.setMinWidth(450);

        hbox.getChildren().addAll(selectionBox, detailsPane);
        getChildren().add(hbox);

        surveyTable.addEventHandler(DefaultTable.Events.ON_ADD, (Event t) ->
        {
            SurveyUtilities.addSurvey();
            refreshContent();
        });

        surveyTable.addEventHandler(DefaultTable.Events.ON_SELECT, (Event t) ->
        {
            detailsPane.setSource(surveyTable.getSelectedItem());
        });

        filterField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
        {
            ObservableList<Survey> filterList = FXCollections.observableArrayList();

            for (Survey item : items)
            {
                String filter = filterField.getText().toLowerCase().trim();

                if (item.toString().toLowerCase().contains(filter))
                {
                    filterList.add(item);
                }
            }

            surveyTable.setItems(filterList);
        });

        surveyTable.setAddButtonDisabled(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance()
                .getActiveUser(), "add.survey"));
    }
    
    private HBox hbox = new HBox();
    
    private VBox selectionBox = new VBox();
    private HBox filterBox = new HBox();
    private Label filterLabel = new Label("Filtro: ");
    private TextField filterField = new TextField();
    
    private DefaultTable<Survey> surveyTable = new DefaultTable<>();
    private SurveyDetails detailsPane = new SurveyDetails();
    
    private MenuItem editItem = new MenuItem("Editar");
    private MenuItem deleteItem = new MenuItem("Excluir");
    {
        editItem.setDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance()
                .getActiveUser(), "edit.survey"));

        deleteItem.setDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance()
                .getActiveUser(), "delete.survey"));

        editItem.setOnAction((ActionEvent t) ->
        {
            SurveyUtilities.editSurvey(surveyTable.getSelectedItem());
            refreshContent();
        });

        deleteItem.setOnAction((ActionEvent t) ->
        {
            SurveyUtilities.deleteSurvey(surveyTable.getSelectedItem());
            refreshContent();
        });
    }
}

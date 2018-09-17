/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.PermissionController;
import com.checkdesk.control.util.Item;
import com.checkdesk.control.util.LogUtilities;
import com.checkdesk.model.data.Log;
import com.checkdesk.views.details.LogDetails;
import com.checkdesk.views.parts.DefaultTable;
import com.checkdesk.views.parts.ItemSelector;
import com.checkdesk.views.util.Prompts;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author MNicaretta
 */
public class LogPane
        extends DefaultPane
{

    public LogPane()
    {
        initComponents();
    }

    @Override
    protected void resize()
    {
        selectionBox.setPrefWidth(getWidth() / 4);
        detailsBox.setMinWidth(getWidth() * 3 / 4);
        selectionBox.setPrefHeight(getHeight());
        detailsBox.setPrefHeight(getHeight());
    }

    @Override
    public void refreshContent()
    {
        table.setItems(LogUtilities.getLogs(filterOption.getValue(), filterSelector.getSelected()));
        detailsPane.refreshContent();
    }

    private void initComponents()
    {
        table.setShowAddPane(false);
        checkbox.setSelected(ApplicationController.isActiveLog());

        filterOption.setMinWidth(100);
        HBox.setHgrow(filterSelector, Priority.ALWAYS);

        VBox.setVgrow(table, Priority.ALWAYS);

        filterBox.getChildren().addAll(filterOption, filterSelector);
        selectionBox.getChildren().addAll(filterBox, table);

        detailsBox.getChildren().addAll(checkbox, detailsPane);

        VBox.setVgrow(detailsPane, Priority.ALWAYS);

        hbox.getChildren().addAll(selectionBox, detailsBox);
        getChildren().add(hbox);

        filterOption.valueProperty().addListener((ObservableValue<? extends Item> ov, Item t, Item t1) ->
        {
            filterSelector.setItems(LogUtilities.filtersFor(t1));
            table.setItems(LogUtilities.getLogs(filterOption.getValue(), filterSelector.getSelected()));
        });

        filterSelector.addEventHandler(ItemSelector.Events.ON_SELECT, (Event t) ->
        {
            table.setItems(LogUtilities.getLogs(filterOption.getValue(), filterSelector.getSelected()));
        });

        table.addEventHandler(DefaultTable.Events.ON_SELECT, (Event t) ->
        {
            detailsPane.setSource(table.getSelectedItem());
        });

        checkbox.setDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance().getActiveUser(), "edit.log"));
        checkbox.setOnAction((ActionEvent t) ->
        {
            checkbox.setSelected(!checkbox.isSelected());
            String title = "Deseja realmente " + (checkbox.isSelected() ? "desativar" : "ativar") + " a aditoria?";
            String message = checkbox.isSelected()
                    ? "Ao desativar a auditoria o sistema não registrará mais as ações dos usuários"
                    : "Ao ativar a auditoria o sistema passa a registrar todas as ações dos usuários";

            if (Prompts.confirm(title, message))
            {
                checkbox.setSelected(!checkbox.isSelected());
                ApplicationController.setActiveLog(checkbox.isSelected());
                refreshContent();
            }
        });
    }

    private HBox hbox = new HBox();

    private VBox selectionBox = new VBox();
    private HBox filterBox = new HBox();
    private ComboBox<Item> filterOption = new ComboBox<>(LogUtilities.getFilterOptions());
    private ItemSelector filterSelector = new ItemSelector();

    private DefaultTable<Log> table = new DefaultTable<>();

    private VBox detailsBox = new VBox();
    private CheckBox checkbox = new CheckBox("Ativar/Desativar auditoria");
    private LogDetails detailsPane = new LogDetails();
}

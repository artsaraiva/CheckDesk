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
import com.checkdesk.views.parts.DatePicker;
import com.checkdesk.views.parts.DefaultTable;
import com.checkdesk.views.parts.NavigationItem;
import com.checkdesk.views.parts.Prompts;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
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
    private Object context;
    
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
        if (context != null)
        {
            table.clearSelection();
        }
        
        table.setItems(LogUtilities.getLogs(fromDate.getDate(), untilDate.getDate()));
        transferLogsButton.setDisable(fromDate.getDate() == null || untilDate.getDate() == null);
        detailsPane.refreshContent();
    }

    @Override
    public void setContext(Object context)
    {
        if (context instanceof Log)
        {
            this.context = context;
            detailsPane.setSource((Log) context);
        }
    }

    @Override
    public NavigationItem createNavigationItem(NavigationItem currentItem)
    {
        Log log = table.getSelectedItem();
        
        if (log != null)
        {
            return new NavigationItem(new LogPane(), log.toString(), log, currentItem);
        }
        
        return super.createNavigationItem(currentItem);
    }
    
    private void initComponents()
    {
        transferLogsButton.setText("Transferir registros");
        table.setShowAddPane(false);
        checkbox.setSelected(ApplicationController.isActiveLog());

        VBox.setVgrow(table, Priority.ALWAYS);

        filterBox.getChildren().addAll(fromDate, untilDate);
        selectionBox.getChildren().addAll(filterBox, table);

        detailsBox.getChildren().addAll(checkbox, transferLogsButton, detailsPane);

        VBox.setVgrow(detailsPane, Priority.ALWAYS);

        hbox.getChildren().addAll(selectionBox, detailsBox);
        getChildren().add(hbox);

        fromDate.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            refreshContent();
        });

        untilDate.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            refreshContent();
        });

        table.addEventHandler(DefaultTable.Events.ON_SELECT, (Event t) ->
        {
            fireEvent(new Event(Events.ON_CHANGE));
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

        transferLogsButton.setOnAction((ActionEvent t) ->
        {
            TextInputDialog dialog = new TextInputDialog("logs2");

            dialog.setTitle("Transferência de registros de auditoria");
            dialog.setHeaderText("Informe o nome da tabela destino:");
            dialog.setContentText("Tabela:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name ->
            {
               LogUtilities.transferLogs(name, fromDate.getDate(), untilDate.getDate());
               refreshContent();
            });
        });

    }

    private HBox hbox = new HBox();

    private VBox selectionBox = new VBox();
    private HBox filterBox = new HBox();
    private ComboBox<Item> filterOption = new ComboBox<>(LogUtilities.getFilterOptions());
    private DatePicker fromDate = new DatePicker();
    private DatePicker untilDate = new DatePicker();

    private DefaultTable<Log> table = new DefaultTable<>();

    private VBox detailsBox = new VBox();
    private CheckBox checkbox = new CheckBox("Ativar/Desativar auditoria");
    private Button transferLogsButton = new Button();
    private LogDetails detailsPane = new LogDetails();
}

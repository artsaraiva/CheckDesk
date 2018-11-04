/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.PermissionController;
import com.checkdesk.control.util.AttachmentUtilities;
import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.model.data.Attachment;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Question;
import com.checkdesk.views.details.FormDetails;
import com.checkdesk.views.parts.DefaultTable;
import com.checkdesk.views.pickers.ItemPicker;
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
public class FormPane
        extends DefaultPane
{
    private List<Form> items = new ArrayList<>();

    public FormPane()
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
        table.setItems(items = FormUtilities.getForms());
        detailsPane.refreshContent();
    }

    private void initComponents()
    {
        filterField.setPromptText("Digite para filtrar");

        filterBox.setAlignment(Pos.CENTER);
        filterBox.setSpacing(15);
        filterBox.getChildren().addAll(filterLabel, filterField);

        HBox.setHgrow(filterField, Priority.ALWAYS);
        VBox.setVgrow(table, Priority.ALWAYS);

        table.setActions(new MenuItem[]
        {
            editItem, deleteItem, attachmentItem
        });
        selectionBox.getChildren().addAll(filterBox, table);

        selectionBox.setMinWidth(450);

        hbox.getChildren().addAll(selectionBox, detailsPane);
        getChildren().add(hbox);

        table.addEventHandler(DefaultTable.Events.ON_ADD, (Event t) ->
        {
            FormUtilities.addForm();
            refreshContent();
        });

        table.addEventHandler(DefaultTable.Events.ON_SELECT, (Event t) ->
        {
            detailsPane.setSource(table.getSelectedItem());
        });

        filterField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
        {
            ObservableList<Form> filterList = FXCollections.observableArrayList();

            for (Form item : items)
            {
                String filter = filterField.getText().toLowerCase().trim();

                if (item.toString().toLowerCase().contains(filter))
                {
                    filterList.add(item);
                }
            }

            table.setItems(filterList);
        });

        table.setAddButtonDisabled(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance()
                .getActiveUser(), "add.form"));
    }

    private HBox hbox = new HBox();

    private VBox selectionBox = new VBox();
    private HBox filterBox = new HBox();
    private Label filterLabel = new Label("Filtro: ");
    private TextField filterField = new TextField();

    private DefaultTable<Form> table = new DefaultTable<>();

    private FormDetails detailsPane = new FormDetails();

    private MenuItem editItem = new MenuItem("Editar");
    private MenuItem deleteItem = new MenuItem("Excluir");
    private MenuItem attachmentItem = new MenuItem("Anexos");
    {
        editItem.setDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance().getActiveUser(), "edit.form"));

        deleteItem.setDisable(!PermissionController.getInstance().hasPermission(ApplicationController.getInstance().getActiveUser(), "delete.form"));

        editItem.setOnAction((ActionEvent t) ->
        {
            FormUtilities.editForm(table.getSelectedItem());
            refreshContent();
        });

        deleteItem.setOnAction((ActionEvent t) ->
        {
            FormUtilities.deleteForm(table.getSelectedItem());
            refreshContent();
        });
        
        attachmentItem.setOnAction((ActionEvent t) ->
        {
            downloadAttachment();
        });
    }
    
    private void downloadAttachment()
    {
        try
        {
            ItemPicker<Question> questionPicker = new ItemPicker<>();
            questionPicker.setItems(FormUtilities.getQuestions(table.getSelectedItem()));
            questionPicker.open("Selecione um item");

            if (questionPicker.getSelected() != null)
            {
                ItemPicker<Attachment> attachmentPicker = new ItemPicker<>();
                attachmentPicker.setItems(AttachmentUtilities.getAttachments(questionPicker.getSelected()));
                attachmentPicker.open("Selecione um item");

                if (attachmentPicker.getSelected() != null)
                {
                    ApplicationController.getInstance().downloadFile(attachmentPicker.getSelected());
                }
            }
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
}

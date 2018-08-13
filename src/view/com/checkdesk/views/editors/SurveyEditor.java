/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.control.util.Item;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.model.data.Category;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.data.User;
import com.checkdesk.views.parts.DatePicker;
import com.checkdesk.views.util.EditorCallback;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.HTMLEditor;

/**
 *
 * @author MNicaretta
 */
public class SurveyEditor
        extends DefaultEditor<Survey>
{
    public SurveyEditor(EditorCallback<Survey> callback)
    {
        super(callback);

        initComponents();

        setTitle("Editor de Pesquisas");
        setHeaderText("Editor de Pesquisas");

        setSource(callback.getSource());
    }

    private void setSource(Survey value)
    {
        this.source = value;

        titleField.setText(value.getTitle());
        createdField.setDate(value.getCreatedDate());
        typeField.setValue(SurveyUtilities.getType(value.getType()));
        infoField.setHtmlText(value.getInfo());
    }

    @Override
    protected void obtainInput()
    {
        source.setTitle(titleField.getText());
        source.setType(typeField.getValue().getValue());
        source.setInfo(infoField.getHtmlText());
        
        //TEMPORARIO
        source.setOwner(new User(1, "", "", "", "", "", 0));
        source.setCategory(new Category(1, null, "", ""));
        source.setForm(new Form(1, "", ""));
    }

    private void initComponents()
    {
        setWidth(500);
        setHeight(400);

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int count = 0;

        HBox.setHgrow(typeField, Priority.ALWAYS);
        infoField.setPrefHeight(300);

        createdField.setDisable(true);
        typeField.setItems(SurveyUtilities.getItems());

        gridPane.addRow(count++, titleLabel, titleField);
        gridPane.addRow(count++, createdLabel, createdField);
        gridPane.addRow(count++, typeLabel, typeField);
        gridPane.add(infoLabel, 0, count++, 2, 1);
        gridPane.add(infoField, 0, count++, 2, 1);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1, col2);

        getDialogPane().setContent(gridPane);
    }

    private GridPane gridPane = new GridPane();

    private Label titleLabel = new Label("Título");
    private TextField titleField = new TextField();

    private Label createdLabel = new Label("Criado em:");
    private DatePicker createdField = new DatePicker();

    private Label typeLabel = new Label("Tipo:");
    private ComboBox<Item> typeField = new ComboBox();

    private Label infoLabel = new Label("Informações:");
    private HTMLEditor infoField = new HTMLEditor();
}

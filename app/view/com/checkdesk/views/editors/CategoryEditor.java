/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.model.data.Category;
import com.checkdesk.views.util.Callback;
import java.util.Iterator;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.HTMLEditor;

/**
 *
 * @author arthu
 */
public class CategoryEditor
        extends DefaultEditor<Category>
{
    public CategoryEditor(Callback<Category> callback)
    {
        super(callback);

        initComponents();

        setTitle("Editor de Usuários");
        setHeaderText("Editor de Usuários");

        setSource(callback.getSource());
    }

    private void setSource(Category value)
    {
        this.source = value;

        nameField.setText(value.getName());
        infoField.setHtmlText(value.getInfo());

        setValidations();
    }

    @Override
    public void resize()
    {
        if (gridPane != null)
        {
            double max = 50;

            for (Iterator<Node> it = gridPane.getChildren().iterator(); it.hasNext();)
            {
                Node node = it.next();
                
                if (node instanceof Label)
                {
                    max = Math.max(max, ((Label) node).getWidth());
                }
            }

            minWidthLabel.set(max);
        }
    }

    @Override
    protected void obtainInput()
    {
        source.setName(nameField.getText());
        source.setInfo(infoField.getHtmlText());
    }

    private void setValidations()
    {
        addValidation(nameField);
    }

    private void initComponents()
    {
        getDialogPane().setPrefWidth(600);
        getDialogPane().setPrefHeight(400);

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int count = 0;

        gridPane.addRow(count++, nameLabel, nameField);
        gridPane.addRow(count++, infoLabel, infoField);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.minWidthProperty().bind(minWidthLabel);
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1, col2);

        getDialogPane().setContent(gridPane);
    }

    private GridPane gridPane = new GridPane();

    private Label nameLabel = new Label("Nome:");
    private TextField nameField = new TextField();

    private Label infoLabel = new Label("Info:");
    private HTMLEditor infoField = new HTMLEditor();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.details.util;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.model.data.Group;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

/**
 *
 * @author MNicaretta
 */
public class DetailsTable
        extends GridPane
{
    private DateFormat df = DateFormat.getDateTimeInstance();
    
    private int labelWidth;
    private int countRow = 0;

    public DetailsTable()
    {
        this(0);
    }

    public DetailsTable(int labelWidth)
    {
        this.labelWidth = labelWidth;
        
        initComponents();
    }
    
    public DetailsTable addItem(String label, String value)
    {
        addRow(countRow++, getItemLabel(label), getItemValue(value));
        
        return this;
    }
    
    public DetailsTable addItem(String label, Object value)
    {
        String v = "n/d";
        
        if (value instanceof Date)
        {
            v = df.format((Date) value);
        }
        
        else if (value instanceof Group)
        {
            listToString(((Group) value).getUsers());
        }
        
        else if (value instanceof List)
        {
            listToString(((Group) value).getUsers());
        }
        
        else if(value != null)
        {
            v = value.toString();
        }
        
        return addItem(label, v);
    }
    
    public DetailsTable addItemHtml(String label, String value)
    {
        addRow(countRow++, getItemLabel(label), getHtmlValue(value));
        
        return this;
    }
    
    private Label getItemLabel(String label)
    {
        Label result = new Label(label + ": ");
        result.getStyleClass().add("details-label");
        result.setAlignment(Pos.TOP_LEFT);
        
        setValignment(result, VPos.TOP);
        
        return result;
    }
    
    private Label getItemValue(String value)
    {
        Label result = new Label(value);
        result.getStyleClass().add("details-value");
        
        return result;
    }
    
    private WebView getHtmlValue(String value)
    {
        WebView result = new WebView();
        
        result.getEngine().setUserStyleSheetLocation(ResourceLocator.getInstance().getStyleResource("details.css"));
        result.getEngine().loadContent("<body class=\"details-html\" contenteditable=\"false\">" + value + "</body>");
        
        result.setPrefHeight(-1);
        
        //calc height
        result.getEngine().documentProperty().addListener((obj, prev, newv) ->
        {
            String heightText = result.getEngine().executeScript(
                    "var body = document.body," +
                    "    html = document.documentElement;" +
                    "Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight);"
            ).toString();

            Double height = Double.parseDouble(heightText.replace("px", "")) + 15;
            result.setPrefHeight(height);
        });
        
        return result;
    }
    
    private String listToString(List list)
    {
        StringJoiner joiner = new StringJoiner(", ");
        
        for (Object item : list)
        {
            joiner.add(item.toString());
        }
        
        return joiner.toString();
    }

    private void initComponents()
    {
        ColumnConstraints labelColumn = new ColumnConstraints();
        ColumnConstraints valueColumn = new ColumnConstraints();
        
        if (labelWidth > 0)
        {
            labelColumn.setMinWidth(labelWidth);
        }
        
        valueColumn.setHgrow(Priority.ALWAYS);
        
        getColumnConstraints().addAll(labelColumn, valueColumn);
        
        widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            valueColumn.setMaxWidth(getWidth() - labelWidth);
        });
    }
}

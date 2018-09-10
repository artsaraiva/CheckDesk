/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.details.util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 *
 * @author MNicaretta
 */
public class Table
        extends GridPane
{
    private DateFormat df = DateFormat.getDateTimeInstance();

    private int countRow = 0;
    private int countColumn = 0;
    private List<Double> widths = new ArrayList<>();

    public Table(String... headers)
    {
        countColumn = headers.length;
        Label[] labels = new Label[headers.length];

        for (int i = 0; i < labels.length; i++)
        {
            labels[i] = new Label(headers[i]);
            labels[i].getStyleClass().add("details-header");
        }

        if (labels.length != 0)
        {
            addRow(countRow++, labels);
        }

        widthProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) ->
        {
            resize();
        });

        resize();
    }

    public Table addRow(Object... objects)
    {
        countColumn = Math.max(countColumn, objects.length);
        Label[] labels = new Label[objects.length];

        for (int i = 0; i < labels.length; i++)
        {
            String v = "";

            if (objects[i] != null)
            {
                v = objects[i].toString();

                if (objects[i] instanceof Date)
                {
                    v = df.format((Date) objects[i]);
                }
            }

            labels[i] = new Label(v);
        }

        addRow(countRow++, labels);

        return this;
    }

    public Table setWidths(Double... widths)
    {
        countColumn = Math.max(countColumn, widths.length);
        double total = 0;

        for (Double width : widths)
        {
            total += width;
        }

        this.widths.clear();

        for (Double width : widths)
        {
            this.widths.add(width / total);
        }

        return this;
    }

    private void resize()
    {
        getColumnConstraints().clear();
        Double width = getWidth() / countColumn;

        for (int i = 0; i < countColumn; i++)
        {
            Double realWidth = Double.NaN;

            if (i < widths.size())
            {
                realWidth = getWidth() * widths.get(i);
            }

            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(!realWidth.isNaN() ? realWidth : width);
            
            getColumnConstraints().add(col);

            if (!realWidth.isNaN())
            {
                width += (width - realWidth) / (countColumn - i - 1);
            }
        }
    }
}

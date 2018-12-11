/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

/**
 *
 * @author MNicaretta
 */
public abstract class Validation
{
    private static EventType VALIDATE = new EventType("onValidate");
    
    public static Callback<Control> getCallback(Control control)
    {
        return new Callback<Control>(control)
        {
            @Override
            public void handle(Event t)
            {
                Control field = getSource();
                String error = (String) t.getSource();
                
                if (error == null || error.isEmpty())
                {
                    field.setBorder(null);
                    field.setTooltip(null);
                    field.setStyle("-fx-faint-focus-color: transparent;");
                }

                else
                {
                    field.setTooltip(new Tooltip(error));
                    field.setBorder(new Border(new BorderStroke(Paint.valueOf("#FF0000"),
                                               BorderStrokeStyle.SOLID,
                                               new CornerRadii(5),
                                               BorderWidths.DEFAULT,
                                               new Insets(-1))));
                    
                    field.setStyle("-fx-focus-color: transparent;-fx-faint-focus-color: transparent;");
                }
            }
        };
    }
    
    protected Node node;
    protected Callback callback;
    
    protected List<Validation> bindedValidations = new ArrayList<>();
    
    public Validation(Control control)
    {
        this(control, control.focusedProperty(), getCallback(control));
    }
    
    public Validation(Node node, ObservableValue changedProperty, Callback callback)
    {
        this.node = node;
        this.callback = callback;
        
        changedProperty.addListener((ObservableValue ov, Object t, Object t1) ->
        {
            doValidate();
        });
        
        doValidate();
    }
    
    protected abstract String validate();
    
    public void bind(Validation... validations)
    {
        bindedValidations.addAll(Arrays.asList(validations));
    }
    
    public boolean isValid()
    {
        return validate() == null || validate().isEmpty();
    }
    
    private void doValidate()
    {
        callback.handle(new Event(validate(), node, VALIDATE));
        
        for (Validation binded : bindedValidations)
        {
            binded.callback.handle(new Event(binded.validate(), binded.node, VALIDATE));
        }
    }
}

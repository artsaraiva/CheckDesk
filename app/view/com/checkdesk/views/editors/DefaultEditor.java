/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.control.ValidationController;
import com.checkdesk.views.parts.MaskField;
import com.checkdesk.views.util.EditorButton;
import com.checkdesk.views.util.Callback;
import com.checkdesk.views.parts.Prompts;
import com.checkdesk.views.util.Validation;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author MNicaretta
 */
public class DefaultEditor<T>
        extends Dialog<T>
{
    public static final EventType ACCEPT_INPUT = new EventType("acceptInput");

    protected final DoubleProperty minWidthLabel = new DoublePropertyBase()
    {
        @Override
        public Object getBean()
        {
            return DefaultEditor.this;
        }

        @Override
        public String getName()
        {
            return "minWidthLabel";
        }

    };

    protected Callback callback;
    protected T source;
    protected ButtonType btSave = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
    protected ButtonType btCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    protected ButtonType btNext = new ButtonType("Próximo", ButtonBar.ButtonData.NEXT_FORWARD);
    protected ButtonType btPrevious = new ButtonType("Voltar", ButtonBar.ButtonData.BACK_PREVIOUS);
    private ButtonType selectedButton;

    private List<Validation> validations = new ArrayList<>();

    public DefaultEditor(Callback<T> callback)
    {
        this.callback = callback;
        this.source = callback.getSource();

        initComponents();
    }

    public boolean onSave()
    {
        try
        {
            if (validateInput())
            {
                obtainInput();

                callback.handle(new Event(source, this, ACCEPT_INPUT));

                return true;
            }

            Prompts.info("Existem campos com erros!");
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return false;
    }

    protected boolean validateInput()
    {
        boolean result = true;

        for (Validation validation : getValidations())
        {
            result &= validation.isValid();
        }

        return result;
    }

    public List<Validation> getValidations()
    {
        return validations;
    }

    protected void obtainInput()
    {
    }

    protected void nextPage()
    {
    }

    protected void previousPage()
    {
    }

    public void resize()
    {
    }

    protected void addValidation(TextField field)
    {
        validations.add(ValidationController.addValidation(field));
        setLimit(field, 200);
    }

    protected void addValidation(MaskField field)
    {
        validations.add(field.getValidation());
        setLimit(field, field.getLimit());
    }
    
    protected void addValidation(Validation validation)
    {
        validations.add(validation);
    }

    protected void setLimit(TextField field, int limit)
    {
        field.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue)
            {
                int l = limit;

                if (field instanceof PasswordField)
                {
                    l = 100;
                }

                else if (field instanceof MaskField)
                {
                    l = ((MaskField) field).getLimit();
                }

                if (newValue == null || newValue.length() > l)
                {
                    ((TextField) field).setText(oldValue);
                }
            }
        });
    }

    protected void addButton(ButtonType type)
    {
        getDialogPane().getButtonTypes().add(type);
    }

    protected void removeButton(ButtonType type)
    {
        getDialogPane().getButtonTypes().remove(type);
    }

    protected EditorButton getButton(ButtonType type)
    {
        return (EditorButton) getDialogPane().lookupButton(type);
    }

    private void initComponents()
    {
        setDialogPane(new Pane());
        getDialogPane().getStylesheets().add(ResourceLocator.getInstance().getStyleResource("default.css"));
        setResizable(false);

        getDialogPane().getButtonTypes().addAll(btSave, btCancel);

        setOnCloseRequest(new EventHandler()
        {
            @Override
            public void handle(Event t)
            {
                if (selectedButton == btSave)
                {
                    if (!onSave())
                    {
                        t.consume();
                    }
                }

                else if (selectedButton == btNext)
                {
                    t.consume();
                    nextPage();
                }

                else if (selectedButton == btPrevious)
                {
                    t.consume();
                    previousPage();
                }
            }

        });

        setResultConverter(new javafx.util.Callback()
        {
            @Override
            public Object call(Object p)
            {
                return selectedButton = (ButtonType) p;
            }

        });
    }

    private class Pane
            extends DialogPane
    {
        public Pane()
        {
            widthProperty()
                    .addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
                    {
                        DefaultEditor.this.resize();
                    });

            heightProperty()
                    .addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
                    {
                        DefaultEditor.this.resize();
                    });
        }

        @Override
        protected Node createButton(ButtonType bt)
        {
            final Button button = (Button) super.createButton(bt);
            EditorButton editorButton = new EditorButton(bt);
            editorButton.setOnMouseClicked((MouseEvent t) ->
            {
                button.fire();
            });

            return editorButton;
        }

    }
}

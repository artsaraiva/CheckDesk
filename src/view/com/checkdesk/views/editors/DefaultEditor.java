/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.util.EditorButton;
import com.checkdesk.views.util.EditorCallback;
import com.checkdesk.views.util.Prompts;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 *
 * @author MNicaretta
 */
public class DefaultEditor<T>
        extends Dialog<T>
{
    public static final EventType ACCEPT_INPUT = new EventType("acceptInput");

    protected EditorCallback callback;
    protected T source;
    protected ButtonType btSave = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
    protected ButtonType btCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    protected ButtonType btNext = new ButtonType("Próximo", ButtonBar.ButtonData.NEXT_FORWARD);
    protected ButtonType btPrevious = new ButtonType("Voltar", ButtonBar.ButtonData.BACK_PREVIOUS);
    private ButtonType selectedButton;

    public DefaultEditor(EditorCallback<T> callback)
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
        return true;
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

        getDialogPane().getButtonTypes().addAll(btCancel, btSave);

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

        setResultConverter(new Callback()
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

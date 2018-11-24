/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.parts.MaskField;
import com.checkdesk.views.util.EditorButton;
import com.checkdesk.views.util.EditorCallback;
import com.checkdesk.views.parts.Prompts;
import com.checkdesk.views.util.Validation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

/**
 *
 * @author MNicaretta
 */
public class DefaultEditor<T>
        extends Dialog<T>
{
    public static final EventType ACCEPT_INPUT = new EventType("acceptInput");

    public static Validation getTextValidation(final TextField field)
    {
        return new Validation()
        {
            @Override
            public boolean validate()
            {
                return field.getText() != null && !field.getText().isEmpty();
            }

            @Override
            public String getError()
            {
                return "Esse campo deve ser preenchido";
            }

        };
    };
    
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
    
    protected EditorCallback callback;
    protected T source;
    protected ButtonType btSave = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
    protected ButtonType btCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    protected ButtonType btNext = new ButtonType("Próximo", ButtonBar.ButtonData.NEXT_FORWARD);
    protected ButtonType btPrevious = new ButtonType("Voltar", ButtonBar.ButtonData.BACK_PREVIOUS);
    private ButtonType selectedButton;

    private Map<Control, Validation> controlMap = new HashMap<>();
    private Map<Control, List<Control>> bindValidationMap = new HashMap<>();
    
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
        boolean result = true;

        for (Control field : controlMap.keySet())
        {
            result &= controlMap.get(field).validate();
        }

        return result;
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
    
    protected void addValidation( final TextField field )
    {
        addValidation( field, getTextValidation( field ) );
    }
    
    protected void addValidation( final MaskField field )
    {
        addValidation( field, field );
    }
    
    protected void addValidation( final Control control, Validation validation )
    {
        addValidation( control, 200, validation );
    }
    
    protected void addValidation( final Control control, final int limit, Validation validation )
    {
        if ( control != null && validation != null )
        {
            controlMap.put( control, validation );
            validate( control );
            
            control.focusedProperty().addListener( new ChangeListener<Boolean>()
            {
                @Override
                public void changed( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue )
                {
                    if ( oldValue && !newValue )
                    {
                        validate( control );
                        
                        if ( bindValidationMap.containsKey( control ) )
                        {
                            for ( Control c : bindValidationMap.get( control ) )
                            {
                                validate( c );
                            }
                        }
                    }
                }
            } );
            
            // set limit
            if ( control instanceof TextField )
            {
                ((TextField) control).textProperty().addListener( new ChangeListener<String>() 
                {
                    @Override
                    public void changed( ObservableValue<? extends String> observableValue, String oldValue, String newValue )
                    {
                        int l = limit;
                        
                        if ( control instanceof PasswordField )
                        {
                            l = 100;
                        }
                        
                        else if ( control instanceof MaskField )
                        {
                            l = ((MaskField) control).getLimit();
                        }

                        if ( newValue == null || newValue.length() > l )
                        {
                            ((TextField) control).setText( oldValue );
                        }
                    }
                } );
            }
        }
    }
    
    protected void bindValidations( Control... controls )
    {
        for ( Control a : controls )
        {
            for ( Control b : controls )
            {
                if ( a != b )
                {
                    List<Control> binds = bindValidationMap.get( a );
                    
                    if ( binds == null )
                    {
                        bindValidationMap.put( a, binds = new ArrayList<>() );
                    }
                    
                    binds.add( b );
                }
            }
        }
    }
    
    private void validate( Control control )
    {
        Validation validation = controlMap.get( control );
        
        if ( !validation.validate() )
        {
            control.setTooltip( new Tooltip( validation.getError() ) );
            control.setBorder( new Border( new BorderStroke( Paint.valueOf( "#FF0000" ),
                                                             BorderStrokeStyle.SOLID,
                                                             new CornerRadii( 5 ),
                                                             BorderWidths.DEFAULT,
                                                             new Insets( -1 ) ) ) );

            control.setStyle( "-fx-focus-color: transparent;-fx-faint-focus-color: transparent;" );
        }

        else
        {
            control.setBorder( null );
            control.setTooltip( null );
            control.setStyle( "-fx-faint-focus-color: transparent;" );
        }
    }
    
    public void clearValidations()
    {
        controlMap.clear();
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
        public Pane()
        {
            widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
            {
                DefaultEditor.this.resize();
            });

            heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
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

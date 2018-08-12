/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.views.util.EditorCallback;
import com.checkdesk.views.util.Prompts;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Callback;

/**
 *
 * @author MNicaretta
 */
public class DefaultEditor<T>
        extends Dialog<T>
{
    public static final EventType ACCEPT_INPUT = new EventType( "acceptInput" );
    
    protected EditorCallback callback;
    protected T source;
    private ButtonType btSave   = new ButtonType( "Salvar",   ButtonBar.ButtonData.OK_DONE );
    private ButtonType btCancel = new ButtonType( "Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE );
    private ButtonType selectedButton;
    
    public DefaultEditor( EditorCallback<T> callback )
    {
        this.callback = callback;
        this.source = callback.getSource();
        
        initComponents();
    }
    
    public boolean onSave()
    {
        try
        {
            if ( validateInput() )
            {
                obtainInput();

                callback.handle( new Event( source, this, ACCEPT_INPUT ) );
                
                return true;
            }
            
            Prompts.info( "Existem campos com erros!" );
        }
        
        catch ( Exception e )
        {
            e.printStackTrace();
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
    
    private void initComponents()
    {
        setResizable( false );
        
        getDialogPane().getButtonTypes().addAll( btCancel, btSave );
        
        setOnCloseRequest( new EventHandler()
        {
            @Override
            public void handle( Event t ) 
            {
                if ( selectedButton == btSave )
                {
                    if ( !onSave() )
                    {
                        t.consume();
                    }
                }
            }
        } );

        setResultConverter( new Callback() 
        {
            @Override
            public Object call(  Object p ) 
            {
                return selectedButton = (ButtonType) p;
            }
        } );
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.model.data.Form;
import com.checkdesk.model.util.FormWrapper;
import com.checkdesk.views.panes.FormEditorPane;
import com.checkdesk.views.util.EditorCallback;

/**
 *
 * @author MNicaretta
 */
public class FormEditor
        extends DefaultEditor<FormWrapper>
{
    public FormEditor(EditorCallback<FormWrapper> callback)
    {
        super(callback);
        
        initComponents();
        
        setTitle("Editor de Formulário");
        setHeaderText("Editor de Formulário");
        
        setSource(callback.getSource());
    }
    
    private void setSource(FormWrapper value)
    {
        formPane.setSource(value);
    }

    @Override
    protected void obtainInput()
    {
        formPane.obtainInput();
    }
    
    private void initComponents()
    {
        setWidth(1000);
        setHeight(500);
        
        formPane.setPrefSize(getWidth(), getHeight());
        
        getDialogPane().setContent(formPane);
    }
    
    private FormEditorPane formPane = new FormEditorPane();
}

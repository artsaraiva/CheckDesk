/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.pickers;

import com.checkdesk.control.util.AttachmentUtilities;
import com.checkdesk.model.data.Attachment;
import com.checkdesk.model.data.Question;
import com.checkdesk.views.parts.DefaultTable;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.MenuItem;

/**
 *
 * @author MNicaretta
 */
public class AttachmentPicker
        extends ItemPicker<List<Attachment>>
{
    private Question question;
    
    public AttachmentPicker()
    {
        initComponents();

        setTitle("Seletor de Anexos");
    }
    
    public void setQuestion(Question question)
    {
        this.question = question;
        table.setItems(AttachmentUtilities.getAttachments(question));
    }

    @Override
    public List<Attachment> getSelected()
    {
        return table.getItems();
    }

    @Override
    protected boolean validate()
    {
        return true;
    }
    
    private void addAttachment()
    {
        Attachment attachment = new Attachment();
        attachment.setQuestion(question);
        
        if (AttachmentUtilities.editAttachment(attachment))
        {
            table.getItems().add(attachment);
            table.setItems(table.getItems());
        }
    }
    
    private void editAttachment()
    {
        Attachment attachment = table.getSelectedItem();
        
        if (attachment != null)
        {
            if (AttachmentUtilities.editAttachment(attachment))
            {
                table.setItems(table.getItems());
            }
        }
    }
    
    private void deleteAttachment()
    {
        Attachment attachment = table.getSelectedItem();
        
        if (attachment != null)
        {
            if (AttachmentUtilities.deleteAttachment(attachment))
            {
                table.getItems().remove(attachment);
                table.setItems(table.getItems());
            }
        }
    }
    
    private void initComponents()
    {
        getDialogPane().setPrefSize(800, 500);
        getDialogPane().setContent(table);
        
        table.addEventHandler(DefaultTable.Events.ON_ADD, (Event t) ->
        {
            addAttachment();
        });
        
        table.setActions(new MenuItem[]
        {
            editItem,
            deleteItem
        });
    }
    
    private DefaultTable<Attachment> table = new DefaultTable<>();
    
    private MenuItem editItem = new MenuItem("Editar");
    private MenuItem deleteItem = new MenuItem("Excluir");
    {
        editItem.setOnAction((ActionEvent t) ->
        {
            editAttachment();
        });
        
        deleteItem.setOnAction((ActionEvent t) ->
        {
            deleteAttachment();
        });
    }
}

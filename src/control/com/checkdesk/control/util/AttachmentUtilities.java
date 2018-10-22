/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Attachment;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.views.editors.AttachmentEditor;
import com.checkdesk.views.parts.Prompts;
import com.checkdesk.views.util.EditorCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javafx.event.Event;

/**
 *
 * @author MNicaretta
 */
public class AttachmentUtilities
{
    public static List<Attachment> getAttachments(Question question)
    {
        List<Attachment> result = new ArrayList();
        
        if (question.getId() != 0)
        {
            try
            {
                result = EntityService.getInstance().getValues(Attachment.class,
                                                               Arrays.asList(new Parameter("question",
                                                                             Attachment.class.getDeclaredField("question"),
                                                                             question,
                                                                             Parameter.COMPARATOR_EQUALS)));
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
        
        return result;
    }
    
    public static boolean editAttachment(Attachment attachment)
    {
        final boolean[] b = {false};
        
        new AttachmentEditor(new EditorCallback<Attachment>(attachment)
        {
            @Override
            public void handle(Event t)
            {
                b[0] = true;
            }
        }).showAndWait();
        
        return b[0];
    }
    
    public static boolean deleteAttachment(Attachment attachment)
    {
        return Prompts.confirm("Exclusão de Anexo", "Deseja realmente excluir o anexo selecionado?");
    }
    
    public static void saveAttachments(Question question) throws Exception
    {
        List<Attachment> deleteAttachements = EntityService.getInstance().loadValues(Attachment.class,
                                                                                     Arrays.asList(new Parameter("question",
                                                                                                   Attachment.class.getDeclaredField("question"),
                                                                                                   question,
                                                                                                   Parameter.COMPARATOR_EQUALS)));
        
        for (Attachment deletable : deleteAttachements)
        {
            boolean delete = true;

            for (Attachment attachment : (Set<Attachment>) question.getAttachments())
            {
                if (deletable.getId() == attachment.getId())
                {
                    delete = false;
                    break;
                }
            }

            if (delete)
            {
                EntityService.getInstance().delete(deletable);
//                    ApplicationController.getInstance().deleteFile(deletable);
            }
        }

        for (Attachment attachment : (Set<Attachment>) question.getAttachments())
        {
            if (attachment.getId() == 0)
            {
                EntityService.getInstance().save(attachment);
            }

            else
            {
                EntityService.getInstance().update(attachment);
            }

            ApplicationController.getInstance().saveFile(attachment);
        }
    }
    
    public static void deleteAttachments(Question question) throws Exception
    {
        for (Attachment attachment : (Set<Attachment>) question.getAttachments())
        {
            EntityService.getInstance().delete(attachment);
//            ApplicationController.getInstance().deleteFile(attachment);
        }
    }
}

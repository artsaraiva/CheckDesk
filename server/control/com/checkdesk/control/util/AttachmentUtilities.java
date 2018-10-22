/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Attachment;
import com.server.checkdesk.main.HandleClient;
import java.io.File;

/**
 *
 * @author MNicaretta
 */
public class AttachmentUtilities
{
    public static void download(Attachment attachment, HandleClient client)
    {
        try
        {
            client.download(getFile(attachment));
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
    
    public static void upload(Attachment attachment, HandleClient client)
    {
        File file = getFile(attachment);
        
        try
        {
            client.upload(file);
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
    
    private static File getFile(Attachment attachment)
    {
        String name = String.format("%06x", attachment.getId());
        
        File file = new File("files" + File.separator + "attachments");

        if (!file.exists() || !file.isDirectory())
        {
            file.mkdirs();
        }
        
        return new File("files" + File.separator + "attachments" + File.separator + name);
    }
}

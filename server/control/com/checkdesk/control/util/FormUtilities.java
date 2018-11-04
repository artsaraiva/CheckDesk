/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.db.service.EntityService;

/**
 *
 * @author MNicaretta
 */
public class FormUtilities
{
    public static Form getForm(int formId)
    {
        Form result = null;
        
        try
        {
            result = (Form) EntityService.getInstance().getValue(Form.class, formId);
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Category;
import com.checkdesk.model.db.service.EntityService;
import java.util.List;

/**
 *
 * @author MNicaretta
 */
public class CategoryUtilities
{
    public static Category getCategory(int id)
    {
        Category result = null;
        
        try
        {
            result = (Category) EntityService.getInstance().getValue(Category.class, id);
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
    
    public static List<Category> getCategories()
    {
        List<Category> result = null;
        
        try
        {
            result = EntityService.getInstance().getValues(Category.class);
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
}

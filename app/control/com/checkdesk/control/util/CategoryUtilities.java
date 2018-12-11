/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Category;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.views.editors.CategoryEditor;
import com.checkdesk.views.parts.Prompts;
import com.checkdesk.views.util.Callback;
import java.util.List;
import javafx.event.Event;

/**
 *
 * @author MNicaretta
 */
public class CategoryUtilities
{
    public static void addCategory()
    {
        Category category = new Category();
        category.setOwnerId(ApplicationController.getInstance().getActiveUser().getId());
        
        new CategoryEditor(new Callback<Category>(category)
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    EntityService.getInstance().insert(getSource());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }
    
    public static void editCategory(Category category)
    {
        new CategoryEditor(new Callback<Category>(category)
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    EntityService.getInstance().update(getSource());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }
    
    public static void deleteCategory(final Category category)
    {
        if (Prompts.confirm("Exlusão de Categoria", "Deseja realmente excluir a categoria?"))
        {
            try
            {
                EntityService.getInstance().delete(category);
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
    }
    
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

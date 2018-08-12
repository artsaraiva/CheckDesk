/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.model.data.Survey;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.views.editors.SurveyEditor;
import com.checkdesk.views.util.EditorCallback;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;

/**
 *
 * @author MNicaretta
 */
public class SurveyUtilities
{
    private static final Item TYPE_PUBLIC    = new Item( "Pública", Survey.TYPE_PUBLIC );
    private static final Item TYPE_PRIVATE   = new Item( "Privada", Survey.TYPE_PRIVATE );
    private static final Item TYPE_ANONYMOUS = new Item( "Anônima", Survey.TYPE_ANONYMOUS );
    private static final Item TYPE_TOTEM     = new Item( "Totem", Survey.TYPE_TOTEM );
    
    public static void addSurvey()
    {
        Survey survey = new Survey();
        survey.setCreatedDate(new Date(System.currentTimeMillis()));
        
        new SurveyEditor(new EditorCallback<Survey>(survey)
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    EntityService.getInstance().save(getSource());
                }
                
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).show();
    }
    
    public static ObservableList<Item> getItems()
    {
        return FXCollections.observableArrayList( TYPE_PUBLIC, TYPE_PRIVATE, TYPE_ANONYMOUS, TYPE_TOTEM );
    }
    
    public static Item getType(int type)
    {
        Item result = null;
        
        for (Item item : getItems())
        {
            if (item.getValue() == type)
            {
                result = item;
                break;
            }
        }
        
        return result;
    }
}

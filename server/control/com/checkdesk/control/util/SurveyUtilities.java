/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.db.service.EntityService;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author MNicaretta
 */
public class SurveyUtilities
{

    private static final Item TYPE_PUBLIC = new Item("Pública", Survey.TYPE_PUBLIC);
    private static final Item TYPE_PRIVATE = new Item("Privada", Survey.TYPE_PRIVATE);
    private static final Item TYPE_ANONYMOUS = new Item("Anônima", Survey.TYPE_ANONYMOUS);
    private static final Item TYPE_TOTEM = new Item("Totem", Survey.TYPE_TOTEM);

    public static Survey getValue(int surveyId)
    {
        Survey result = null;

        try
        {
            result = (Survey) EntityService.getInstance().getValue(Survey.class, surveyId);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static List<Survey> getValues()
    {
        List<Survey> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(Survey.class);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static ObservableList<Item> getItems()
    {
        return FXCollections.observableArrayList(TYPE_PUBLIC, TYPE_PRIVATE, TYPE_ANONYMOUS, TYPE_TOTEM);
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

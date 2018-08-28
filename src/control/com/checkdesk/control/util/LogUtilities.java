/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Log;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author MNicaretta
 */
public class LogUtilities
{
    public static final Item EVENT_ADD         = new Item("Adicionar",                  Log.EVENT_ADD);
    public static final Item EVENT_UPDATE      = new Item("Editar",                     Log.EVENT_UPDATE);
    public static final Item EVENT_DELETE      = new Item("Excluir",                    Log.EVENT_DELETE);
    public static final Item EVENT_ACTIVE_LOGS = new Item("Ativar/Desativar auditoria", Log.EVENT_ACTIVE_LOGS);
    
    private static final int OPTION_USER   = 0;
    private static final int OPTION_DATE   = 1;
    private static final int OPTION_ACTION = 2;

    public static ObservableList<Item> getFilterOptions()
    {
        return FXCollections.observableArrayList(new Item("Usuário", OPTION_USER),
                                                 new Item("Data",    OPTION_DATE),
                                                 new Item("Ação",    OPTION_ACTION));
    }

    public static List filtersFor(Item item)
    {
        List result = new ArrayList();

        try
        {
            switch (item.getValue())
            {
                case OPTION_USER:
                    result = EntityService.getInstance().getValues(User.class);
                    break;

                case OPTION_DATE:
                    result = getDateList();
                    break;

                case OPTION_ACTION:
                    result = getEventsList();
                    break;
            }
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    private static List<Date> getDateList()
    {
        List objects = new ArrayList();

        try
        {
            objects = EntityService.getInstance().getFieldValues(Log.class.getDeclaredField("timestamp"), Log.class);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return (List<Date>) objects;
    }

    private static List<Item> getEventsList()
    {
        return Arrays.asList(EVENT_ADD, EVENT_UPDATE, EVENT_DELETE, EVENT_ACTIVE_LOGS);
    }
    
    public static Item getEvent(int type)
    {
        Item result = null;

        for (Item item : getEventsList())
        {
            if (item.getValue() == type)
            {
                result = item;
                break;
            }
        }

        return result;
    }

    public static List<Log> getLogs(Item item, Object value)
    {
        List<Log> result = new ArrayList<>();

        Field field = null;

        try
        {
            switch (item.getValue())
            {
                case OPTION_USER:
                    field = Log.class.getDeclaredField("user");
                    break;

                case OPTION_DATE:
                    field = Log.class.getDeclaredField("timestamp");
                    break;

                case OPTION_ACTION:
                    field = Log.class.getDeclaredField("event");
                    value = value instanceof Item ? ((Item) value).getValue() : value;
                    break;
            }

            if (field != null)
            {
                result = EntityService.getInstance().getValues(Log.class, Arrays.asList(new Parameter(field.getName(),
                                                                                                      field,
                                                                                                      value,
                                                                                                      Parameter.COMPARATOR_EQUALS)));
            }
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }
}
